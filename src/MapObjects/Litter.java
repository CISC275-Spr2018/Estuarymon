package MapObjects;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashSet;
/**Class of the Litter objects that spawn periodically around the map. 
 * A subclass of Interactable
 * 
 * @author Juan Villacis
 * 
 * 
 *
 */
public class Litter extends Interactable implements Serializable {
	
	int imgID;
	int imgIndex;
	LitterType lType;
	
	public static HashSet<Litter> litterSet = new HashSet<Litter>();
	
	/** Constructor for all Litter objects. Width and height are set at 60 and the collision rectangle at 60X60
	 *
	 */
	public Litter() {
		super(0,0,60,60);
		this.setRelativeCollisionRect(10, 10, 60, 60);
		
	}
	
	/**Method that sets the imgID of a Litter object that is used to map the object to an appropriate Litter Sprite in View. 
	 * @param i that will represent the imgID of the Litter object in game. 
	 */
	public void setImgID(int i) {
		this.imgID= i;
	}
	
	/**Method that returns the imgID that is used to map the litter object to its Sprite in View.
	 * @return The imgID of this Litter object. 
	 */
	public int getImgID() {
		return this.imgID;
	}
	
	/**Sets this Litter object's lType attribute that represents the kind of Litter it is.
	 * 
	 * @param lt A value of the Enum LitterType that will represent the kind of Litter of this object. 
	 */
	public void setType(LitterType lt) {
		this.lType = lt;
	}
	
	/**Returns this Litter object's lType attribute that represents the kind of Litter it is. 
	 * 
	 * @return An value of the Enum LitterType
	 */
	public LitterType getType() {
		return this.lType;
	}
	
	@Override
	public String toString() {
		return "litter" + " at x:" + this.xLocation + " y:" + this.yLocation;
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