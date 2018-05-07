package Player;

/** Represents a movement direction on the screen using cardinal and inter-cardinal directions. It is laid out like a compass was set on center of the screen, where north points toward the top center. Thus, south points to the bottom center, west points to the left, etc. */
public enum Direction {
	/** Towards the top of the screen */
	NORTH,
	/** Towards the right side of the screen */
	EAST,
	/** Towards te bottom of the screen */
	SOUTH,
	/** Towards the left side of the screen */
	WEST,
	/** Both {@link #NORTH} and {@link #EAST} */
	NORTHEAST,
	/** Both {@link #NORTH} and {@link #WEST} */
	NORTHWEST,
	/** Both {@link #SOUTH} and {@link #EAST} */
	SOUTHEAST,
	/** Both {@link #SOUTH} and {@link #WEST} */
	SOUTHWEST;
}
