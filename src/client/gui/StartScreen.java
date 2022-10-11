package client.gui;

import client.gui.utils.ErrorHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import utils.ErrorCode;

public class StartScreen {
  private final Window window;
  private final GridPane mainGrid;

  private TextField txfPlayerName, txfServerName;
  private Label lblError;
  private Button btnCreate, btnJoin;

  public StartScreen(Window window, GridPane mainGrid) {
    this.window = window;
    this.mainGrid = mainGrid;
    this.mainGrid.setPadding(new Insets(10));
  }

  public void show() {
    window.setSize(265, 210);

    mainGrid.add(new Label("Player name:"), 0, 0);
    mainGrid.add(new Label("Server name:"), 0, 1);

    txfPlayerName = new TextField();
    mainGrid.add(txfPlayerName, 1, 0);

    txfServerName = new TextField();
    mainGrid.add(txfServerName, 1, 1);

    lblError = new Label();
    ErrorHandler.addError(lblError);
    mainGrid.add(lblError, 0, 2, 2, 1);

    btnCreate = new Button("Create server");
    btnCreate.setOnAction((event) -> handleCreate());
    mainGrid.add(btnCreate, 0, 3, 2, 1);
    GridPane.setHalignment(btnCreate, HPos.CENTER);

    btnJoin = new Button("Join server");
    btnJoin.setOnAction((event) -> handleJoin());
    mainGrid.add(btnJoin, 0, 4, 2, 1);
    GridPane.setHalignment(btnJoin, HPos.CENTER);
  }

  private void handleCreate() {
    if (unsafeInputs(txfPlayerName, txfServerName)) return;

    btnCreate.setText("Loading...");
    btnCreate.setDisable(true);
    boolean status = window.getNetworkClient().createServer(txfPlayerName.getText(), txfServerName.getText());

    if (!status) {
      btnCreate.setText("Create server");
      btnCreate.setDisable(false);

      ErrorHandler.addError(txfServerName);
      lblError.setText("Please choose another server name");
    }
  }

  private void handleJoin() {
    if (unsafeInputs(txfPlayerName, txfServerName)) return;

    btnJoin.setText("Loading...");
    btnJoin.setDisable(true);
    String error = window.getNetworkClient().joinServer(txfPlayerName.getText(), txfServerName.getText());

    if (error == null) return;

    btnJoin.setText("Join server");
    btnJoin.setDisable(false);

    if (error.equals(ErrorCode.NOT_FOUND)) {
      ErrorHandler.addError(txfServerName);
      lblError.setText("Couldn't find server");
    } else if (error.equals(ErrorCode.SERVER_FULL)) {
      ErrorHandler.addError(txfServerName);
      lblError.setText("Server is full");
    } else {
      ErrorHandler.addError(txfServerName);
      lblError.setText("Server has started the game");
    }
  }

  private boolean unsafeInputs(TextField ...fields) {
    String regex = "^[A-Za-z\\d\\s_]*$";

    for (TextField field : fields) {
      String text = field.getText().trim();
      field.setText(text);
      if (text.isBlank() || !text.matches(regex)) {
        ErrorHandler.addError(field);
        lblError.setText("Field empty or contains illegal characters");
        return true;
      } else if (ErrorHandler.hasError(field)) {
        ErrorHandler.removeError(field);
      }
    }

    lblError.setText("");
    return false;
  }
}
