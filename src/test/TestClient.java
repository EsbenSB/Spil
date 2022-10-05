package test;

import utils.Config;
import utils.PackageService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class TestClient {
  public TestClient() throws IOException {
    try (Socket connectionSocket = new Socket(Config.SERVER_HOST, Config.SERVER_PORT)) {
      CommunicationService commService = new CommunicationService(connectionSocket);
      commService.start();

      try (Scanner scanner = new Scanner(System.in)) {
        System.out.println("Create a server with 'create', join a server with 'join' or start the server with 'start'");
        label:
        while (true) {
          String input = scanner.nextLine();
          switch (input) {
            case "close":
              commService.close();
              break label;
            case "create":
              commService.sendQuery("task=create_server&client_name=lukas&server_name=gameServer1");
              break;
            case "join":
              commService.sendQuery("task=join_server&client_name=mads&server_name=gameServer1");
              break;
            case "start":
              commService.sendQuery("task=start_server");
              break;
            default:
              commService.sendQuery(input);
              break;
          }
        }
      }
    }
  }

  private static class CommunicationService extends Thread {
    private final DataOutputStream out;
    private final BufferedReader in;
    private boolean running = true;

    public CommunicationService(Socket connectionSocket) {
      try {
        out = new DataOutputStream(connectionSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
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

      try {
        in.close();
        out.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private void listen() {
      try {
        String query = in.readLine();
        System.out.println("Received query " + query);
        HashMap<String, String> data = PackageService.deconstructQuery(query);
        String task = data.get("task");
        if (task.equals("join_server")) {
          ArrayList<HashMap<String, String>> clients = PackageService.deconstructClientsString(data.get("clients"));
          System.out.println("Connected clients: " + clients);
        } else if (task.equals("start_server")) {
          int[][] grid = PackageService.deconstructGridData(data);
          System.out.println("Grid: " + Arrays.deepToString(grid));
        }
      } catch (IOException e) {
        System.out.println("Connection closed");
        close();
      }
    }

    public void sendQuery(String query) {
      try {
        out.writeBytes(query + "\n");
        System.out.println("Sent query " + query);
      } catch (IOException e) {
        System.out.println("Connection closed");
        close();
      }
    }
  }

  public static void main(String[] args) throws IOException {
    TestClient client = new TestClient();
  }
}
