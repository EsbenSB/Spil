package client.updated.game;

import client.updated.game.components.Game;
import client.updated.game.components.Pair;
import client.updated.game.components.Player;
import client.updated.gui.Window;

import java.util.*;
import java.util.function.Supplier;

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
    if (player.getSpeed() == 0) return false;

    Pair<Integer> newPos = player.getPos().add(dir);

    // Walking into a wall without changing direction
    if (getTile(newPos) == 0 && player.getDir().equals(dir)) return false;
    player.setDir(dir);

    // Walking into a wall
    if (getTile(newPos) == 0) return true;
    player.setPos(newPos);
    boolean shouldReturn = moved(player);

    // Walking into a wall with 2 speed
    if (player.getSpeed() == 1 || shouldReturn) return true;
    if (getTile(player.getPos().add(dir)) == 0) return true;
    player.setPos(newPos.add(dir));
    moved(player);
    return true;
  }

  private static boolean moved(Player player) {
    // Walking into the finish
    if (getTile(player.getPos()) == -2) {
      player.addScore(30 - 10 * game.getFinishes());
      addFinish();
      player.setFinished(true);
      window.playerFinished(player);
      player.setPos(new Pair<>(0, 0));
      return true;
    }

    // Pickup powerup
    ArrayList<Integer> pickUpItems = new ArrayList<>(Arrays.asList(2,3,4,5,6,7,8,9));
    if(pickUpItems.contains(getTile(player.getPos()))){
      player.setItem(getTile(player.getPos()));
      setTile(player.getPos(), -1);
      window.playerGotPowerup();
    }

    if (getTile(player.getPos()) != 12) return false;
    player.setSpeed(0);
    player.setEffect("6");

    runLater(3000, () -> {
      player.setSpeed(1);
      player.setEffect("0");
      return null;
    });
    return true;
  }

  public static boolean action(Player player) {
    Pair<Integer> pos = player.getPos().add(player.getDir());

    if (getTile(pos) != 12) return false;
    setTile(pos, -1);
    return true;
  }

  public static boolean usePowerup(Player player, Pair<Integer> dir) {
    if (!Arrays.asList(2,3,4,5,6,7,8,9).contains(player.getItem())) return false;

    Pair<Integer> targetPos = player.getPos().add(dir);

    switch (player.getItem()) {
      case 2:  // Trap
        if (getTile(targetPos) == 0) return false;

        setTile(targetPos, 12);
        break;
      case 3:  // Super star
        player.setSpeed(2);
        player.setImmune(true);
        //--player.setEffect("3");

        // TODO: We need to set effect="3" and make images for the effect...
        //       and so that we can make the player immune to damage
        int dummyVariable = 0;  // TODO: Remove this line, as it is only here to remove warnings in the switch branch...

        runLater(3000, () -> {
          player.setSpeed(1);
          player.setImmune(false);
          //--player.setEffect("0");
          return null;
        });
        break;
      case 4:  // Pickaxe
        if (getTile(targetPos) != 0) return false;

        setTile(targetPos, -1);
        break;
      case 5:  // Speed boost
        player.setSpeed(2);

        runLater(5000, () -> {
          player.setSpeed(1);
          return null;
        });
        break;
      case 6:  // Gun
        boolean hit = false;
        Pair<Integer> pos = player.getPos().add(dir);

        while (getTile(pos) != 0 && !hit) {
          for (Player p : game.getPlayers()) {
            if (p.getPos().equals(pos)) {
              p.setSpeed(0);
              p.setEffect("6");

              runLater(3000, () -> {
                p.setSpeed(1);
                p.setEffect("0");
                return null;
              });
              hit = true;
              break;
            }
          }
        }
        break;
      case 7:  // Shield
        player.setImmune(true);
        player.setEffect("7");

        runLater(10000, () -> {
          player.setImmune(false);
          player.setEffect("0");
          return null;
        });
        break;
      case 8:  // Demon
        for (Player p : game.getPlayers()) {
          if (!p.equals(player)) {
            p.setEffect("8");

            runLater(5000, () -> {
              p.setEffect("0");
              window.updatePlayer(p);
              return null;
            });
          }
        }
        break;
      case 9:  // Bomb
        if (getTile(targetPos) == 0) targetPos = targetPos.add(dir);
        if (getTile(targetPos) == 0) return false;

        setTile(targetPos, 9);

        Pair<Integer> finalPos = new Pair<>(targetPos.x, targetPos.y);
        runLater(2000, () -> {
          explosion(finalPos);
          window.handleExplosion(finalPos);
          return null;
        });
    }

    player.setItem(-1);
    return true;
  }

  public static void explosion(Pair<Integer> pos) {
    // TODO: Check in all directions for players. If a player is hit make speed=0 and effect="3"
    //       If a wall is hit, stop checking
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

  public static void setTile(Pair<Integer> pos, int tileValue){
    game.getMazeGrid()[pos.y][pos.x] = tileValue;
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
}
