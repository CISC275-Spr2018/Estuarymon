package MapObjects;

import Player.Direction;

/**
 * Class of the animal that is seen walking around on screen. A subclass of
 * Interactable
 * 
 * @author Kalloyan Stoyanov
 * 
 * 
 *
 */

public class Animal extends Interactable implements java.io.Serializable {
	/** Current Direction of the Animal */
	private Direction curDir;
	/** Speed of the Animal */
	private int speed = 1;
	/** Height of the Animal */
	private static int imageHeight = 70;
	/** Width of the Animal */
	private static int imageWidth = 90;
	private static int health = 90;

	/**
	 * Constructor for the animal object. The animal originally spawns at the x
	 * coordinate 200 and y coordinate 400. Width is set to 90 and height is set to
	 * 70. The collision rectangle has dimensions of 70x90 and the initial direction
	 * of the animal is Northwest.
	 * 
	 * @param None. 
	 * @return An Animal object
	 *
	 */
	public Animal() {
		super(200, 400, imageHeight, imageWidth);
		this.setRelativeCollisionRect(15, 15, imageWidth, imageHeight); // this is for the collision detection
		curDir = Direction.NORTHWEST;
	}

	/**
	 * Simple getter method that retrieves the current direction of the animal.
	 * 
<<<<<<< HEAD
	 * @param empty
=======
	 * @param l empty
	 *            
>>>>>>> bdb104553ae472f5edb18095e3e9ab7438aadf4c
	 * @return The current direction of the crab
	 */
	public Direction getDirection() {
		return curDir;
	}

	/**
	 * "Sets the direction of the crab.
	 * 
	 * @param direction The direction that the user wants the crab to go
	 *            
	 * @return empty
	 */
	public void setDirection(Direction direction) {
		this.curDir = direction;
	}
	
	public static int getHealth() {
		return health;
	}

	public static void loseHealth() {
		Animal.health -= 30;
	}

}
