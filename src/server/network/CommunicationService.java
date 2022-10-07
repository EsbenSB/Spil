package server.network;

import server.utils.Logger;
import utils.PackageService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class CommunicationService extends Thread {
  private ServerInterface server;
  private final String address;
  private String clientName = "";
  private String clientID = "";
  private final BufferedReader in;
  private final DataOutputStream out;
  private boolean running = true;

  public CommunicationService(ServerInterface server, Socket connectionSocket) {
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
  }

  public void close() {
    running = false;
    server.commDisconnected(this);

    try {
      in.close();
      out.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized void sendData(HashMap<String, String> data) {
    try{
      String query = PackageService.constructQuery(data);
      out.writeBytes(query + "\n");

      Logger.info("Sent query %s to %s", query, this);
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  public void listen(){
    try{
      String query = in.readLine();

      if (query == null) {
        throw new IOException();
      }

      Logger.info("Received query %s from %s", query, this);

      HashMap<String, String> data = PackageService.deconstructQuery(query);
      server.processData(this, data);


    } catch (IOException e) {
      close();
    }
  }

  public void setServer(ServerInterface server) {
    this.server = server;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getClientID() {
    return clientID;
  }

  public void setClientID(String clientID) {
    this.clientID = clientID;
  }

  @Override
  public String toString() {
    return clientName.isBlank() ? address : clientName;
  }
}
