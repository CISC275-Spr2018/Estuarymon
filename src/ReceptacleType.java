/**Enum that holds all of the Receptacle types that appear in the game 
 * 
 * @author Juan Villacis 
 *
 */
public enum ReceptacleType {
	TRASHBIN("trashbin"),
	RECYCLINGBIN("recyclingbin");
	
	private static final LitterType[] VALUES = LitterType.values();
	public static final int LENGTH = VALUES.length;
	
	private String name = null;
	
	private ReceptacleType(String s) {
		name = s;
	}
	
	public String getName() {
		return name;
	}
	
}
