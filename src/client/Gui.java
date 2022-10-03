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

public class Gui extends Application {

	public static final int size = 30; 
	public static final int scene_height = size * 20 + 50;
	public static final int scene_width = size * 20 + 200;

	public static Image image_floor;
	public static Image image_wall;
	public static Image hero_right,hero_left,hero_up,hero_down;
	public static Image hero_shield_up, hero_shield_down, hero_shield_right, hero_shield_left;
	public static Image hero_blood_up, hero_blood_down, hero_blood_right, hero_blood_left;

	// nye sprites

	// player 1 gul

	public static Image hero1_right,hero1_left,hero1_up,hero1_down;
	public static Image hero1_shield_up, hero1_shield_down, hero1_shield_right, hero1_shield_left;

	// player 2 blå

	public static Image hero2_right,hero2_left,hero2_up,hero2_down;
	public static Image hero2_shield_up, hero2_shield_down, hero2_shield_right, hero2_shield_left;


	// player 3 rød

	public static Image hero3_right,hero3_left,hero3_up,hero3_down;
	public static Image hero3_shield_up, hero3_shield_down, hero3_shield_right, hero3_shield_left;

	// player 4 grøn

	public static Image hero4_right,hero4_left,hero4_up,hero4_down;
	public static Image hero4_shield_up, hero4_shield_down, hero4_shield_right, hero4_shield_left;




	

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


			//XXXX GAMLE SPRITES XXXX
			image_wall  = new Image(getClass().getResourceAsStream("../images/0_1.jpeg"),size,size,false,false);
			image_floor = new Image(getClass().getResourceAsStream("../images/-1.jpeg"),size,size,false,false);

			hero_right  = new Image(getClass().getResourceAsStream("../images/1_1_0_0_0.jpeg"),size,size,false,false);
			hero_left   = new Image(getClass().getResourceAsStream("../images/1_-1_0_0_0.jpeg"),size,size,false,false);
			hero_up     = new Image(getClass().getResourceAsStream("../images/1_0_-1_0_0.jpeg"),size,size,false,false);
			hero_down   = new Image(getClass().getResourceAsStream("../images/1_0_1_0_0.jpeg"),size,size,false,false);

			// hero shield sprite
			hero_shield_up = new Image(getClass().getResourceAsStream("../images/1_0_-1_7_0.jpeg"),size,size,false,false);
			hero_shield_down   = new Image(getClass().getResourceAsStream("../images/1_0_1_7_0.jpeg"),size,size,false,false);
			hero_shield_right   = new Image(getClass().getResourceAsStream("../images/1_1_0_7_0.jpeg"),size,size,false,false);
			hero_shield_left   = new Image(getClass().getResourceAsStream("../images/1_-1_0_7_0.jpeg"),size,size,false,false);

			// hero blood sprite
			hero_blood_up = new Image(getClass().getResourceAsStream("../images/1_0_-1_6_0.jpeg"),size,size,false,false);
			hero_blood_down = new Image(getClass().getResourceAsStream("../images/1_0_1_6_0.jpeg"),size,size,false,false);
			hero_blood_right = new Image(getClass().getResourceAsStream("../images/1_1_0_6_0.jpeg"),size,size,false,false);
			hero_blood_left = new Image(getClass().getResourceAsStream("../images/1_-1_0_6_0.jpeg"),size,size,false,false);



			// ------- NYE SPRITES -------

			// player 1
			hero1_right = new Image(getClass().getResourceAsStream("../images/hero1_right.jpg"),size,size,false,false);
			hero1_left = new Image(getClass().getResourceAsStream("../images/hero1_left.jpg"),size,size,false,false);
			hero1_up = new Image(getClass().getResourceAsStream("../images/hero1_up.jpg"),size,size,false,false);
			hero1_down = new Image(getClass().getResourceAsStream("../images/hero1_down.jpg"),size,size,false,false);

			hero1_shield_up = new Image(getClass().getResourceAsStream("../images/hero1_up_shield.jpg"),size,size,false,false);
			hero1_shield_down   = new Image(getClass().getResourceAsStream("../images/hero1_down_shield.jpg"),size,size,false,false);
			hero1_shield_right   = new Image(getClass().getResourceAsStream("../images/hero1_right_shield.jpg"),size,size,false,false);
			hero1_shield_left   = new Image(getClass().getResourceAsStream("../images/hero1_left_shield.jpg"),size,size,false,false);

			// player 2
			hero2_right = new Image(getClass().getResourceAsStream("../images/hero2_right.jpg"),size,size,false,false);
			hero2_left = new Image(getClass().getResourceAsStream("../images/hero2_left.jpg"),size,size,false,false);
			hero2_up = new Image(getClass().getResourceAsStream("../images/hero2_up.jpg"),size,size,false,false);
			hero2_down = new Image(getClass().getResourceAsStream("../images/hero2_down.jpg"),size,size,false,false);

			hero2_shield_up = new Image(getClass().getResourceAsStream("../images/hero2_up_shield.jpg"),size,size,false,false);
			hero2_shield_down   = new Image(getClass().getResourceAsStream("../images/hero2_down_shield.jpg"),size,size,false,false);
			hero2_shield_right   = new Image(getClass().getResourceAsStream("../images/hero2_right_shield.jpg"),size,size,false,false);
			hero2_shield_left   = new Image(getClass().getResourceAsStream("../images/hero2_left_shield.jpg"),size,size,false,false);

			// player 3
			hero3_right = new Image(getClass().getResourceAsStream("../images/hero3_right.jpg"),size,size,false,false);
			hero3_left = new Image(getClass().getResourceAsStream("../images/hero3_left.jpg"),size,size,false,false);
			hero3_up = new Image(getClass().getResourceAsStream("../images/hero3_up.jpg"),size,size,false,false);
			hero3_down = new Image(getClass().getResourceAsStream("../images/hero3_down.jpg"),size,size,false,false);

			hero3_shield_up = new Image(getClass().getResourceAsStream("../images/hero3_up_shield.jpg"),size,size,false,false);
			hero3_shield_down   = new Image(getClass().getResourceAsStream("../images/hero3_down_shield.jpg"),size,size,false,false);
			hero3_shield_right   = new Image(getClass().getResourceAsStream("../images/hero3_right_shield.jpg"),size,size,false,false);
			hero3_shield_left   = new Image(getClass().getResourceAsStream("../images/hero3_left_shield.jpg"),size,size,false,false);

			// player 4
			hero4_right = new Image(getClass().getResourceAsStream("../images/hero4_right.jpg"),size,size,false,false);
			hero4_left = new Image(getClass().getResourceAsStream("../images/hero4_left.jpg"),size,size,false,false);
			hero4_up = new Image(getClass().getResourceAsStream("../images/hero4_up.jpg"),size,size,false,false);
			hero4_down = new Image(getClass().getResourceAsStream("../images/hero1_down.jpg"),size,size,false,false);

			hero4_shield_up = new Image(getClass().getResourceAsStream("../images/hero4_up_shield.jpg"),size,size,false,false);
			hero4_shield_down   = new Image(getClass().getResourceAsStream("../images/hero4_down_shield.jpg"),size,size,false,false);
			hero4_shield_right   = new Image(getClass().getResourceAsStream("../images/hero4_right_shield.jpg"),size,size,false,false);
			hero4_shield_left   = new Image(getClass().getResourceAsStream("../images/hero4_left_shield.jpg"),size,size,false,false);



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
				case UP:    playerMoved(0,-1,"up");    break;
				case DOWN:  playerMoved(0,+1,"down");  break;
				case LEFT:  playerMoved(-1,0,"left");  break;
				case RIGHT: playerMoved(+1,0,"right"); break;
				case ESCAPE:System.exit(0); 
				default: break;
				}
			});
			
            // Putting default players on screen
			for (int i=0;i<GameLogic.players.size();i++) {
			  fields[GameLogic.players.get(i).getXpos()][GameLogic.players.get(i).getYpos()].setGraphic(new ImageView(hero_up));
			}
			scoreList.setText(getScoreList());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removePlayerOnScreen(Pair oldpos) {
		Platform.runLater(() -> {
			fields[oldpos.getX()][oldpos.getY()].setGraphic(new ImageView(image_floor));
			});
	}
	
	public static void placePlayerOnScreen(Pair newpos, String direction) {
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
	}

	public static void placeBloodyPlayerOnScreen(Pair newpos, String direction) {
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
	}
	
	public static void movePlayerOnScreen(Pair oldpos, Pair newpos, String direction)
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
	public void playerMoved(int delta_x, int delta_y, String direction) {
		GameLogic.updatePlayer(delta_x,delta_y,direction);
		updateScoreTable();
	}
	
	public String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : GameLogic.players) {
			b.append(p+"\r\n");
		}
		return b.toString();
	}




}

