package client.updated;

import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class LobbyScreen {
  private final Window window;
  private final GridPane mainGrid;
  private final String serverName;
  private final boolean admin;

  private TextArea txaPlayerNames;
  private Button btnStart;

  public LobbyScreen(Window window, GridPane mainGrid, String serverName, boolean admin) {
    this.window = window;
    this.mainGrid = mainGrid;
    this.mainGrid.setPadding(new Insets(10));
    this.serverName = serverName;
    this.admin = admin;
  }

  public void show() {
    window.setSize(420, 290);

    PseudoClass titleClass = PseudoClass.getPseudoClass("title");
    Label lblTitle = new Label("Welcome to " + serverName);
    lblTitle.pseudoClassStateChanged(titleClass, true);
    mainGrid.add(lblTitle, 0, 0);

    PseudoClass subtitleClass = PseudoClass.getPseudoClass("subtitle");
    Label lblPlayers = new Label("Players:");
    lblPlayers.pseudoClassStateChanged(subtitleClass, true);
    mainGrid.add(lblPlayers, 0, 1);
    GridPane.setMargin(lblPlayers, new Insets(20, 0, 0, 0));

    txaPlayerNames = new TextArea();
    txaPlayerNames.setPrefSize(400, 100);
    txaPlayerNames.setEditable(false);
    mainGrid.add(txaPlayerNames, 0, 2);

    btnStart = new Button("Start game");
    btnStart.setOnAction((event) -> handleStart());
    btnStart.setDisable(!admin);
    mainGrid.add(btnStart, 0, 3);
    GridPane.setHalignment(btnStart, HPos.RIGHT);
  }

  private void handleStart() {
    btnStart.setText("Loading...");
    btnStart.setDisable(true);
    window.getNetworkClient().startGame();
  }

  public void updatePlayers(ArrayList<Player> players) {
    StringBuilder sb = new StringBuilder();
    for (Player player : players) {
      sb.append(String.format("%s -> %s%s%n", player.getID(), player,
              player.getID().equals(GameController.getMe().getID()) ? " (you)" : ""));
    }

    txaPlayerNames.setText(sb.toString());
  }
}
