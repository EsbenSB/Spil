package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Test {
  public int x;
  public int y;
  public Test(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Test test = (Test) o;
    return x == test.x && y == test.y;
  }

  public static void main(String[] args) {
    // TEST SOMETHING
    ArrayList<Test> playerPoss = new ArrayList<>();
    playerPoss.add(new Test(10, 10));
    playerPoss.add(new Test(5, 5));
    playerPoss.add(new Test(6, 15));

    boolean hit = false;
    Test pos = new Test(2, 5);
    while (pos.x != 7 && !playerPoss.contains(pos)) {
      pos = new Test(pos.x + 1, pos.y);

      System.out.printf("%d : %d%n", pos.x, pos.y);
    }
  }
}
