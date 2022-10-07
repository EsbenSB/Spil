package test.thread;

import utils.Config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CustomThread extends Thread {
  private final BufferedReader in;

  public CustomThread(Socket s) throws IOException {
    this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
  }

  public void run() {
    try {
      System.out.println("Thread got " + in.readLine());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws IOException {
    Socket s = new Socket(Config.SERVER_HOST, Config.SERVER_PORT);
    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    DataOutputStream out = new DataOutputStream(s.getOutputStream());
    CustomThread thread = new CustomThread(s);
    thread.start();

    System.out.println("Main got " + in.readLine());
    out.writeBytes("got it\n");
  }
}
