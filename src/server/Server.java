package server;

import client.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

  private final ServerSocket serverSocket;
  private final ArrayList<CommunicationService> connections = new ArrayList<>();
  private final ArrayList<Player> players = new ArrayList<>();

  public Server() {
    try {
      serverSocket = new ServerSocket(6789);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    ConnectionService connectionService = new ConnectionService(this);
    connectionService.start();
    Scanner scanner = new Scanner(System.in);

    boolean running = true;
    while (running) {
      String input = scanner.nextLine();

      if (input.equals("close")) {
        connectionService.setRunning(false);
        connections.forEach(commService -> commService.setRunning(false));
        running = false;
      }
    }
  }

  public ServerSocket getSocket() {
    return serverSocket;
  }

  public void clientConnected(Socket connectionSocket) {
    CommunicationService commService = new CommunicationService(this, connectionSocket);
    commService.start();
    connections.add(commService);
    //players.add()
    System.out.println("Client connected");
  }

  public void clientDisconnected(CommunicationService commService) {
    connections.remove(commService);
    System.out.println("Client disconnected");
  }

  public static void main(String[] args) {
    Server server = new Server();
  }

}
