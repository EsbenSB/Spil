package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import test.javafx.MazeView;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

public class Gui extends Application {
	public static final int size = 30; 
	public static final int scene_height = size * 20 + 50;
	public static final int scene_width = size * 20 + 200;

	public static HashMap<String, Image> images = new HashMap<>();
	private static Label[][] fields;
	private TextArea scoreList;

	// -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	
			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();
			
			GridPane boardGrid = new GridPane();

			// --- Whole lotta SPRITES ---
			images = loadImages(size);

			/*
			fields = new Label[20][20];
			for (int j=0; j<20; j++) {
				for (int i=0; i<20; i++) {
					switch (Generel.board[j].charAt(i)) {
					case 'w':
						fields[i][j] = new Label("", new ImageView(image_wall));
						break;
					case ' ':					
						fields[i][j] = new Label("", new ImageView(image_floor));
						break;
					default: throw new Exception("Illegal field value: "+Generel.board[j].charAt(i) );
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			 */
			scoreList.setEditable(false);
			
			
			grid.add(mazeLabel,  0, 0);
			grid.add(scoreLabel, 1, 0);
			grid.add(boardGrid,  0, 1);
			grid.add(scoreList,  1, 1);
						
			Scene scene = new Scene(grid,scene_width,scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

				switch (event.getCode()) {
				case UP:    playerMoved(0,-1); break;
				case DOWN:  playerMoved(0,1);  break;
				case LEFT:  playerMoved(-1,0); break;
				case RIGHT: playerMoved(+1,0); break;
				case ESCAPE:System.exit(0);
				case SPACE: playerUseItem();
				default: break;
				}
			});

			scoreList.setText(getScoreList());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removePlayerOnScreen(Pair oldpos) {
		/*
		Platform.runLater(() -> {
			fields[oldpos.getX()][oldpos.getY()].setGraphic(new ImageView(image_floor));
			});
		 */
	}
	
	public static void placePlayerOnScreen(Pair newpos, Pair direction) {
		/*
		Platform.runLater(() -> {
			int newx = newpos.getX();
			int newy = newpos.getY();
			if (direction.equals("right")) {
				fields[newx][newy].setGraphic(new ImageView(hero_right));
			};
			if (direction.equals("left")) {
				fields[newx][newy].setGraphic(new ImageView(hero_left));
			};
			if (direction.equals("up")) {
				fields[newx][newy].setGraphic(new ImageView(hero_up));
			};
			if (direction.equals("down")) {
				fields[newx][newy].setGraphic(new ImageView(hero_down));
			};
			});
		 */
	}

	public static void placeBloodyPlayerOnScreen(Pair newpos, String direction) {
		/*
		Platform.runLater(() -> {
			int newx = newpos.getX(); // Skal nok bruge den gamle pos.
			int newy = newpos.getY();
			if (direction.equals("right")) {
				fields[newx][newy].setGraphic(new ImageView(hero_blood_right));
			};
			if (direction.equals("left")) {
				fields[newx][newy].setGraphic(new ImageView(hero_blood_left));
			};
			if (direction.equals("up")) {
				fields[newx][newy].setGraphic(new ImageView(hero_blood_up));
			};
			if (direction.equals("down")) {
				fields[newx][newy].setGraphic(new ImageView(hero_blood_down));
			};
		});
		 */
	}
	
	public static void movePlayerOnScreen(Pair oldpos, Pair newpos, Pair direction)
	{
		removePlayerOnScreen(oldpos);
		placePlayerOnScreen(newpos,direction);
	}
	

	
	public void updateScoreTable()
	{
		Platform.runLater(() -> {
			scoreList.setText(getScoreList());
			});
	}

	public void playerMoved(int x, int y) {
		GameLogic.move(x,y);
		updateScoreTable();
	}

	public void playerUseItem() {
		GameLogic.useItem();
		updateScoreTable();
	}

	public void findItem(int delta_x, int delta_y, int item){
		GameLogic.grabItem(delta_x,delta_y,item);

		//if(Pair  )
	}

	
	public String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : GameLogic.players) {
			b.append(p+"\r\n");
		}
		return b.toString();
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

