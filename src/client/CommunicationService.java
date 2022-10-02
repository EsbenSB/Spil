package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommunicationService extends Thread {

  private final Socket connectionSocket;
  private final BufferedReader inFromServer;
  private final DataOutputStream outToServer;
  private boolean running = true;

  public CommunicationService(Socket connectionSocket) {
    this.connectionSocket = connectionSocket;

    try {
      inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      outToServer = new DataOutputStream(connectionSocket.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    while (running) {
      try {
        String fromServer = inFromServer.readLine();
        System.out.println(fromServer);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    try {
      inFromServer.close();
      outToServer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendMessage(String message) {
    try {
      outToServer.writeBytes(message + "\n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

}
