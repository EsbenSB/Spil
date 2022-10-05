package client;

import server.components.MazeGenerator;
import utils.PackageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;



public class GameLogic {
public static List<Player> players = new ArrayList<Player>();	
	public static Player me;

	
	
	public static void makePlayers(String name) {
		/*Pair p=getRandomFreePosition();
		me = new Player(name,p,"up");
		players.add(me);
		p=getRandomFreePosition();
		Player harry = new Player("Kaj",p,"up");
		players.add(harry);*/
	}
	
	public static Pair getRandomFreePosition() {
		// finds a random new position which is not wall
		// and not occupied by other players

		/*
		int x = 1;
		int y = 1;
		boolean foundfreepos = false;
		while  (!foundfreepos) {
			Random r = new Random();
			x = Math.abs(r.nextInt()%18) +1;
			y = Math.abs(r.nextInt()%18) +1;
			if (Generel.board[y].charAt(x)==' ') // er det gulv ?
			{
				foundfreepos = true;
				for (Player p: players) {
					if (p.getXpos()==x && p.getYpos()==y) //pladsen optaget af en anden 
						foundfreepos = false;
				}
				
			}
		}
		Pair p = new Pair(x,y);
		return p;
		 */
		return null;
	}
	
	public static void updatePlayer(int delta_x, int delta_y, String direction) {
		/*
		me.direction = direction;
		int x = me.getXpos(),y = me.getYpos();

		if (Generel.board[y+delta_y].charAt(x+delta_x)=='w') {
			me.addPoints(-1);
		} 
		else {
			// collision detection
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			if (p!=null) {
              me.addPoints(10);
              //update the other player
              p.addPoints(-10);
              Pair pa = getRandomFreePosition();
              p.setLocation(pa);
              Pair oldpos = new Pair(x+delta_x,y+delta_y);
              Gui.movePlayerOnScreen(oldpos,pa,p.direction);
			} else 
				me.addPoints(1);
			Pair oldpos = me.getLocation();
			Pair newpos = new Pair(x+delta_x,y+delta_y);
			Gui.movePlayerOnScreen(oldpos,newpos,direction);
			me.setLocation(newpos);
		}
		 */
	}

	public static HashMap<Pair, Integer> getCellLoc(Pair loc){
		/*
		henter en Key fra client baseret på den loc, man sætter som parameter
		cliet.getKey(loc);
		 */
	}

	public static void grabItem(int delta_x, int delta_y, int item){

	};

	public static void useItem(){
	/* pseudo
	Finder me's nuværende loc
	Kalder getCellLoc med me's nuværende loc
	Tjekker og me.item() er placeable
	Tager den key fra clienten, som macther me's nuværende loc og opdaterer den keys value til me.getItem()
	me.getItem() ændres til null
	else
	Tjekker den om me.items er ikke placeble
	effekten af me.Item() aktiverer
	me.getItem() ændres til null
	
	 Pair currentLoc = me.getLocation();
	  getCellLoc(currenLoc).value = me.getItem();
	  me.getItem() = 0;
		 */

	};

	public static void move(int x, int y){
		int z = me.getXpos();
		int u = me.getYpos();

		Pair newLoc = new Pair(z + (x),u + (y));
		me.setLocation(newLoc);
		Pair newDir = new Pair(x,y);
		me.setDirection(newDir);

		checkCell();

	}

	public static void checkCell(){
		/* pseudo

		Eventuelt laver vi klasse til powerups, og checker at det IKKE er floor eller en wall.
		Hvis det er en cell med en powerup-værdi, tager vi værdien af cellen og giver den tilsvarende powerup.

		for(maze.cell){
			if (maze.cell.value == 0(wall)){
				Error: "Cant move here, this is a wall".
			}

			else if(maze.cell.value == 1(player)){

			}
			else if(maze.cell.value == 2(trap)){

			}
			else if(maze.cell.value == 3(super star)){

			}
			else if(maze.cell.value == 4(pickaxe)){

			}

			else if(maze.cell.value == 5(Speed boost)){

			}

			else if(maze.cell.value == 6(gun)){

			}

			else if (gamelogic.maze.cell(newLoc.value)) == 7(shield){
				pickUpShield(me.getPlayerID);
				}

			else if(maze.cell.value == 8(demon)){

			}

			else if(maze.cell.value == 9(bomb)){

			}

			else if(maze.cell.value == 10(skud)){

			}

			else if(maze.cell.value == 11(eksplosion)){

			}

			else if(maze.cell.value == 12(AKtiv trap)){

			}
		}


		 */
	}

	public static void pickUpShield(Player player){
		/*
		Client sæt sprite til shield

		 */
	}


	public static Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}
	
	
	

}
