package test.thread;

public class CustomThread extends Thread {
  private boolean running;
  private boolean listening;

  public CustomThread() {
    this.running = false;
    this.listening = false;
  }

  public void run() {
    while (running) {
      int count = 0;
      while (listening) {
        if (count % 10000000 == 0) {
          System.out.println(count);
        }
        count++;
      }
    }
  }

  public void startRunning() {
    System.out.println("Started running");

    running = true;
    start();
  }

  public void stopRunning() {
    running = false;

    System.out.println("Stopped running");
  }

  public void startListening() {
    System.out.println("Started listening");

    listening = true;
  }

  public void stopListening() {
    listening = false;

    System.out.println("Stopped listening");
  }

  public static void main(String[] args) throws InterruptedException {
    CustomThread thread = new CustomThread();
    thread.startRunning();
    thread.startListening();

    Thread.sleep(1000);

    thread.stopListening();

    Thread.sleep(1000);

    thread.startListening();

    Thread.sleep(1000);

    thread.stopListening();
    thread.stopRunning();
  }
}
