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

public class Animal extends Interactable {

	private Direction curDir;
	private int speed = 1;

	private static int imageHeight = 70;
	private static int imageWidth = 90;

	/**
	 * Constructor for the animal object. The animal originally spawns at the x
	 * coordinate 200 and y coordinate 400. Width is set to 90 and height is set to
	 * 70. The collision rectangle has dimensions of 70x90 and the initial direction
	 * of the animal is Northwest.
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
	 * @param l
	 *            empty
	 * @return The current direction of the crab
	 */
	public Direction getDirection() {
		return curDir;
	}

	/**
	 * "Sets the direction of the crab.
	 * 
	 * @param direction
	 *            The direction that the user wants the crab to go
	 * @return empty
	 */
	public void setDirection(Direction direction) {
		this.curDir = direction;
	}

}
