package client.gui;

import client.game.GameController;
import client.game.components.Pair;
import client.game.components.Player;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import test.javafx.MazeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class GameScreen {
  private static final int SCOREBOARD_WIDTH = 200;
  private static final int SCOREBOARD_HEIGHT = 120;

  private final Window window;
  private final GridPane mainGrid;

  private HashMap<String, Image> images;
  private Label[][] tiles;

  private GridPane gameGrid;
  private Label lblPowerup;
  private TextArea txaFinishes;
  private TextArea txaScores;
  private Button btnStart;

  public GameScreen(Window window, GridPane mainGrid) {
    this.window = window;
    this.mainGrid = mainGrid;
    this.mainGrid.setPadding(new Insets(0, 10, 0, 0));

    initGameGrid();
    initKeyEvents();
  }

  public void show() {
    PseudoClass subtitle = PseudoClass.getPseudoClass("subtitle");

    mainGrid.add(gameGrid, 0, 0, 1, 6);

    lblPowerup = new Label("");
    mainGrid.add(lblPowerup, 1, 0);

    Label lblFinishes = new Label("Finishes:");
    lblFinishes.pseudoClassStateChanged(subtitle, true);
    mainGrid.add(lblFinishes, 1, 1);
    GridPane.setMargin(lblFinishes, new Insets(10, 0, 0, 0));

    txaFinishes = new TextArea();
    txaFinishes.setEditable(false);
    txaFinishes.setPrefSize(SCOREBOARD_WIDTH, SCOREBOARD_HEIGHT);
    mainGrid.add(txaFinishes, 1, 2);

    Label lblScores = new Label("Scores:");
    lblScores.pseudoClassStateChanged(subtitle, true);
    mainGrid.add(lblScores, 1, 3);

    txaScores = new TextArea();
    txaScores.setEditable(false);
    txaScores.setPrefSize(SCOREBOARD_WIDTH, SCOREBOARD_HEIGHT);
    mainGrid.add(txaScores, 1, 4);

    btnStart = new Button("Start new game");
    btnStart.setDisable(true);
    btnStart.setOnAction((event) -> handleStart());
    mainGrid.add(btnStart, 1, 5);
    GridPane.setHalignment(btnStart, HPos.CENTER);
    GridPane.setValignment(btnStart, VPos.TOP);

    updatePowerup();
  }

  private void initGameGrid() {
    int[][] mazeGrid = GameController.getMazeGrid();
    int gridWidth = mazeGrid[0].length;
    int gridHeight = mazeGrid.length;

    double imageSize = calcImageSize(gridWidth, gridHeight);
    window.setSize(imageSize * gridWidth + SCOREBOARD_WIDTH + 20, imageSize * gridHeight);
    try {
      this.images = loadImages(imageSize);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    gameGrid = new GridPane();
    gameGrid.setPrefSize(imageSize * gridWidth, imageSize * gridHeight);
    tiles = new Label[gridHeight][gridWidth];

    for (int row = 0; row < gridHeight; row++) {
      for (int col = 0; col < gridWidth; col++) {
        if (row == 1 && col == 1) {
          tiles[row][col] = new Label("", new ImageView(images.get(String.format("1_0_1_0_%s.jpeg",
                  GameController.getMe().getID()))));
          continue;
        }

        String cell = Integer.toString(mazeGrid[row][col]);

        if (cell.equals("0")) {
          cell += "_" + Math.round(Math.random() * 2);
        }

        tiles[row][col] = new Label("", new ImageView(images.get(cell + ".jpeg")));
        gameGrid.add(tiles[row][col], col, row);
      }
    }

    gameGrid.add(tiles[1][1], 1, 1);
  }

  private void initKeyEvents() {
    window.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (event) -> {
      if (GameController.getMe().isFinished()) return;

      String ID = GameController.getMe().getID();
      int invert = GameController.getMe().getEffect().equals("8") ? -1 : 1;

      switch (event.getCode()) {
        case UP:
          Pair<Integer> dir = new Pair<>(0, -1).multiply(invert);
          window.playerMoved(ID, dir, true);
          break;
        case DOWN:
          dir = new Pair<>(0, 1).multiply(invert);
          window.playerMoved(ID, dir, true);
          break;
        case LEFT:
          dir = new Pair<>(-1, 0).multiply(invert);
          window.playerMoved(ID, dir, true);
          break;
        case RIGHT:
          dir = new Pair<>(1, 0).multiply(invert);
          window.playerMoved(ID, dir, true);
          break;
        case W:
          dir = new Pair<>(0, -1).multiply(invert);
          window.playerUsePowerup(ID, dir, true);
          break;
        case S:
          dir = new Pair<>(0, 1).multiply(invert);
          window.playerUsePowerup(ID, dir, true);
          break;
        case A:
          dir = new Pair<>(-1, 0).multiply(invert);
          window.playerUsePowerup(ID, dir, true);
          break;
        case D:
          dir = new Pair<>(1, 0).multiply(invert);
          window.playerUsePowerup(ID, dir, true);
          break;
        case SPACE:
          window.playerAction(ID, true);
      }
    });
  }

  // -------------------------------------------------------------------------------------------------------------------

  private void handleStart() {
    window.getNetworkClient().startGame();
  }

  // -------------------------------------------------------------------------------------------------------------------

  public void resetTile(Pair<Integer> pos) {
    updateTile(pos, images.get("-1.jpeg"));
  }

  private void updateTile(Pair<Integer> pos, Image newImage) {
    tiles[pos.y][pos.x].setGraphic(new ImageView(newImage));
  }

  public void updateScores(ArrayList<Player> players) {
    StringBuilder sb = new StringBuilder();
    for (Player player : players) {
      sb.append(String.format("%s : %d%n", player, player.getScore()));
    }

    txaScores.setText(sb.toString());
  }

  public void addFinish(Player player) {
    txaFinishes.appendText(String.format("%d -> %s%n", GameController.getFinishes(), player));
    updateScores(GameController.getPlayers());

    if (GameController.getFinishes() == GameController.getPlayers().size()) {
      btnStart.setDisable(false);
    }
  }

  public void updatePowerup() {
    Player player = GameController.getMe();
    ImageView imageView = new ImageView(images.get(player.getItem() + ".jpeg"));
    imageView.setFitWidth(SCOREBOARD_WIDTH);
    imageView.setFitHeight(SCOREBOARD_WIDTH);

    lblPowerup.setGraphic(imageView);
  }

  // -------------------------------------------------------------------------------------------------------------------

  public void removePlayer(Player player) {
    for (Player p : GameController.getPlayers()) {
      if (!p.equals(player) && p.getPos().equals(player.getPos())) {
        updateTile(player.getPos(), images.get(p.getImageName()));
        return;
      }
    }

    String originalTile = Integer.toString(GameController.getTile(player.getPos()));
    updateTile(player.getPos(), images.get(originalTile + ".jpeg"));
  }

  public void addPlayer(Player player) {
    if (player.isFinished()) return;

    updateTile(player.getPos(), images.get(player.getImageName()));
  }

  public void handleGun(Pair<Integer> pos, Pair<Integer> dir) {
    boolean start = true;
    boolean hit = false;
    while (!hit) {
      Pair<Integer> nextPos = pos.add(dir);

      int dirHor = dir.x;
      int dirVer = dir.y;
      int version = start ? -1 : 0;

      // Check if hit player
      for (Player player : GameController.getPlayers()) {
        if (player.getPos().equals(nextPos)) {
          addPlayer(player);
          if (start) return;

          version = 1;
          hit = true;
          break;
        }
      }

      // If shot hit wall
      if (GameController.getTile(nextPos) == 0) {
        version = 1;
        hit = true;
      }

      // We should not draw shot on ourselves
      if (!pos.equals(GameController.getMe().getPos())) {
        updateTile(pos, images.get(String.format("10_%d_%d_%d.jpeg", dirHor, dirVer, version)));
        start = false;

        Pair<Integer> finalPos = pos;
        GameController.runLater(1000, () -> {
          resetTile(finalPos);
          return null;
        });
      }

      pos = nextPos;
    }
  }

  public void handleExplosion(Pair<Integer> pos) {
    boolean hit = false;
    for (Player player : GameController.getPlayers()) {
      if (player.getPos().equals(pos)) {
        addPlayer(player);
        hit = true;
        break;
      }
    }

    if (!hit) {
      updateTile(pos, images.get("11_1_1_0.jpeg"));
    }

    Pair<Integer> dir = new Pair<>(0, -1);
    handleExplosion(pos.add(dir), dir);
    dir = new Pair<>(1, 0);
    handleExplosion(pos.add(dir), dir);
    dir = new Pair<>(0, 1);
    handleExplosion(pos.add(dir), dir);
    dir = new Pair<>(-1, 0);
    handleExplosion(pos.add(dir), dir);
  }

  public void handleExplosion(Pair<Integer> pos, Pair<Integer> dir) {
    if (GameController.getTile(pos) == 0) return;

    boolean hit = false;
    for (Player player : GameController.getPlayers()) {
      if (player.getPos().equals(pos)) {
        addPlayer(player);
        hit = true;
        break;
      }
    }

    if (!hit) {
      int stop = GameController.getTile(pos.add(dir)) == 0 ? 1 : 0;
      updateTile(pos, images.get(String.format("11_%d_%d_%d.jpeg", dir.x, dir.y, stop)));
    }

    handleExplosion(pos.add(dir), dir);
  }

  public void handleAction(Pair<Integer> pos) {
    updateTile(pos, images.get(GameController.getTile(pos) + ".jpeg"));
  }

  public void addTrap(Pair<Integer> pos) {
    updateTile(pos, images.get("12.jpeg"));
  }

  // -------------------------------------------------------------------------------------------------------------------

  private double calcImageSize(int gridWidth, int gridHeight) {
    double gridAspectRatio = (double) gridWidth / gridHeight;

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    double screenAvailableWidth = screenBounds.getWidth() - SCOREBOARD_WIDTH - 10;
    double screenAvailableHeight = screenBounds.getHeight();
    double screenAspectRatio = screenAvailableWidth / screenAvailableHeight;

    int prioritizedGridDimension = gridAspectRatio > screenAspectRatio ? gridWidth : gridHeight;
    double prioritizedScreenDimension = gridAspectRatio == 1 ? Math.min(screenAvailableWidth, screenAvailableHeight) :
            (prioritizedGridDimension == gridWidth ? screenAvailableWidth : screenAvailableHeight);

    return prioritizedScreenDimension / prioritizedGridDimension;
  }

  private static HashMap<String, Image> loadImages(double size) throws FileNotFoundException {
    HashMap<String, Image> images = new HashMap<>();

    File folder = new File(System.getProperty("user.dir") + "/src/images");
    File[] files = folder.listFiles();

    if (files == null) throw new FileNotFoundException("Could not load images.");

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
