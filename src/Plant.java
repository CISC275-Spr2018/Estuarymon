
public class Plant {
	int health = 100;
	int xLocation;
	int yLocation;
	
	public Plant(int health, int xLocation, int yLocation) 
	{
		this.health = health;
		this.xLocation = xLocation;
		this.yLocation = yLocation;
	}

	public int getxLocation() {
		return xLocation;
	}

	public void setxLocation(int xLocation) {
		this.xLocation = xLocation;
	}

	public int getyLocation() {
		return yLocation;
	}

	public void setyLocation(int yLocation) {
		this.yLocation = yLocation;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealth()
	{
		return health;
	}

}
