package server;

import server.network.Server;

public class StartServer {
  public static void main(String[] args) {
    Server server = new Server();
    server.start();
  }
}
