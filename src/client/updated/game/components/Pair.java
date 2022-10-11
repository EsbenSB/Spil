package client.updated.game.components;

import java.util.Objects;

public class Pair<T> {
  public T x;
  public T y;

  public Pair(T x, T y) {
    this.x = x;
    this.y = y;
  }

  public Pair<Integer> add(Pair<Integer> pair) {
    return new Pair<>((Integer)x + pair.x, (Integer)y + pair.y);
  }

  public Pair<Integer> subtract(Pair<Integer> pair) {
    return new Pair<>((Integer)x - pair.x, (Integer)y - pair.y);
  }

  public Pair<Integer> multiply(int multiple) {
    return new Pair<>((Integer)x * multiple, (Integer)y * multiple);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pair<?> pair = (Pair<?>) o;
    return Objects.equals(x, pair.x) && Objects.equals(y, pair.y);
  }

  @Override
  public String toString() {
    return "Pair{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}
