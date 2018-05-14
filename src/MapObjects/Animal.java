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
	/** Health of the Animal */
	private static int health = 90;

	/**
	 * Constructor for the animal object. The animal originally spawns at the x
	 * coordinate 0 and y coordinate 480. Width is set to 90 and height is set to
	 * 70. The collision rectangle has dimensions of 70x90 and the initial direction
	 * of the animal is Northwest.
	 * 
	 * @param None. 
	 * @return An Animal object
	 *
	 */
	public Animal() {
		super(360, 50, imageHeight, imageWidth);
		this.setRelativeCollisionRect(15, 15, imageWidth, imageHeight); // this is for the collision detection
		curDir = Direction.NORTHWEST;
	}

	/**
	 * Simple getter method that retrieves the current direction of the animal.
	 *
	 * @param empty
	 *            
	 * @return The current direction of the crab
	 */
	public Direction getDirection() {
		return curDir;
	}

	/**
	 * "Sets the direction of the animal.
	 * 
	 * @param direction The direction that the user wants the crab to go
	 *            
	 * @return empty
	 */
	public void setDirection(Direction direction) {
		this.curDir = direction;
	}
	
	/**
	 * Simple getter method that retrieves the current health of the animal.
	 *
	 * @param empty
	 *            
	 * @return The current health of the animal
	 */
	public static int getHealth() {
		return health;
	}

	/**
	 * "Decreases the health of the animal by 30.
	 * 
	 * @param empty
	 *            
	 * @return empty
	 */
	public static void loseHealth() {
		Animal.health -= 30;
	}

}
