package client.gui;

import client.game.GameController;
import client.network.NetworkClient;
import client.game.components.Pair;
import client.game.components.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;

public class Window extends Application {
  private Stage stage;
  private GridPane grid;
  private NetworkClient networkClient;
  private LobbyScreen lobbyScreen;
  private GameScreen gameScreen;

  private EventHandler<KeyEvent> keyEventHandler;

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    this.networkClient = new NetworkClient(this);

    grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    Scene scene = new Scene(grid);
    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/styles.css")).toExternalForm());

    stage.setScene(scene);
    stage.show();

    GameController.setWindow(this);
    showStartScreen();
  }

  public void showStartScreen() {
    Platform.runLater(() -> {
      clearGrid();

      StartScreen startScreen = new StartScreen(this, grid);
      startScreen.show();
    });
  }

  public void showLobbyScreen(String serverName, boolean admin) {
    Platform.runLater(() -> {
      clearGrid();

      lobbyScreen = new LobbyScreen(this, grid, serverName, admin);
      lobbyScreen.show();
      lobbyScreen.updatePlayers(GameController.getPlayers());
    });
  }

  public void showGameScreen() {
    Platform.runLater(() -> {
      clearGrid();

      if (gameScreen != null) {
        GameController.resetGame();
        getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
      }

      gameScreen = new GameScreen(this, grid);
      gameScreen.show();
      gameScreen.updateScores(GameController.getPlayers());
    });
  }

  public void playerJoined() {
    lobbyScreen.updatePlayers(GameController.getPlayers());
  }

  public void playerLeft(String playerID) {
    Platform.runLater(() -> {
      lobbyScreen.updatePlayers(GameController.getPlayers());

      if (gameScreen != null) {
        gameScreen.updateScores(GameController.getPlayers());
        Player player = GameController.getPlayer(playerID);

        if (player == null) return;

        gameScreen.removePlayer(player);
      }
    });
  }

  public void playerMoved(String playerID, Pair<Integer> dir, boolean sendToServer) {
    Platform.runLater(() -> {
      Player player = GameController.getPlayer(playerID);

      if (player == null) return;

      gameScreen.removePlayer(player);
      boolean change = GameController.move(player, dir);
      updatePlayer(player);

      if (change && sendToServer) networkClient.move(dir);
    });
  }

  public void playerAction(String playerID, boolean sendToServer) {
    Platform.runLater(() -> {
      Player player = GameController.getPlayer(playerID);

      if (player == null) return;
      boolean change = GameController.action(player);

      if (!change) return;
      if (sendToServer) networkClient.action();

      gameScreen.handleAction(player.getPos().add(player.getDir()));
    });
  }

  public void playerGotPowerup() {
    gameScreen.updatePowerup();
    gameScreen.resetTile(GameController.getMe().getPos());
  }

  public void playerUsePowerup(String playerID, Pair<Integer> dir, boolean sendToServer) {
    Platform.runLater(() -> {
      Player player = GameController.getPlayer(playerID);

      if (player == null) return;
      boolean change = GameController.usePowerup(player, dir);

      if (!change) return;
      if (sendToServer) networkClient.usePowerup(dir);

      switch (player.getItem()) {
        case 2:  // Trap
          gameScreen.addTrap(player.getPos().add(dir));
          break;
        case 3:  // Super star
        case 7:  // Shield
          updatePlayer(player);
          break;
        case 4:  // Pickaxe
          gameScreen.resetTile(player.getPos().add(dir));
          break;
        case 6:  // Gun
          gameScreen.handleGun(player.getPos(), dir);
          break;
        case 9:  // Bomb
          Pair<Integer> targetPos = player.getPos().add(dir.multiply(2));
          if (GameController.getTile(targetPos) == 0) gameScreen.handleExplosion(targetPos.subtract(dir));
          else gameScreen.handleExplosion(targetPos);
      }

      player.setItem(-1);
      gameScreen.updatePowerup();
    });
  }

  public void playerFinished(Player player) {
    gameScreen.addFinish(player);
    gameScreen.removePlayer(player);
  }

  public void updatePlayer(Player player) {
    Platform.runLater(() -> gameScreen.addPlayer(player));
  }

  public void removeTrap(Pair<Integer> pos) {
    Platform.runLater(() -> gameScreen.removeTrap(pos));
  }

  public void handleExplosion(Pair<Integer> pos) {
    gameScreen.handleExplosion(pos);
  }

  private void clearGrid() {
    grid.getChildren().clear();
  }

  public void setSize(double width, double height) {
    stage.setWidth(width);
    stage.setHeight(height);

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
  }

  public Scene getScene() {
    return stage.getScene();
  }
  public NetworkClient getNetworkClient() {
    return networkClient;
  }
  public EventHandler<KeyEvent> getKeyEventHandler() {
    return keyEventHandler;
  }
  public void setKeyEventHandler(EventHandler<KeyEvent> keyEventHandler) {
    this.keyEventHandler = keyEventHandler;
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    networkClient.stopListening();
  }
}
