package client.game.components;

public class Player implements Comparable<Player> {
  private final String ID;
  private final String name;

  private Pair<Integer> pos;
  private Pair<Integer> dir;
  private String effect;
  private int score;
  private int item;
  private int speed;
  private boolean finished;

  public Player(String ID, String name) {
    this.ID = ID;
    this.name = name;
    this.score = 0;

    init();
  }

  public void init() {
    this.pos = new Pair<>(1, 1);
    this.dir = new Pair<>(0, 1);
    this.effect = "0";
    this.finished = false;
    this.item = -1;
    this.speed = 1;
  }

  public String getID() {
    return ID;
  }
  public Pair<Integer> getPos() {
    return pos;
  }
  public void setPos(Pair<Integer> pos) {
    this.pos = pos;
  }
  public Pair<Integer> getDir() {
    return dir;
  }
  public void setDir(Pair<Integer> dir) {
    this.dir = dir;
  }
  public String getEffect() {
    return effect;
  }
  public void setEffect(String effect) {
    this.effect = effect;
  }
  public int getScore() {
    return score;
  }
  public void addScore(int score) {
    this.score += score;
  }
  public String getImageName() {
    return String.format("1_%d_%d_%s_%s.jpeg", dir.x, dir.y, effect, ID);
  }
  public boolean isFinished() {
    return finished;
  }
  public void setFinished(boolean finished) {
    this.finished = finished;
  }
  public int getSpeed() {
    return speed;
  }
  public void setSpeed(int speed) {
    this.speed = speed;
  }
  public void setItem(int item) {
    this.item = item;
  }
  public int getItem() {
    return item;
  }

  @Override
  public int compareTo(Player o) {
    return ID.compareTo(o.getID());
  }

  @Override
  public String toString() {
    return name;
  }
}
