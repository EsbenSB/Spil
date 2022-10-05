package client.updated;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Objects;

public class Window extends Application {
  private Stage stage;
  private GridPane grid;
  private NetworkClient networkClient;
  private LobbyScreen lobbyScreen;
  private GameScreen gameScreen;
  private Game game;

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    this.networkClient = new NetworkClient(this);

    grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10));

    Scene scene = new Scene(grid);
    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

    stage.setScene(scene);
    stage.show();

    showStartScreen();
  }

  public void showStartScreen() {
    clearGrid();

    StartScreen startScreen = new StartScreen(this, grid);
    startScreen.show();
  }

  public void showLobbyScreen(String serverName, boolean admin) {
    clearGrid();

    lobbyScreen = new LobbyScreen(this, grid, serverName, admin);
    lobbyScreen.show();
  }

  public void showGameScreen(int[][] mazeGrid) {
    clearGrid();

    // TODO: Remove this when fixed
    System.out.println(Arrays.deepToString(mazeGrid));

    gameScreen = new GameScreen(this, grid);
    gameScreen.show();
  }

  public void showGameScreenLater(int[][] mazeGrid) {
    Platform.runLater(() -> showGameScreen(mazeGrid));
  }

  public void createGame(String clientID, String ClientName) {
    // TODO: Don't forget this
    //--game = new Game(clientID, clientName);
  }

  public void playerJoined(String clientID, String clientName) {
    // TODO: Don't forget this
    //--game.createPlayer(clientID, clientName);
    lobbyScreen.updatePlayers(clientID, clientName);
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

  public NetworkClient getNetworkClient() {
    return networkClient;
  }

  public String getPlayerID() {
    // TODO: Don't forget this
    //--return game.getID();
    return "0";
  }

  public String getPlayerName() {
    // TODO: Don't forget this
    //--return game.getName();
    return "lukas";
  }
}
