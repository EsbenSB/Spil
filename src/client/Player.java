package client;

public class Player {
	String name;
	Pair location;
	int point;
	Pair direction;
	int item;
	int condition;

	public Player(String name, Pair loc, Pair direction) {
		this.name = name;
		this.location = loc;
		this.direction = direction;
		this.point = 0;
		this.item = 0;
		this.condition = 0;
	};
	
	public Pair getLocation() {
		return this.location;
	}

	public void setLocation(Pair p) {
		this.location=p;
	}

	public int getXpos() {
		return location.x;
	}
	public void setXpos(int xpos) {
		this.location.x = xpos;
	}
	public int getYpos() {
		return location.y;
	}
	public void setYpos(int ypos) {
		this.location.y = ypos;
	}
	public Pair getDirection() {
		return direction;
	}
	public void setDirection(Pair direction) {
		this.direction = direction;
	}
	public void addPoints(int p) {
		point+=p;
	}
	public String toString() {
		return name+":   "+point;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getItem() {
		return item;
	}
}
