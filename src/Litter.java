import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashSet;
/**
 * Abstract class that is inherited by any Litter subclasses (for now Trash and Recyclable)
 * Contains a static HashSet litterSet where all Litter objects active in the game are stored. 
 * @author Juan Villacis
 * 
 *
 */
public class Litter extends Interactable implements Serializable {
	
	BufferedImage litterImage;
	LitterType lType;
	
	public static HashSet<Litter> litterSet = new HashSet<Litter>();
	
	/** Constructor for all Litter objects. Width and height are set at 40 and the collision rectangle at 40x40
	 *
	 */
	public Litter() {
		super(0,0,40,40);
		this.setRelativeCollisionRect(10, 10, 40, 40);
		
	}
	
	/**Method that sets the BufferedImage that View displays to represent the Litter object 
	 * @param li the BufferedImage that will represent the Litter object in game. 
	 */
	public void setlitterImage(BufferedImage li) {
		this.litterImage = li;
	}
	
	/**Method that returns the BufferedImage that represents the Litter object 
	 * @return The BufferedImage that represents the Litter object  
	 */
	public BufferedImage getlitterImage() {
		return this.litterImage;
	}
	
	public void setType(LitterType lt) {
		this.lType = lt;
	}
	
	public LitterType getType() {
		return this.lType;
	}
	
	@Override
	public String toString() {
		return this.lType.getName() + " at x:" + this.xLocation + " y:" + this.yLocation;
	}
	
	
	/**
	 * Overridden hashCode function that hashes the Litter object by its xLocation on the map. 
	 * @return The litter object's x location. 
	 */
	@Override
	public int hashCode() {
		return this.xLocation;
	}
	
	/**
	 * Overridden equals method. Two objects are considered equals such that l1.equals(l2) returns true if their x and y locations match.
	 * @return true if the two Litter object coordinates match, false otherwise. 
	 */
	@Override
	public boolean equals(Object o) {//Litter is exactly the same if they have the same coordinates. 
		if(o instanceof Litter) {
			Litter l2 = (Litter) o;
			return this.getXLocation() == l2.getYLocation() && this.getYLocation() == l2.getYLocation();
		}
		return false;
	}
	
}