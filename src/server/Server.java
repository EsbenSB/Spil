package server;

import client.Player;
import utils.CommunicationService;
import utils.NetworkUser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server implements NetworkUser {

  private final ServerSocket serverSocket;
  private final ArrayList<CommunicationService> connections = new ArrayList<>();
  private final ArrayList<Player> players = new ArrayList<>();
  private int id = 1;
  String message;

  public Server() {
    try {
      serverSocket = new ServerSocket(6389);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    ConnectionService connectionService = new ConnectionService(this);
    connectionService.start();


    boolean running = true;
    while (running) {
     // System.out.println("What is your name?");
    }
  }

  public void addPlayers(Player player) {
    players.add(player);
  }

  public ServerSocket getSocket() {
    return serverSocket;
  }

  public void clientConnected(Socket connectionSocket) {
    CommunicationService commService = new CommunicationService(this, connectionSocket);
    commService.start();
    connections.add(commService);
    System.out.println("Client connected");

    HashMap<String, String> data = new HashMap<>();
    data.put("task","init");
    data.put("client_id",Integer.toString(id));
    id++;
    commService.sendData(data);
  }

  @Override
  public void processData(HashMap<String, String> data) {
    if(data.get("task").equals("init")){
      String client_name = data.get("client_name");

      // TODO: Save the clients name
    }
  }

  @Override
  public void commDisconnected(CommunicationService commService) {
    connections.remove(commService);
    System.out.println("Client disconnected");
  }

  public static void main(String[] args) {
    Server server = new Server();
  }

}
