package server.services;

import server.components.Server;
import utils.PackageService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class CommunicationService extends Thread {
  private final Server server;
  private final String address;
  private String serverID;
  private String clientID;
  private final BufferedReader in;
  private final DataOutputStream out;
  private boolean running = true;

  public CommunicationService(Server server, Socket connectionSocket) {
    this.server = server;
    this.address = connectionSocket.getRemoteSocketAddress().toString();

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
      server.processData(this, data);
    } catch (IOException e) {
      server.commDisconnected(this);
      running = false;
    }
  }
  public void setRunning(boolean running) {
    this.running = running;
  }

  public String getServerID() {
    return serverID;
  }

  public void setServerID(String serverID) {
    this.serverID = serverID;
  }

  public String getClientID() {
    return clientID;
  }

  public void setClientID(String clientID) {
    this.clientID = clientID;
  }

  public String getAddress() {
    return address;
  }
}
