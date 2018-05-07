package Player;
import MapObjects.Interactable;
import MapObjects.Litter;
import MapObjects.LitterType;
import MapObjects.Plant;
import MapObjects.Receptacle;

/**
 * Player class that represents the player in the game. Extends the Interactable class. 
 * 
 * @author Juan Villacis 
 *
 */
public class Player extends Interactable {
	/** Boolean that determines whether the Player is currently holding a peice of Litter*/
	public static boolean hasLitter = false;
	private int dx = 0;
	private int dy = 0;
	/** Speed of the player */
	private final int speed = 10;
	/** Direction enum representing the current Player's direction. */
	private Direction direction = Direction.EAST;
	/** PlayerStatus enum representing the current Player's status */
	private PlayerStatus status = PlayerStatus.IDLE;
	
	/** Constructor for a new Player. Sets initial coordinates of the player, as well as its dimensions and the collision Rectangle for it. 
	 * 
	 * @param xLoc Initial x location of the Player. 
	 * @param yLoc Initial y location of the Player.
	 * @param rWidth Width of the Player. 
	 * @param rHeight Height of the Player
	 * @return a new Player object with the specified dimensions and coordinates. 
	 */
	public Player(int xLoc, int yLoc, int rWidth, int rHeight) {
		super(xLoc, yLoc, rWidth, rHeight);
		this.setRelativeCollisionRect(40, 40, rWidth - 80, rHeight - 80);
	}

//	public boolean shouldCollectLitter(Litter l) {
//		if (this.getCollidesWith(l) && !hasLitter) {
//	
//	public boolean shouldCollectLitter(Litter l) {
//		if(this.getCollidesWith(l) && !hasLitter) {
//			litterType = l.getType();
//			return true;
//		} else {
//			return false;
//		}
//	}

//	public boolean shouldDepositLitter(Receptacle r) {
//		if (this.getCollidesWith(r) && hasLitter && r.getType().ordinal() == litterType.ordinal()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
	/**
	 * Sets the dx attribute of the Player. Used for movement and collision detection. 
	 * 
	 * @param dx the Dx attribute of the Player. 
	 * @return None. 
	 */
	public void setDx(int dx) {
		this.dx = dx;
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
		// TODO
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
		// TODO
		System.out.println("Plant!");
		// restore health and pick new plant
		Plant.plants[i].health = 100;
		Plant.randPlant = (int) Math.floor(Math.random() * 4);
	}

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
		this.status = PlayerStatus.WALKING;
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

	public void move(boolean playerMove) {
		if (playerMove) {
			this.addXLocation(this.speed * this.dx);
			this.addYLocation(this.speed * this.dy);
		} else {
			this.addXLocation(0);
			this.addYLocation(0);
		}
	}
	/**
	 * Returns the current Direction of the Player. 
	 * 
	 * @param None. 
	 * @return The enum value corresponding to the direction of the player. 
	 */
	public Direction getDirection() {
		return this.direction;
	}
	
	/**
	 * Method that sets the Direction of this Player object
	 *  
	 * @param d The new Direction of the Player
	 * @return None. 
	 */
	public void setDirection(Direction d) {
		this.direction = d;
	}
	
	/**
	 * Returns the current status of the Player. 
	 * 
	 * @param None. 
	 * @return The enum value corresponding to the status of the player. 
	 */
	public PlayerStatus getStatus() {
		return this.status;
	}
}
