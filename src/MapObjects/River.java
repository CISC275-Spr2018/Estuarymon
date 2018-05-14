package MapObjects;
/**
 * Represents a river object
 * 
 * @author Hunter
 *
 */
//everytime damage plant is called after health is 0 mod to find how many times its been
//if its more than a certain amount start to increment river
public class River extends Interactable{
	
	/**Constructor for plant object
	 * 
	 * @param xLocation the x coordinate of river on map
	 * @param yLocation the y coordinate of river on map
	 * @param width the width of the river on map
	 * @param height the height of the river on map
	 * 
	 * @return
	 */
	public River(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.setRelativeCollisionRect(0, 0, width, height);
	}
	
	
	
}
