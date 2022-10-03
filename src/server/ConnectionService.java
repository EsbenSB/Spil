package server;

import java.io.IOException;
import java.net.Socket;

public class ConnectionService extends Thread {

  private final Server server;
  private boolean running = true;

  public ConnectionService(Server server) {
    this.server = server;
  }

  public void run() {
    while (running) {
      try {
        Socket connectionSocket = server.getSocket().accept();
        server.clientConnected(connectionSocket);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

}
