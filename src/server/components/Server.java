package server.components;

import server.services.CommunicationService;
import utils.Config;
import utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
  private ServerSocket serverSocket;
  private final HashMap<String, GameServer> gameServers = new HashMap<>();
  private final ArrayList<CommunicationService> connections = new ArrayList<>();
  private boolean running = true;

  public void start() {
    try {
      serverSocket = new ServerSocket(Config.SERVER_PORT);
      Logger.info("Server running on %s:%d", Config.SERVER_HOST, Config.SERVER_PORT);
    } catch (IOException e) {
      Logger.error("Server could not start. This is likely due to the port %d being in use.", Config.SERVER_PORT);
      return;
    }

    run();
  }

  private void run() {
    while (running) {
      listenForConnections();
    }

    close();
  }

  public void close() {
    running = false;
    try {
      serverSocket.close();
    } catch (IOException e) {
      Logger.error("Server could not close...");
      Logger.error("Message: %s", e.getMessage());
    }
  }

  private void listenForConnections() {
    try {
      Socket connectionSocket = serverSocket.accept();
      clientConnected(connectionSocket);
    } catch (IOException e) {
      Logger.error("Stopped listening for connections. Process interrupted.");
      running = false;
    }
  }

  private void clientConnected(Socket connectionSocket) {
    CommunicationService commService = new CommunicationService(this, connectionSocket);
    commService.start();

    Logger.info("%s connected", connectionSocket.getRemoteSocketAddress());
  }

  public void processData(CommunicationService commService, HashMap<String, String> data) {
    String task = data.get("task");

    switch (task) {
      case "create_server":
        // Tjek om server_name er ledigt,
        // hvis ja, så opret en GameServer og player,
        // sæt comm servicens serverID og clientID,
        // send pakke tilbage med client_id og server_id,
        // hvis nej, så send pakke tilbage med client_id=null og server_id=null
        break;
      case "join_server":
        // Tjek om game server med server_name findes,
        // hvis ja, så opret player og sæt comm servicens serverID og clientID
        // send pakke tilbage med server_id og client_id
        // derefter send til alle clients i game serveren task=joined_server
        // hvis nej, så send pakke tilabe med client_id=null og server_id=null
        break;
      case "start_server":
        // Tjek om client_id er admin af serveren med server_id
        // hvis ja, så generere maze og
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

  public void commDisconnected(CommunicationService commService) {
    connections.remove(commService);
    Logger.info("Connection to %s cut off...", commService.getAddress());
  }

  private boolean gameServerExists(String name) {
    for (GameServer gameServer : gameServers.values()) {
      if (gameServer.getName().equals(name)) {
        return true;
      }
    }

    return false;
  }
}
