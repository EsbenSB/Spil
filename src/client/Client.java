package client;

import utils.CommunicationService;
import utils.NetworkUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import client.GameLogic;

public class Client implements NetworkUser {
  String id;
  Scanner scanner = new Scanner(System.in);
  String serverId;



//Player player = new Player()

  public Client() {
    id = null;
    try {
      Socket connectionSocket = new Socket("localhost", 6389);
      CommunicationService commService = new CommunicationService(this, connectionSocket);
      commService.start();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  @Override
  public void processData(HashMap<String, String> data) {
    // TODO: Process the data!!!
    if(data.get("task").equals("init")){
      id = data.get("client_id");
      System.out.println("What is your name?");
      String name = scanner.nextLine();
      GameLogic.me.setName(name);
    }

    if(data.get("task").equals("create_server")){
      serverId = data.get("server_id");
    }

  }

  @Override
  public void commDisconnected(CommunicationService commService) {
    // TODO: Communication was shut down!!!
  }

  public static void main(String[] args) {
    Client client = new Client();
  }

}
