package client.network;

import utils.PackageService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class CommunicationService {
  private final NetworkClient networkClient;
  private final Socket connectionSocket;
  private final BufferedReader in;
  private final DataOutputStream out;

  private NetworkListener listener;
  private volatile boolean listening;

  public CommunicationService(NetworkClient networkClient, Socket connectionSocket) {
    this.networkClient = networkClient;
    this.connectionSocket = connectionSocket;
    this.listening = false;

    try {
      in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      out = new DataOutputStream(connectionSocket.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void close() {
    if (listening) {
      stopListening();
    }

    try {
      in.close();
      out.close();
      connectionSocket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void processQuery(String query) {
    HashMap<String, String> data = PackageService.deconstructQuery(query);
    networkClient.processData(data);
  }

  public void sendData(HashMap<String, String> data) {
    String query = PackageService.constructQuery(data);

    try {
      out.writeBytes(query + "\n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private HashMap<String, String> waitForData() {
    try {
      String query = in.readLine();

      return PackageService.deconstructQuery(query);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public HashMap<String, String> sendDataAndWait(HashMap<String, String> data) {
    sendData(data);
    return waitForData();
  }

  public void startListening() {
    try {
      connectionSocket.setSoTimeout(1000);
    } catch (SocketException e) {
      throw new RuntimeException(e);
    }

    listener = new NetworkListener(this, in);
    listener.start();
    listening = true;
  }

  public void stopListening() {
    if (!listening) return;

    listener.stopRunning();
  }

  public void setListening(boolean listening) {
    this.listening = listening;

    try {
      connectionSocket.setSoTimeout(0);
    } catch (SocketException e) {
      throw new RuntimeException(e);
    }
  }
}
