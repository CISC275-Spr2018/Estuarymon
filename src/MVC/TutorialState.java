package MVC;

/** The current state of the tutorial
 * 
 * @author Juan Villacis 
 *
 */
public enum TutorialState {
	/** The game should spawn a Trash object */
	SPAWNTRASH,
	/** The player should go pick up a Trash object */
	SIGNALTRASH,
	/** The player should deposit into the trash can */
	SIGNALTRASHCAN,
	/** The game should spawn a Recyclable object. */
	SPAWNRECYCLABLE,
	/** The player should go pick up a Recyclable object */
	SIGNALRECYCLABLE,
	/** The player should deposit into the Recycle bin */
	SIGNALRECYCLINGBIN,
	/** The game should damage a plant */
	DAMAGEPLANT,
	/** The player should go plant a Plant */
	SIGNALPLANT,
	/** The crab should move towards a Litter */
	CRABEATLITTER;
}
