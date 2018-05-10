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
public enum LitterType implements java.io.Serializable{
	TRASH(0),
	RECYCLABLE(1);
	
	private static final LitterType[] VALUES = LitterType.values();
	public static final int LENGTH = VALUES.length;
	private static final Random RANDOM = new Random();
	
	private int id = 0;
	private LitterType(int i) {
		id = i;
	}
	public int getID() {
		return id;
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
