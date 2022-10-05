package client.updated;

import utils.Config;
import utils.PackageService;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class NetworkClient {
  private final Window window;
  private final CommunicationService commService;

  private String clientID;

  public NetworkClient(Window window) {
    this.window = window;

    try {
      Socket connectionSocket = new Socket(Config.SERVER_HOST, Config.SERVER_PORT);

      commService = new CommunicationService(this, connectionSocket);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void processData(HashMap<String, String> data) {
    String task = data.get("task");

    switch (task) {
      case "joined_server":
        String clientID = data.get("client_id");
        String clientName = data.get("client_name");

        window.playerJoined(clientID, clientName);
        break;
      case "start_server":
        int[][] mazeGrid = PackageService.deconstructGridData(data);

        window.showGameScreenLater(mazeGrid);
        break;
      case "move":
        break;
      case "use":
        break;
      case "use_powerup":
        break;
    }
  }

  public boolean createServer(String clientName, String serverName) {
    HashMap<String, String> sendData = new HashMap<>();
    sendData.put("task", "create_server");
    sendData.put("client_name", clientName);
    sendData.put("server_name", serverName);

    HashMap<String, String> responseData = commService.sendDataAndWait(sendData);

    if (responseData.get("task").equals("error")) {
      return false;
    }

    clientID = responseData.get("client_id");
    window.createGame(clientID, clientName);

    startListening();
    return true;
  }

  public ArrayList<HashMap<String, String>> joinServer(String clientName, String serverName) {
    HashMap<String, String> sendData = new HashMap<>();
    sendData.put("task", "join_server");
    sendData.put("client_name", clientName);
    sendData.put("server_name", serverName);

    HashMap<String, String> responseData = commService.sendDataAndWait(sendData);

    if (responseData.get("task").equals("error")) {
      ArrayList<HashMap<String, String>> returnData = new ArrayList<>();
      HashMap<String, String> returnMap = new HashMap<>();
      returnMap.put("error", responseData.get("code"));
      returnData.add(returnMap);
      return returnData;
    }

    clientID = responseData.get("client_id");
    window.createGame(clientID, clientName);

    startListening();
    return PackageService.deconstructClientsString(responseData.get("clients"));
  }

  public int[][] startGame() {
    stopListening();

    HashMap<String, String> sendData = new HashMap<>();
    sendData.put("task", "start_server");

    HashMap<String, String> responseData = commService.sendDataAndWait(sendData);

    if (responseData.get("task").equals("error")) {
      return null;
    }

    return PackageService.deconstructGridData(responseData);
  }

  public void startListening() {
    commService.startListening();
  }

  public void stopListening() {
    commService.stopListening();
  }
}
