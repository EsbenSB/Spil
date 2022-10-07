package client.updated.network;

import client.updated.game.components.Pair;
import client.updated.game.GameController;
import client.updated.gui.Window;
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
        GameController.createPlayer(data.get("client_id"), data.get("client_name"));

        window.playerJoined();
        break;
      case "left_server":
        window.playerLeft(data.get("client_id"));

        GameController.deletePlayer(data.get("client_id"));
        break;
      case "start_server":
        int[][] mazeGrid = PackageService.deconstructGridData(data);
        GameController.setMazeGrid(mazeGrid);

        window.showGameScreen();
        break;
      case "server_closed":
        stopListening();

        window.showStartScreen();
        break;
      case "move":
        int dirHor = Integer.parseInt(data.get("dir_hor"));
        int dirVer = Integer.parseInt(data.get("dir_ver"));
        window.playerMoved(data.get("client_id"), new Pair<>(dirHor, dirVer), false);
        break;
      case "use":
        window.playerAction(data.get("client_id"), false);
        break;
      case "use_powerup":
        dirHor = Integer.parseInt(data.get("dir_hor"));
        dirVer = Integer.parseInt(data.get("dir_ver"));
        window.playerUsePowerup(data.get("client_id"), new Pair<>(dirHor, dirVer), false);
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
    GameController.createGame(clientID, clientName);
    window.showLobbyScreen(serverName, true);

    startListening();
    return true;
  }

  public String joinServer(String clientName, String serverName) {
    HashMap<String, String> sendData = new HashMap<>();
    sendData.put("task", "join_server");
    sendData.put("client_name", clientName);
    sendData.put("server_name", serverName);

    HashMap<String, String> responseData = commService.sendDataAndWait(sendData);

    if (responseData.get("task").equals("error")) {
      return responseData.get("code");
    }

    clientID = responseData.get("client_id");
    ArrayList<HashMap<String, String>> players = PackageService.deconstructClientsString(responseData.get("clients"));
    GameController.createGame(clientID, clientName, players);
    window.showLobbyScreen(serverName, false);

    startListening();
    return null;
  }

  public void startGame() {
    HashMap<String, String> sendData = new HashMap<>();
    sendData.put("task", "start_server");

    commService.sendData(sendData);
  }

  public void move(Pair<Integer> dir) {
    HashMap<String, String> sendData = new HashMap<>();
    sendData.put("task", "move");
    sendData.put("dir_hor", Integer.toString(dir.x));
    sendData.put("dir_ver", Integer.toString(dir.y));

    commService.sendData(sendData);
  }

  public void action() {
    HashMap<String, String> sendData = new HashMap<>();
    sendData.put("task", "use");

    commService.sendData(sendData);
  }

  public void usePowerup(Pair<Integer> dir) {
    HashMap<String, String> sendData = new HashMap<>();
    sendData.put("task", "use_powerup");
    sendData.put("dir_hor", Integer.toString(dir.x));
    sendData.put("dir_ver", Integer.toString(dir.y));

    commService.sendData(sendData);
  }

  public void startListening() {
    commService.startListening();
  }

  public void stopListening() {
    commService.stopListening();
  }
}
