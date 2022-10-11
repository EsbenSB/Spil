package client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class NetworkListener extends Thread {
  private final CommunicationService commService;
  private final BufferedReader in;

  private boolean running;

  public NetworkListener(CommunicationService commService, BufferedReader in) {
    this.commService = commService;
    this.in = in;
    this.running = true;
  }

  public void run() {
    while (running) {
      try {
        String query = in.readLine();
        commService.processQuery(query);
      } catch (SocketTimeoutException e) {
        // Basically do nothing as we just want to check if we are still running
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    commService.setListening(false);
  }

  public void stopRunning() {
    running = false;
  }
}
