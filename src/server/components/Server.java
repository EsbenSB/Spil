package server.components;

import server.services.CommunicationService;
import utils.Config;
import utils.ErrorCode;
import utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements ServerInterface {
  private ServerSocket serverSocket;
  private final HashMap<String, GameServer> gameServers = new HashMap<>();
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

    Logger.info("%s connected", commService);
  }

  @Override
  public void processData(CommunicationService commService, HashMap<String, String> data) {
    String task = data.get("task");
    String clientName = data.get("client_name");
    String serverName = data.get("server_name");
    HashMap<String, String> returnData = new HashMap<>();
    returnData.put("task", task);

    switch (task) {
      case "create_server":
        if (getGameServerByName(serverName) == null) {
          GameServer gameServer = createGameServer(serverName);
          commService.setClientName(clientName);
          commService.setClientID("0");
          transferCommService(commService, gameServer);

          returnData.put("client_id", commService.getClientID());
          break;
        }
      case "join_server":
        GameServer gameServer = getGameServerByName(serverName);
        if (gameServer != null) {
          commService.setClientName(clientName);
          commService.setClientID(gameServer.getNextClientID());
          transferCommService(commService, gameServer);

          returnData.put("client_id", commService.getClientID());
          break;
        }
      default:
        returnData.put("task", "error");
        returnData.put("code", ErrorCode.NOT_FOUND);
    }

    commService.sendData(returnData);
  }

  @Override
  public void commDisconnected(CommunicationService commService) {
    Logger.info("Connection to %s cut off...", commService);
  }

  private GameServer getGameServerByName(String name) {
    for (GameServer gameServer : gameServers.values()) {
      if (gameServer.getName().equals(name)) {
        return gameServer;
      }
    }

    return null;
  }

  private GameServer createGameServer(String name) {
    GameServer gameServer = new GameServer(this, name);
    Logger.info("Created game server %s with id %s", name, gameServer.getServerID());
    return gameServer;
  }

  public void removeGameServer(GameServer gameServer) {
    gameServers.remove(gameServer.getServerID());

    Logger.info("Shutdown game server %s with id %s", gameServer.getName(), gameServer.getServerID());
  }

  private void transferCommService(CommunicationService commService, GameServer gameServer) {
    gameServer.addConnection(commService);
    commService.setServer(gameServer);
  }
}
