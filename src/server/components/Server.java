package server.components;

import utils.Config;
import utils.Logger;
import utils.PackageService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
  private ServerSocket serverSocket;
  private final HashMap<String, GameServer> gameServers = new HashMap<>();
  private final AtomicBoolean running = new AtomicBoolean(false);

  public void close() {
    running.set(false);
    try {
      serverSocket.close();
    } catch (IOException e) {
      Logger.error("Server could not close...");
      Logger.error("Message: %s", e.getMessage());
    }
  }

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
    running.set(true);
    while (running.get()) {
      Socket connectionSocket = listenForConnections();
      if (connectionSocket == null) break;

      clientConnected(connectionSocket);
    }
    close();
  }

  private Socket listenForConnections() {
    try {
      return serverSocket.accept();
    } catch (IOException e) {
      Logger.error("Stopped listening for connections. Process interrupted.");
      return null;
    }
  }

  private void clientConnected(Socket connectionSocket) {
    Logger.info("%s connected", connectionSocket.getRemoteSocketAddress());
  }

  public void receivedQuery(String query) {
    HashMap<String, String> data = PackageService.deconstructQuery(query);
  }
}
