package MapObjects;
import Player.Player;
/**Class of the Receptacles in which the Litter objects are placed into. 
 * A subclass of Interactable
 * 
 * @author Matthew Gargano
 * 
 * 
 *
 */
public class Receptacle extends Interactable{
	/** The type of Receptacle this object is. */
	ReceptacleType rType;
	/** y Location of the trash bin Recptacle in the game */
	public static int trashYpos = 450;
	/** y Location of the recycling bin Receptacle in the game */
	public static int recyclingYpos = 680;
	/** 
	 * Constructor for Receptacle objects. The x coordinate is set to 0, the y coordinate is set according to the type of receptacle.
	 * The height and width are determined. 
	 * 
	 * @param rWidth The width of the Receptacle object. 
	 * @param rHeight the height of the Receptacle object
	 * @param rType Enum value corresponding to the Receptacle type. 
	 * @return A new Receptacle object. 
	 */
	public Receptacle(int rWidth, int rHeight, ReceptacleType rType) {
		super(0, trashYpos + rType.ordinal()*(recyclingYpos - trashYpos), rWidth, rHeight);
		this.rType = rType;
		this.setRelativeCollisionRect(0, 0, rWidth, rHeight);
	}
	/**Method that removes Litter from the player object 
	 * 
	 * @param p the player Object interacting with the receptacle 
	 * @return
	 */
	public void takeLitter(Player p) {
		Player.hasLitter = false;
	}
	/** Method that returns the ReceptacleType
	 *
	 * @param
	 * @return ReceptacleType of the Receptacle
	 */
	public ReceptacleType getType() {
		return this.rType;
	}
}
