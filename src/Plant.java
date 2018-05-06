
public class Plant extends Interactable{
	int health;
	public static Plant [] plants = new Plant[4];
	public static int randPlant = (int) Math.floor(Math.random() * 4);
	
	public Plant(int health, int xLocation, int yLocation) 
	{
		super(xLocation,yLocation,100,100);
		this.health = health;
		this.setRelativeCollisionRect(10, 10, 100, 100);
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealth()
	{
		return health;
	}

}