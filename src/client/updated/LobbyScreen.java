package client.updated;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class LobbyScreen {
  private final Window window;
  private final GridPane mainGrid;
  private final String serverName;
  private final boolean admin;

  private TextArea txaPlayerNames;

  public LobbyScreen(Window window, GridPane mainGrid, String serverName, boolean admin) {
    this.window = window;
    this.mainGrid = mainGrid;
    this.serverName = serverName;
    this.admin = admin;
  }

  public void show() {
    window.setSize(400, 400);

    mainGrid.add(new Label("Welcome to " + serverName), 0, 0);

    txaPlayerNames = new TextArea();
    txaPlayerNames.setPrefSize(200, 100);
    txaPlayerNames.setEditable(false);
    mainGrid.add(txaPlayerNames, 0, 1);

    Button btnStart = new Button("Start game");
    btnStart.setOnAction((event) -> handleStart());
    btnStart.setDisable(!admin);
    mainGrid.add(btnStart, 0, 2);
    GridPane.setHalignment(btnStart, HPos.RIGHT);

    updatePlayers(window.getPlayerID(), window.getPlayerName());
  }

  private void handleStart() {
    int[][] mazeGrid = window.getNetworkClient().startGame();

    if (mazeGrid == null) {
      throw new RuntimeException("Something went wrong");
    }

    window.showGameScreen(mazeGrid);
  }

  public void updatePlayers(String clientID, String clientName) {
    String playerString = String.format("%s -> %s%n", clientID, clientName);

    txaPlayerNames.appendText(playerString);
  }
}
