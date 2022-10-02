package server;

import server.components.Server;

public class StartServer {
  public static void main(String[] args) {
    Server server = new Server();
    server.start();
  }
}
