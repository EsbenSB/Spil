package test.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import server.components.MazeGenerator;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

public class MazeView extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    int viewWidth = 900;
    int viewHeight = 600;
    int mazeWidth = 20;
    int mazeHeight = 10;

    GridPane grid = new GridPane();
    grid.setHgap(0);
    grid.setVgap(0);
    grid.setPadding(new Insets(0));

    HashMap<String, Image> images = loadImages(viewWidth/(mazeWidth*2.0 + 1));

    MazeGenerator mazeGenerator = new MazeGenerator(mazeWidth, mazeHeight);
    int[][] mazeGrid = mazeGenerator.getGrid();

    for (int row = 0; row < mazeGrid.length; row++) {
      for (int col = 0; col < mazeGrid[0].length; col++) {
        String image = Integer.toString(mazeGrid[row][col]);

        if (image.equals("0")) {
          image += "_" + Math.round(Math.random() * 2);
        }

        grid.add(new ImageView(images.get(image + ".jpeg")), row, col);
      }
    }

    Scene scene = new Scene(grid, viewWidth, viewHeight);
    stage.setScene(scene);
    stage.show();
  }

  private static HashMap<String, Image> loadImages(double size) throws Exception {
    HashMap<String, Image> images = new HashMap<>();

    File folder = new File(System.getProperty("user.dir") + "/src/images");
    File[] files = folder.listFiles();

    if (files == null) throw new Exception("Could not load images.");

    for (final File fileEntry : files) {
      String localPath = "/images/" + fileEntry.getName();
      if (!localPath.endsWith(".jpeg")) continue;

      InputStream imageStream = MazeView.class.getResourceAsStream(localPath);
      if (imageStream == null) continue;

      images.put(fileEntry.getName(), new Image(imageStream, size, size, false, false));
    }

    return images;
  }
}
