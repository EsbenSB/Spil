package test;

import java.util.ArrayList;
import java.util.Arrays;

public class Test {
  private String name;

  public Test(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Test{" +
            "name='" + name + '\'' +
            '}';
  }

  public static void main(String[] args) {
    // TEST SOMETHING
    int one = 41;
    int two = 21;
    System.out.println((double) one / two);
  }
}
