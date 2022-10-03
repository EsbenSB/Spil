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

  private final NetworkUser networkUser;
  private final BufferedReader in;
  private final DataOutputStream out;
  private boolean running = true;

  public CommunicationService(NetworkUser networkUser, Socket connectionSocket) {
    this.networkUser = networkUser;

    try {
      in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      out = new DataOutputStream(connectionSocket.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    while (running) {
      listen();
    }

    close();
  }

  public void close() {
    try {
      in.close();
      out.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendData(HashMap<String, String> data) {
    try{
      String message = PackageService.constructQuery(data);
      out.writeBytes(message + "\n");
    } catch (IOException e){
      e.printStackTrace();
    }

  }

  public void listen(){
    try{
      String message = in.readLine();
      HashMap<String, String> data = PackageService.deconstructQuery(message);
      networkUser.processData(data);
    } catch (IOException e) {
      networkUser.commDisconnected(this);
      running = false;
    }
  }
  public void setRunning(boolean running) {
    this.running = running;
  }

}
