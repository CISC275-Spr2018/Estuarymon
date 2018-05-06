package MapObjects;

/**Enum that holds all of the Receptacle types that appear in the game 
 * 
 * @author Juan Villacis 
 *
 */
public enum ReceptacleType {
	TRASHBIN(0),
	RECYCLINGBIN(1);
	
	private static final LitterType[] VALUES = LitterType.values();
	public static final int LENGTH = VALUES.length;
	
	private int id;
	
	private ReceptacleType(int i) {
		id = i;
	}
	
	public int getID() {
		return id;
	}
	
}
