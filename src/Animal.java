import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animal extends Interactable {

	private Direction curDir;
	private int speed = 1;

	private static int imageHeight = 70;
	private static int imageWidth = 90;

	public Animal() {
		super(200, 400, imageHeight, imageWidth);
		this.setRelativeCollisionRect(15, 15, 90, 110); // this is for the collision detection
		curDir = Direction.NORTHWEST;
		// curDir = Direction.WEST;
	}

	public Direction getDirection() {
		return curDir;
	}

	public void setDirection(Direction direction) {
		this.curDir = direction;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void eatLitter() {
		// TODO
		System.out.println("Animal eat litter!");
		// Get sick, change sprite, slow down, retreat, etc.
	}
}
