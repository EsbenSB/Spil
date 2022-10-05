package test.thread;

import utils.Config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class InterruptSocketIO extends Thread {
  private final BufferedReader in;
  private final DataOutputStream out;

  private boolean running;
  private boolean listening;

  public InterruptSocketIO() {
    this.running = true;
    this.listening = false;

    try {
      Socket connectionSocket = new Socket(Config.SERVER_HOST, Config.SERVER_PORT);
      System.out.println("connected");
      connectionSocket.setSoTimeout(500);
      in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      out = new DataOutputStream(connectionSocket.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    while (running) {
      while (listening) {
        try {
          String input = in.readLine();
          System.out.println(input);

          out.writeBytes(input + "\n");
        } catch (SocketTimeoutException e) {
          System.out.println("Read timed out");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
    System.out.println("stopped running");
  }

  public void startListening() {
    System.out.println("started listening");
    listening = true;
  }

  public void stopListening() {
    listening = false;
    System.out.println("stopped listening");
  }

  public void stopRunning() {
    System.out.println("trying to stop running running");
    if (listening) {
      stopListening();
    }

    running = false;
  }

  public static void main(String[] args) throws InterruptedException {
    InterruptSocketIO thread = new InterruptSocketIO();
    thread.start();
    thread.startListening();
    Thread.sleep(1000);
    thread.stopListening();
    Thread.sleep(1000);
    thread.startListening();
    Thread.sleep(5000);
    thread.stopRunning();
  }
}
