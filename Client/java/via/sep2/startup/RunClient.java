package via.sep2.startup;
import javafx.application.Application;
import javafx.stage.Stage;
import via.sep2.networking.SocketService;
import via.sep2.data.ClientData;
import via.sep2.ui.popup.MessageType;

import java.io.IOException;

public class RunClient extends Application
{
  private SocketService socketService;
  @Override
  public void start(Stage stage) throws Exception
  {
    // Initialize SocketService
    socketService = new SocketService("localhost", 6032);


    // Initialize ViewHandler
    ViewHandler viewHandler = new ViewHandler(stage);

    ClientData.initialize(socketService);
    // Start ViewHandler
    viewHandler.start();

    // Start SocketService
    try {
      socketService.start();
      Thread thread=new Thread(ClientData::refreshAllData);
      thread.setDaemon(true);
      thread.start();
    } catch (IOException e) {
      ViewHandler.popupMessage(MessageType.ERROR,"Could not connect to the server");
    }
  }

  public static void main(String[] args)
  {
    RunClient.launch(args);
  }
}
