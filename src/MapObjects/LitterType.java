package MapObjects;
import java.awt.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**Enum that holds the different type of Litters that can spawn in the game. 
 * 
 * @author Juan Villacis
 *
 */
public enum LitterType {
	TRASH("trash"),
	RECYCLABLE("recyclable");
	
	private static final LitterType[] VALUES = LitterType.values();
	public static final int LENGTH = VALUES.length;
	private static final Random RANDOM = new Random();
	
	private String name = null;
	private LitterType(String s) {
		name = s;
	}
	public String getName() {
		return name;
	}
	
	/**Generates a random value from this enumerated type
	 *
	 * 
	 * @return A randomly selected LitterType value 
	 */
	public static LitterType randomLitter() {
		return VALUES[RANDOM.nextInt(LENGTH)];
	}
	
	
}
