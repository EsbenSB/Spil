package test.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Window extends Application {
  private GridPane grid;
  private Stage stage;

  @Override
  public void start(Stage stage) {
    this.stage = stage;

    grid = new GridPane();
    grid.setHgap(0);
    grid.setVgap(0);
    grid.setPadding(new Insets(0));

    Scene scene = new Scene(grid);
    stage.setScene(scene);
    stage.show();

    showWindowOne();
  }

  private void setSize(double width, double height) {
    stage.setWidth(width);
    stage.setHeight(height);

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
  }

  private void showWindowOne() {
    setSize(400, 400);

    Button btnSwitch = new Button("Switch to window 2");
    btnSwitch.setOnAction(event -> Platform.runLater(() -> {
      grid.getChildren().clear();
      showWindowTwo();
    }));
    grid.add(btnSwitch, 0, 0);
  }

  private void showWindowTwo() {
    setSize(600, 600);

    Button btnSwitch = new Button("Switch to window 1");
    btnSwitch.setOnAction(event -> Platform.runLater(() -> {
      grid.getChildren().clear();
      showWindowOne();
    }));
    grid.add(btnSwitch, 0, 0);
  }
}
