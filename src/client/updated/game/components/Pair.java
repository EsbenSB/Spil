package client.updated.game.components;

public class Pair<T> {
  public T x;
  public T y;

  public Pair(T x, T y) {
    this.x = x;
    this.y = y;
  }

  public boolean equals(Pair<T> pair) {
    return x == pair.x && y == pair.y;
  }

  public Pair<Integer> add(Pair<Integer> pair) {
    return new Pair<>((Integer)x + pair.x, (Integer)y + pair.y);
  }

  @Override
  public String toString() {
    return "Pair{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}
