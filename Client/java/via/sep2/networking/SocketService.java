package via.sep2.networking;

import dtos.Request;
import dtos.Response;
import dtos.error.ErrorResponse;
import javafx.application.Platform;
import via.sep2.data.ClientData;
import via.sep2.startup.ViewHandler;
import via.sep2.ui.popup.MessageType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Troels, Mario
 */
public class SocketService {
    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final PropertyChangeSupport support;
    private volatile boolean running;
    private Thread listenerThread;

    public SocketService(String host, int port) {
        this.host = host;
        this.port = port;
        this.support = new PropertyChangeSupport(this);
        this.running = false;
    }

    public static Object sendRequest(Request request) {
        try (Socket socket = new Socket("localhost", 6032);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            outputStream.writeObject(request);
            Response response = (Response) inputStream.readObject();
            switch (response.status()) {
                case "SUCCESS" -> {
                    return response.payload();
                }
                case "UPDATE" -> {
                    switch ((String) response.payload()) {
                        case "dining" -> Platform.runLater(
                            ClientData::refreshDining);
                        case "reservations" -> Platform.runLater(
                            ClientData::refreshReservations);
                        case "people" -> Platform.runLater(
                            ClientData::refreshPeople);
                        case "all" -> Platform.runLater(
                            ClientData::refreshAllData);
                    }
                }
                case "ERROR" -> throw new RuntimeException(((ErrorResponse) response.payload()).errorMessage());
                case "notification"->Platform.runLater(() ->ClientData.sendNotification((String)response.payload()));
                default -> throw new RuntimeException("Unknown server status code: " + response.status());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not connect to server!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid response from server.");
        }
        return null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void start() throws IOException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        running = true;

        listenerThread = new Thread(this::listenForResponses);
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void listenForResponses() {
        final int MAX_RECONNECT_ATTEMPTS = 5;
        int reconnectAttempts = 0;

        while (running && reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
            try {
                Response response = (Response) inputStream.readObject();
                reconnectAttempts = 0; // Reset on successful read
                handleResponse(response);
            } catch (IOException e) {
                reconnectAttempts++;
                if (running && reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
                    try {
                        socket.close();
                        socket = new Socket(host, port);
                        outputStream = new ObjectOutputStream(socket.getOutputStream());
                        inputStream = new ObjectInputStream(socket.getInputStream());
                        Platform.runLater(()->ViewHandler.popupMessage(MessageType.SUCCESS,"Reconnected to server successfully!"));
                    } catch (IOException ex) {
                        System.err.println("Reconnect attempt " + reconnectAttempts + " failed: " + ex.getMessage());
                        try {
                            Thread.sleep(5000); // Wait before retrying
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                } else {
                    running = false;
                    try {
                        socket.close();
                        Platform.runLater(() -> support.firePropertyChange("connectionError", null, "Lost connection to server"));
                    } catch (IOException ex) {
                        System.err.println("Error closing socket: " + ex.getMessage());
                    }
                    Platform.runLater(()->ViewHandler.popupMessage(MessageType.ERROR,"Lost connection to the server"));

                }
            } catch (ClassNotFoundException e) {
                System.err.println("Invalid response from server: " + e.getMessage());
            }
        }
    }

    private void handleResponse(Response response) {
        if ("UPDATE".equals(response.status())) {
            //refresh the data from the needed list
            switch ((String) response.payload()) {
                case "dining" -> Platform.runLater(ClientData::refreshDining);
                case "reservations" -> Platform.runLater(ClientData::refreshReservations);
                case "person" -> Platform.runLater(ClientData::refreshPeople);
                case "all" -> Platform.runLater(ClientData::refreshAllData);
                default -> System.out.println("Unknown update type: " + response.payload());
            }
        }
        else if ("notification".equals(response.status()) && response.payload() instanceof String) {
            Platform.runLater(() -> ClientData.sendNotification((String) response.payload()));
        }
        else {
            System.out.println("Unknown response status: " + response.status());
        }
    }
}