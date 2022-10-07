package server.network;

import server.components.MazeGenerator;
import utils.ErrorCode;
import server.utils.Logger;
import utils.PackageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameServer implements ServerInterface {
  private static int nextServerID = 0;

  private final Server parentServer;
  private final String name;
  private final String serverID;
  private final ArrayList<CommunicationService> connections = new ArrayList<>();
  private final ArrayList<String> availableIDs = new ArrayList<>(Arrays.asList("1", "2", "3"));

  private boolean started;

  public GameServer(Server parentServer, String name) {
    this.parentServer = parentServer;
    this.name = name;
    this.started = false;

    this.serverID = Integer.toString(nextServerID);
    nextServerID++;
  }

  @Override
  public void processData(CommunicationService commService, HashMap<String, String> data) {
    String task = data.get("task");
    HashMap<String, String> returnData = new HashMap<>();

    switch (task) {
      case "start_server":
        // Client med id=0 er admin/opretter af serveren
        if (!commService.getClientID().equals("0")) {
          returnData.put("task", "error");
          returnData.put("code", ErrorCode.NOT_ADMIN);
          commService.sendData(returnData);
          break;
        }

        MazeGenerator mazeGenerator = new MazeGenerator(15, 10);
        int[][] grid = mazeGenerator.getGrid();

        returnData = PackageService.constructGridData(grid);
        broadcastData(returnData, null);

        started = true;
        break;
      case "move":
        broadcastData(data,commService);
      case "use":
        broadcastData(data,commService);
      case "use_powerup":
        data.put("client_id", commService.getClientID());
        broadcastData(data, commService);
    }
  }

  @Override
  public void commDisconnected(CommunicationService commService) {
    removeConnection(commService);
  }

  private void broadcastData(HashMap<String, String> data, CommunicationService sender) {
    for (CommunicationService commService : connections) {
      if (commService != sender) {
        commService.sendData(data);
      }
    }
  }

  public ArrayList<CommunicationService> getConnections() {
    return new ArrayList<>(connections);
  }

  public ArrayList<CommunicationService> getConnections(CommunicationService commService) {
    ArrayList<CommunicationService> tempConnections = new ArrayList<>();

    for (CommunicationService connection : connections) {
      if (!connection.equals(commService)) {
        tempConnections.add(connection);
      }
    }

    return tempConnections;
  }

  public void addConnection(CommunicationService commService) {
    connections.add(commService);

    HashMap<String, String> data = new HashMap<>();
    data.put("task", "joined_server");
    data.put("client_id", commService.getClientID());
    data.put("client_name", commService.getClientName());

    broadcastData(data, commService);

    Logger.info("%s joined game server %s with id %s", commService, name, serverID);
  }

  public void removeConnection(CommunicationService commService) {
    connections.remove(commService);
    Logger.info("%s left game server %s with id %s", commService, name, serverID);

    if (commService.getClientID().equals("0")) {
      HashMap<String, String> data = new HashMap<>();
      data.put("task", "server_closed");
      broadcastData(data, commService);

      for (CommunicationService connection : connections) {
        connection.setServer(parentServer);
      }

      parentServer.removeGameServer(this);
      return;
    }

    availableIDs.add(commService.getClientID());
    availableIDs.sort(String::compareTo);

    HashMap<String, String> data = new HashMap<>();
    data.put("task", "left_server");
    data.put("client_id", commService.getClientID());
    broadcastData(data, commService);

    if (connections.isEmpty()) {
      parentServer.removeGameServer(this);
    }
  }

  public String getNextClientID() {
    return availableIDs.remove(0);
  }
  public String getServerID() {
    return serverID;
  }
  public String getName() {
    return name;
  }
  public boolean isStarted() {
    return started;
  }
}
