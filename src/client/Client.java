package client;

import server.utils.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Client {
  private final CommunicationService commService;
  private String serverID;
  private String clientID;

  public Client() {
    try {
      Socket connectionSocket = new Socket("localhost", 6389);
      commService = new CommunicationService(this, connectionSocket);
      commService.start();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void processData(HashMap<String, String> data) {
    String task = data.get("task");

    switch (task) {
      case "create_server":
        break;
      case "join_server":
        break;
      case "joined_server":
        break;
      case "start_server":
        break;
      case "move":
        break;
      case "use":
        break;
      case "use_powerup":
        break;
      default:
        break;
    }
  }

  public void commDisconnected() {
    Logger.warn("Server shut down...");
  }
}
