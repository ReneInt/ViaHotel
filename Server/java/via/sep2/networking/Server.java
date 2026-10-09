package via.sep2.networking;

import via.sep2.startup.ServiceProvider;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private final static int PORT = 6032;
    private final ServiceProvider serviceProvider;

    public Server(ServiceProvider serviceProvider)
    {
        this.serviceProvider = serviceProvider;
    }

    public void start() throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started, listening for connections...");
        while (true)
        {
            Socket socket = serverSocket.accept();
            MainSocketHandler socketHandler = new MainSocketHandler(socket,
                serviceProvider);
            Thread socketThread = new Thread(socketHandler);
            socketThread.start();
        }
    }
}