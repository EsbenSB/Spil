package client;

import utils.CommunicationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import static client.GameLogic.getRandomFreePosition;

public class Client {
  String id;



//Player player = new Player()

  public Client() {
    id = null;
    try {
      Socket connectionSocket = new Socket("localhost", 6389);
      CommunicationService commService = new CommunicationService(connectionSocket);
      commService.start();
      Scanner scanner = new Scanner(System.in);


      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      //Pair p=getRandomFreePosition();
      System.out.println("Indtast spillernavn");
      //Player me = new Player(navn,null,"up");

      String input = scanner.nextLine();

          commService.sendMessage(input);
          //commService.sendPlayer(me);

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

  public void processData(HashMap<String, String> data){
    if(data.get("task").equals("init")){

    }
  }

  public static void main(String[] args) {
    Client client = new Client();
  }

}
