package client.updated.game;

import client.updated.game.components.Game;
import client.updated.game.components.Pair;
import client.updated.game.components.Player;
import client.updated.gui.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class GameController {
  private static Game game;
  private static Window window;

  public static void createGame(String clientID, String clientName) {
    game = new Game(clientID, clientName);
  }

  public static void createGame(String clientID, String clientName, ArrayList<HashMap<String, String>> players) {
    createGame(clientID, clientName);

    for (HashMap<String, String> playerData : players) {
      createPlayer(playerData.get("client_id"), playerData.get("client_name"));
    }
  }

  public static boolean move(Player player, Pair<Integer> dir) {
    Pair<Integer> newPos = new Pair<>(player.getPos().x + dir.x, player.getPos().y + dir.y);

    // Walking into a wall without changing direction
    if (getTile(newPos) == 0 && player.getDir().equals(dir)) return false;
    player.setDir(dir);

    // Walking into a wall
    if (getTile(newPos) == 0) return true;
    player.setPos(newPos);

    // Walking into the finish
    if (getTile(newPos) == -2) {
      player.addScore(30 - 10 * game.getFinishes());
      addFinish();
      player.setFinished(true);
      window.playerFinished(player);
      player.setPos(new Pair<>(0, 0));
      return true;
    }

    ArrayList<Integer> pickUpItems = new ArrayList<>(Arrays.asList(2,3,4,5,6,7,8,9));

    if(pickUpItems.contains(getTile(newPos))){
      player.setItem(getTile(newPos));
      getTile((newPos))
    }
    // TODO: Check if tile is trap, if yes apply trap effect, otherwise do nothing

    return true;
  }

  public static boolean action(Player player) {
    // TODO: Check if the tile directly in front of the player is a trap,
    //       if yes destroy/remove trap and return true, otherwise return false
    return false;
  }

  public static boolean usePowerup(Player player) {
    // TODO: Check if player has a powerup, if yes then apply effects or use and return true
    //       otherwise return false

    ArrayList<Integer> powerUps = new ArrayList<>(Arrays.asList(2,3,4,5,6,7,8,9));
    if(powerUps.contains(player.getItem())) {
      if(player.getItem() == 2 || player.getItem() == 9){
      //direction w,a,s,d = if 2  12 if 9 11
        return true;
      } else
        if(player.getItem() == 3) { // super star
          player.setEffect("7");
          player.setSpeed(2);
          return true;
        } else
          if(player.getItem() == 5){ // speed boost
            player.setSpeed(2);
            return true;
          } else
          if(player.getItem() == 7){ // shield
          player.setEffect("7");
          return true;
          } else
            if(player.getItem() == 4){
              
            }

        }
    return false;
  }

  public static ArrayList<Player> getPlayers() {
    return game.getPlayers();
  }

  public static Player getPlayer(String clientID) {
    for (Player player : game.getPlayers()) {
      if (player.getID().equals(clientID)) {
        return player;
      }
    }

    return null;
  }

  public static void setMazeGrid(int[][] mazeGrid) {
    game.setMazeGrid(mazeGrid);
  }

  public static void createPlayer(String clientID, String clientName) {
    Player player = new Player(clientID, clientName);
    game.addPlayer(player);
  }

  public static void deletePlayer(String playerID) {
    for (Player player : game.getPlayers()) {
      if (player.getID().equals(playerID)) {
        game.removePlayer(player);
        return;
      }
    }
  }

  public static int getTile(Pair<Integer> pos) {
    return game.getMazeGrid()[pos.y][pos.x];
  }

  public static int[][] getMazeGrid() {
    return game.getMazeGrid();
  }

  public static Player getMe() {
    return game.getMe();
  }

  public static int getFinishes() {
    return game.getFinishes();
  }

  public static void addFinish() {
    game.addFinish();
  }

  public static void setWindow(Window window) {
    GameController.window = window;
  }
}
