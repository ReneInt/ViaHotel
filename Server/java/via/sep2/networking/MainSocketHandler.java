package via.sep2.networking;

import dtos.Request;
import dtos.Response;
import dtos.dining.DiningRequestDto;
import dtos.error.ErrorResponse;
import dtos.person.PersonDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import types.PersonType;
import via.sep2.networking.exceptions.InvalidActionException;
import via.sep2.networking.exceptions.NotFoundException;
import via.sep2.networking.exceptions.ServerFailureException;
import via.sep2.networking.requesthandlers.RequestHandler;
import via.sep2.services.exceptions.ValidationException;
import via.sep2.startup.ServiceProvider;
import via.sep2.utilities.logging.Formating;
import via.sep2.utilities.logging.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;

public class MainSocketHandler implements Runnable, PropertyChangeListener
{
    private final Socket clientSocket;
    private final ServiceProvider serviceProvider;
    private final Logger logger;
    private ObjectOutputStream outgoingData;

    public MainSocketHandler(Socket clientSocket,
        ServiceProvider serviceProvider)
    {
        this.clientSocket = clientSocket;
        this.serviceProvider = serviceProvider;
        this.logger = serviceProvider.getLogger();
        EventManager.addPropertyChangeListener(this);
    }

    @Override public void run()
    {
        ObjectInputStream incomingData = null;
        try
        {
            incomingData = new ObjectInputStream(clientSocket.getInputStream());
            outgoingData = new ObjectOutputStream(
                clientSocket.getOutputStream());
            handleRequestWithErrorHandling(incomingData, outgoingData);
        }
        catch (IOException e)
        {
            logger.log("Socket initialization error: " + e.getMessage() + "\n"
                + Arrays.toString(e.getStackTrace()), Formating.HEADER1);
        }
        finally
        {
            try
            {
                EventManager.removePropertyChangeListener(this);
                if (incomingData != null)
                    incomingData.close();
                if (outgoingData != null)
                    outgoingData.close();
                clientSocket.close();
            }
            catch (IOException e)
            {
                logger.log("Socket close error: " + e.getMessage());
            }
        }
    }

    private void handleRequestWithErrorHandling(ObjectInputStream incomingData,
        ObjectOutputStream outgoingData) throws IOException
    {
        try
        {
            handleRequest(incomingData, outgoingData);
        }
        catch (SQLException e)
        {
            logger.log("Database error: " + e.getMessage());
            sendErrorResponse(outgoingData, "ERROR", e.getMessage());
        }
        catch (NotFoundException | InvalidActionException |
               ValidationException e)
        {
            logger.log(e);
            sendErrorResponse(outgoingData, "ERROR", e.getMessage());
        }
        catch (ServerFailureException e)
        {
            logger.log(
                Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage(),
                Formating.HEADER1);
            sendErrorResponse(outgoingData, "SERVER_FAILURE", e.getMessage());
        }
        catch (ClassCastException e)
        {
            logger.log("Invalid request: " + e.getMessage());
            sendErrorResponse(outgoingData, "ERROR", "Invalid request");
        }
        catch (Exception e)
        {
            logger.log(
                Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage(),
                Formating.HEADER1);
            sendErrorResponse(outgoingData, "SERVER_FAILURE", e.getMessage());
        }
    }

    private void sendErrorResponse(ObjectOutputStream outgoingData,
        String status, String message) throws IOException
    {
        try
        {
            ErrorResponse payload = new ErrorResponse(message);
            Response error = new Response(status, payload);
            synchronized (outgoingData)
            {
                outgoingData.writeObject(error);
                outgoingData.flush();
            }
            logger.log(
                "Sent error response: " + status + ", message: " + message);
        }
        catch (IOException e)
        {
            logger.log("Failed to send error response: " + e.getMessage());
            throw e;
        }
    }

    private void handleRequest(ObjectInputStream incomingData,
        ObjectOutputStream outgoingData)
        throws IOException, ClassNotFoundException, SQLException
    {
        Request request = (Request) incomingData.readObject();

        logger.log(
            "Incoming request: " + request.handler() + "/" + request.action()
                + ". Body: " + request.payload());

        RequestHandler handler = switch (request.handler())
        {
            case "auth" -> serviceProvider.getAuthenticationRequestHandler();
            case "reservation" -> serviceProvider.getReservationRequestHandler();
            case "dining" ->serviceProvider.getDiningRequestHandler();
            case "person" -> serviceProvider.getPeopleRequestHandler();
            case "room" -> serviceProvider.getRoomRequestHandler();
            default -> throw new IllegalStateException(
                "Unexpected value: " + request.handler());
        };

        Object result = handler.handle(request.action(), request.payload());
        Response response = new Response("SUCCESS", result);
        synchronized (outgoingData)
        {
            outgoingData.writeObject(response);
            outgoingData.flush();
        }
        logger.log("Sent SUCCESS response for " + request.handler() + "/"
            + request.action());
    }

    @Override public void propertyChange(PropertyChangeEvent evt)
    {
        try {
            String entityType = (String) evt.getNewValue();
            logger.log("MainSocketHandler sending UPDATE for: " + entityType);

            Response updateResponse = new Response("UPDATE", entityType);
            synchronized (outgoingData) {
                outgoingData.writeObject(updateResponse);
                outgoingData.flush();
            }
            sendNotification("New update in " + entityType);
        } catch (IOException e) {
            logger.log("Failed to send update/notification to client: " + e.getMessage());
        }
    }

    private void sendNotification(String message)
    {
        try {
            Response notificationResponse = new Response("notification", message);
            synchronized (outgoingData) {
                outgoingData.writeObject(notificationResponse);
                outgoingData.flush();
            }
            logger.log("Sent notification: " + message);
        } catch (IOException e) {
            logger.log("Failed to send notification: " + e.getMessage());
        }
    }
}