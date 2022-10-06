package client.updated;

import java.util.ArrayList;

public class Game {
  private final Player me;
  private final ArrayList<Player> players;

  private int[][] mazeGrid;
  private int finishes;

  public Game(String playerID, String playerName) {
    this.me = new Player(playerID, playerName);
    this.players = new ArrayList<>(4);
    this.players.add(this.me);

    this.finishes = 0;
  }

  public ArrayList<Player> getPlayers() {
    players.sort(Player::compareTo);
    return new ArrayList<>(players);
  }

  public void addPlayer(Player player) {
    if (!players.contains(player)) {
      players.add(player);
    }
  }

  public void removePlayer(Player player) {
    players.remove(player);
  }

  public Player getMe() {
    return me;
  }
  public int[][] getMazeGrid() {
    return mazeGrid;
  }
  public void setMazeGrid(int[][] mazeGrid) {
    this.mazeGrid = mazeGrid;
  }
  public int getFinishes() {
    return finishes;
  }
  public void addFinish() {
    finishes++;
  }
}
