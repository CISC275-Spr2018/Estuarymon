
public class Plant extends Interactable{
	int health;
	
	public Plant(int health, int xLocation, int yLocation) 
	{
		this.health = health;
		this.xLocation = xLocation;
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
