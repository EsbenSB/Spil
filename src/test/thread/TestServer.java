package test.thread;

import utils.Config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
  public static void main(String[] args) throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(Config.SERVER_PORT)) {
      try (Socket connectionSocket = serverSocket.accept()) {
        System.out.println("connected");

        BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());

        Thread.sleep(1100);
        out.writeBytes("Hello\n");
        System.out.println("sent message");

        String input = in.readLine();
        System.out.println(input);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
