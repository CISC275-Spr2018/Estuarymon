package MapObjects;
/**
 * Represents a plant object
 * @author Hunter
 *
 */
public class Plant extends Interactable{
	public int health;
	public static Plant [] plants = new Plant[4];
	public static int randPlant = (int) Math.floor(Math.random() * 4);
	
	/**Constructor for plant object
	 * 
	 * @param health is the amount of health plant starts with
	 * @param xLocation the x coordinate of plant on map
	 * @param yLocation the y coordinate of plant on map
	 * @return
	 */
	public Plant(int health, int xLocation, int yLocation) 
	{
		super(xLocation,yLocation,100,100);
		this.health = health;
		this.setRelativeCollisionRect(10, 10, 200, 200);
	}

	/**Sets health of plant object
	 * 
	 * @param health is the amount of health plant starts with
	 * @return
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	
	/**Gets health of plant object
	 * 
	 * @param 
	 * @return
	 */
	public int getHealth()
	{
		return health;
	}

}
