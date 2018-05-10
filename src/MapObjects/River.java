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
	
	public River(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.setRelativeCollisionRect(0, 0, width, height);
	}
	
	
	
}
