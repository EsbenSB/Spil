package client;

import utils.CommunicationService;
import utils.NetworkUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Client implements NetworkUser {
  String id;



//Player player = new Player()

  public Client() {
    id = null;
    try {
      Socket connectionSocket = new Socket("localhost", 6389);
      CommunicationService commService = new CommunicationService(this, connectionSocket);
      commService.start();
      Scanner scanner = new Scanner(System.in);


      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      //Pair p=getRandomFreePosition();
      System.out.println("Indtast spillernavn");
      //Player me = new Player(navn,null,"up");

      String input = scanner.nextLine();

//      commService.sendData(input);
//      commService.sendPlayer(me);

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
  }

  @Override
  public void commDisconnected(CommunicationService commService) {
    // TODO: Communication was shut down!!!
  }

  public static void main(String[] args) {
    Client client = new Client();
  }

}
