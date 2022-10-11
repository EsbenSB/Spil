package server.network;

import utils.Config;
import utils.ErrorCode;
import server.utils.Logger;
import utils.PackageService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements ServerInterface {
  private ServerSocket serverSocket;
  private final HashMap<String, GameServer> gameServers = new HashMap<>();
  private boolean running = true;
  private int nextTestServerID = 0;

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

    GameServer gameServer;

    switch (task) {
      case "create_server":
        if (gameServers.get(serverName) != null) {
          returnData.put("task", "error");
          returnData.put("code", ErrorCode.NOT_AVAILABLE);
          break;
        }

        gameServer = createGameServer(serverName);
        commService.setClientName(clientName);
        commService.setClientID("0");
        transferCommService(commService, gameServer);

        returnData.put("client_id", commService.getClientID());
        break;
      case "join_server":
        gameServer = gameServers.get(serverName);

        if (gameServer == null) {
          returnData.put("task", "error");
          returnData.put("code", ErrorCode.NOT_FOUND);
          break;
        }

        if (gameServer.getConnections().size() == 4) {
          returnData.put("task", "error");
          returnData.put("code", ErrorCode.SERVER_FULL);
          break;
        }

        if (gameServer.isStarted()) {
          returnData.put("task", "error");
          returnData.put("code", ErrorCode.SERVER_STARTED);
          break;
        }

        commService.setClientName(clientName);
        commService.setClientID(gameServer.getNextClientID());
        transferCommService(commService, gameServer);

        returnData.putAll(PackageService.constructClientsData(commService.getClientID(),
                gameServer.getConnections(commService)));
        break;
      case "test":
        String testServerName = "test_server_" + nextTestServerID++;
        gameServer = createGameServer(testServerName);
        commService.setClientName(clientName);
        commService.setClientID("0");
        transferCommService(commService, gameServer);

        returnData.put("client_id", commService.getClientID());
        returnData.put("server_name", testServerName);
    }

    commService.sendData(returnData);
  }

  @Override
  public void commDisconnected(CommunicationService commService) {
    Logger.info("Connection to %s cut off...", commService);
  }

  private GameServer createGameServer(String name) {
    GameServer gameServer = new GameServer(this, name);
    gameServers.put(name, gameServer);
    Logger.info("Created game server %s with id %s", name, gameServer.getServerID());
    return gameServer;
  }

  public void removeGameServer(GameServer gameServer) {
    gameServers.remove(gameServer.getName());

    Logger.info("Shutdown game server %s with id %s", gameServer.getName(), gameServer.getServerID());
  }

  private void transferCommService(CommunicationService commService, GameServer gameServer) {
    gameServer.addConnection(commService);
    commService.setServer(gameServer);
  }
}
