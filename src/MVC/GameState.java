package MVC;



/**Enum that holds all of the Receptacle types that appear in the game 
 * 
 * @author Juan Villacis 
 *
 */
public enum GameState {
	TUTORIAL(0),
	TUTORIAL_SPAWNTRASH(1),
	TUTORIAL_SIGNALTRASH(2),
	TUTORIAL_SIGNALTRASHCAN(3),
	TUTORIAL_SPAWNRECYCLABLE(4),
	TUTORIAL_SIGNALRECYCLABLE(5),
	TUTORIAL_DAMAGEPLANT(6),
	TUTORIAL_SIGNALPLANT(7),
	TUTORIAL_CRABEATLITTER(8),
	REGULARGAME(9);
	
	private static final GameState[] VALUES = GameState.values();
	public static final int LENGTH = VALUES.length;
	
	private int id;
	
	private GameState(int i) {
		id = i;
	}
	
	public int getID() {
		return id;
	}
	
}
