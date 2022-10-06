package test.thread;

import utils.Config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class CustomServerThread {
  public static void main(String[] args) throws IOException, InterruptedException {
    ServerSocket ss = new ServerSocket(Config.SERVER_PORT);
    Socket s = ss.accept();
    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    DataOutputStream out = new DataOutputStream(s.getOutputStream());

    Thread.sleep(1000);

    out.writeBytes("hello\n");
    System.out.println(in.readLine());
  }
}
