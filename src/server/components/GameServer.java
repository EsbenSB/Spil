package server.components;

import client.Pair;
import client.Player;
import server.services.CommunicationService;
import utils.ErrorCode;
import utils.Logger;
import utils.PackageService;

import java.util.ArrayList;
import java.util.HashMap;

public class GameServer implements ServerInterface {
  private static int nextServerID = 0;

  private final Server parentServer;
  private final String name;
  private final String serverID;
  private final ArrayList<CommunicationService> connections = new ArrayList<>();
  private final HashMap<String, Player> players = new HashMap<>();
  private final Pair loc = new Pair(0,0);
  private final Pair dir = new Pair(0,0);

  public GameServer(Server parentServer, String name) {
    this.parentServer = parentServer;
    this.name = name;

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
        if (commService.getClientID().equals("0")) {
          MazeGenerator mazeGenerator = new MazeGenerator(10, 10);
          int[][] grid = mazeGenerator.getGrid();

          returnData = PackageService.constructGridData(grid);
          broadcastData(returnData, null);
        } else {
          returnData.put("task", "error");
          returnData.put("code", ErrorCode.NOT_ADMIN);
          commService.sendData(returnData);
        }
        break;
      case "move":
      case "use":
      case "use_powerup":
        broadcastData(data,commService);
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

  public void addConnection(CommunicationService commService) {
    connections.add(commService);

    HashMap<String, String> data = new HashMap<>();
    data.put("task", "joined_server");
    data.put("client_id", commService.getClientID());
    data.put("client_name", commService.getClientName());
    Player player = new Player(commService.getClientName(),loc,dir);
    players.put(commService.getClientID(),player);

    broadcastData(data, commService);

    Logger.info("%s joined game server %s with id %s", commService, name, serverID);
  }

  public void removeConnection(CommunicationService commService) {
    connections.remove(commService);
    Logger.info("%s left game server %s with id %s", commService, name, serverID);

    HashMap<String, String> data = new HashMap<>();
    data.put("task", "left_server");
    data.put("client_id", commService.getClientID());
    broadcastData(data, commService);

    if (connections.isEmpty()) {
      parentServer.removeGameServer(this);
    }
  }

  public String getNextClientID() {
    return Integer.toString(connections.size());
  }

  public String getServerID() {
    return serverID;
  }

  public String getName() {
    return name;
  }
}
