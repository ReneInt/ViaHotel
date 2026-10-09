package via.sep2.startup;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import via.sep2.networking.Server;
import via.sep2.startup.ServiceProvider;
public class RunServer
{

    public static void main(String[] args) throws IOException{
        ServiceProvider serviceLocator = new ServiceProvider();
        Server server = new Server(serviceLocator);
        server.start();
    }
}
