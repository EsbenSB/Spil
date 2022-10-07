package test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

public class RunLater {
  public static void runLater(int delay, Supplier<Void> callback) {
    Timer timer = new Timer();
    timer.schedule(
            new TimerTask() {
              @Override
              public void run() {
                callback.get();
                timer.cancel();
              }
            }, delay);
  }

  public static void main(String[] args) {
    System.out.println("I should run now...");
    runLater(1000, () -> {
      System.out.println("I am running now!!");
      return null;
    });
  }
}
