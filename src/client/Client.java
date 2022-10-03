package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import static client.GameLogic.getRandomFreePosition;

public class Client {



//Player player = new Player()

  public Client() {
    try {
      Socket connectionSocket = new Socket("localhost", 6789);
      CommunicationService commService = new CommunicationService(connectionSocket);
      commService.start();
      Scanner scanner = new Scanner(System.in);


      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      // Pair p=getRandomFreePosition();
      System.out.println("Indtast spillernavn");
      String navn = inFromUser.readLine();
      // Player me = new Player(navn,p,"up");

      // Torben siger vi skal lave spilleren på serveren.


      boolean running = true;
      while (running) {
        String input = scanner.nextLine();

        if (input.equals("close")) {
          commService.setRunning(false);
          running = false;
        } else {
          commService.sendMessage(input);
          commService.sendPlayer(me);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    Client client = new Client();
  }

}
