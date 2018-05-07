package Player;
import MapObjects.Interactable;
import MapObjects.Litter;
import MapObjects.LitterType;
import MapObjects.Plant;
import MapObjects.Receptacle;

/** A user-controlled character that can move around the screen and interact with other map objects */
public class Player extends Interactable {
	/** Whether the Player is carrying {@link Litter}. */
	public static boolean hasLitter = false;
	/** Horizontal movement direction. 
	 *  <code>-1</code> for moving left,
	 *  <code>0</code> for horizontally stable (initial value),
	 *  <code>1</code> for moving right.
	 */
	private int dx = 0;
	/** Vertical movement direction.
	 *  <code>-1</code> for moving up,
	 *  <code>0</code> for vertically stable (initial value),
	 *  <code>1</code> for moving down.
	 */
	private int dy = 0;
	/** Movement speed */
	private static final int SPEED = 10;
	/** Movement direction.
	 *  Gets recalculated when {@link #dx} and {@link #dy} are changed to match their behaviour. Initial value is EAST.
	 */
	private Direction direction = Direction.EAST;
	/** Player status, i.e.&nbsp;IDLE or WALKING, etc.
	 *  Gets recalculated when {@link #dx} and {@link #dy} are changed to match their behaviour. Initial value is IDLE.
	 */
	private PlayerStatus status = PlayerStatus.IDLE;

	/** Create a player with the given position and size, and sets up the collision box.
	 *  @param xLoc The x-location of the Player
	 *  @param yLoc The y-location of the Player
	 *  @param rWidth The width of the Player
	 *  @param rHeight The height of the Player
	 */
	public Player(int xLoc, int yLoc, int rWidth, int rHeight) {
		super(xLoc, yLoc, rWidth, rHeight);
		// Set collision rectangle, 40px padding on each side
		this.setRelativeCollisionRect(40, 40, rWidth - 80, rHeight - 80);
	}

	/**Returns a boolean depending on whether or not this player is currently holding a Litter object that needs to be disposed of. 
	 * 
	 * @param None
	 * @return True if the player is holding a peice of Litter, false otherwise. 
	 */
	public boolean getHasLitter() {
		return this.hasLitter;
	}
	
	/**"Picks up" a Litter object the Player is colliding with. Removes the Litter from the Litter hashSet and sets Player.hasLitter to true 
	 * 
	 * @param l The Litter object being picked up 
	 * @return The Litter object being picked up
	 */
	public Litter pickUpLitter(Litter l) {
		System.out.println("Player pick up litter " + l.toString());
		this.hasLitter = true;
		Litter.litterSet.remove(l);
		return l;
	}
	/**
	 * Method that restores a plants health as well as chooses a new plant to start fading. 
	 * 
	 * @param i The index in the plant array of the plant to be replanted. 
	 * @return None. 
	 */
	public void growPlant(int i) {
		System.out.println("Plant!");
		// restore health and pick new plant
		Plant.plants[i].health = 100;
		Plant.randPlant = (int) Math.floor(Math.random() * 4);
	}

	/** Changes the velocity of the Player relatively, and recalculates {@link #direction} and {@link #status}. For example, if the player is moving left then alterVelocity is called with <code>ddx=1</code> <code>ddy=1</code>, then he will start moving down. This method also keeps the velocities bounded from -1 to 1.
	 *  @param ddx The amount to change {@link #dx}.
	 *  @param ddy The amount to change {@link #dy}.
	 *  @see #dy
	 *  @see #dy
	 *  @see #direction
	 *  @see #status
	 */
	public void alterVelocity(int ddx, int ddy) {
		this.dx += ddx;
		this.dy += ddy;
		// Catch when set too high/low because of key repeating
		if (this.dx < -1)
			this.dx = -1;
		else if (this.dx > 1)
			this.dx = 1;
		if (this.dy < -1)
			this.dy = -1;
		else if (this.dy > 1)
			this.dy = 1;

		// Fix direction and status
		this.status = PlayerStatus.WALKING; // All but one are WALKING.
		if (dx < 0) {
			// left
			if (dy < 0) {
				// up
				this.direction = Direction.NORTHWEST;
			} else if (dy > 0) {
				// down
				this.direction = Direction.SOUTHWEST;
			} else {
				// horizontal
				this.direction = Direction.WEST;
			}
		} else if (dx > 0) {
			// right
			if (dy < 0) {
				// up
				this.direction = Direction.NORTHEAST;
			} else if (dy > 0) {
				// down
				this.direction = Direction.SOUTHEAST;
			} else {
				// horizontal
				this.direction = Direction.EAST;
			}
		} else {
			// vertical
			if (dy < 0) {
				// up
				this.direction = Direction.NORTH;
			} else if (dy > 0) {
				// down
				this.direction = Direction.SOUTH;
			} else {
				// horizontal
				// DON'T MODIFY DIRECTION
				// But do fix status.
				this.status = PlayerStatus.IDLE;
			}
		}
	}

	
	/** Moves the player according to it's speed and velocity. */
	public void move() {
		this.addXLocation(SPEED * this.dx);
		this.addYLocation(SPEED * this.dy);
	}
	/** Gets the current {@link #direction} of the Player
	 *  @return The current {@link #direction} of the Player
	 */
	public Direction getDirection() {
		return this.direction;
	}

	/** Gets the current {@link #status} of the player
	 *  @return The current {@link #status} of the Player
	 */
	public PlayerStatus getStatus() {
		return this.status;
	}

	/** Change the player's direction to the parameter
	 *  @param d The new direction
	 */
	public void setDirection(Direction d) {
		this.direction = d;
	}
}
