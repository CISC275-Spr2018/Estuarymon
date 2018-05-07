package MapObjects;
import Player.Player;
/**Class of the Receptacles in which the Litter objects are placed into. 
 * A subclass of Interactable
 * 
 * 
 * 
 * 
 *
 */
public class Receptacle extends Interactable{
	ReceptacleType rType;
	public static int trashYpos = 450;
	public static int recyclingYpos = 680;
	/** Constructor for Receptacle objects. The x coordinate is set to 0, the y coordinate is set according to the type of receptacle.
	 * The height and width are determined. 
	 *
	 */
	public Receptacle(int rWidth, int rHeight, ReceptacleType rType) {
		super(0, trashYpos + rType.ordinal()*(recyclingYpos - trashYpos), rWidth, rHeight);
		this.rType = rType;
		this.setRelativeCollisionRect(0, 0, rWidth, rHeight);
	}
	/**Method that removes Litter from the player object 
	 * @param p the player Object interacting with the receptacle 
	 */
	public void takeLitter(Player p) {
		Player.hasLitter = false;
	}
	/** Method that returns the ReceptacleType
	 *
	 * 
	 * @return ReceptacleType of the Receptacle
	 */
	public ReceptacleType getType() {
		return this.rType;
	}
}
