package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommunicationService extends Thread {

  private final Server server;
  private final Socket connectionSocket;
  private final BufferedReader inFromClient;
  private final DataOutputStream outToClient;
  private boolean running = true;

  public CommunicationService(Server server, Socket connectionSocket) {
    this.server = server;
    this.connectionSocket = connectionSocket;
    try {
      inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      outToClient = new DataOutputStream(connectionSocket.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    while (running) {
      try {
        String fromClient = inFromClient.readLine();
        System.out.println(fromClient);
        outToClient.writeBytes("Recieved message.\n");
      } catch (IOException e) {
        server.clientDisconnected(this);
        running = false;
      }
    }

    try {
      inFromClient.close();
      outToClient.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

}
