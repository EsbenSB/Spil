package utils;

import client.Pair;
import client.Player;
import server.Server;
import utils.PackageService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

import static client.GameLogic.getRandomFreePosition;
import static client.GameLogic.me;

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

  public void sendData(HashMap<String, String> data) {
    try{
      String message = PackageService.constructQuery(data);
      outToClient.writeBytes(message + "\n");
    } catch (IOException e){
      e.printStackTrace();
    }

  }

  public void listen(){
    try{
      String message = inFromClient.readLine();
      HashMap<String, String> data = PackageService.deconstructQuery(message);
      server.processData(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void run() {

    while (running) {
      try {

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
  public void
  public void setRunning(boolean running) {
    this.running = running;
  }

}
