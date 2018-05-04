import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animal extends Interactable {

	private static int numOfImages = 35;
	BufferedImage[] img = new BufferedImage[numOfImages];
	// Direction curDir;
	private static int imageHeight = 120;
	private static int imageWidth = 140;

	public Animal() {
		super(200, 400, imageHeight ,imageWidth);
		this.setRelativeCollisionRect(15, 15, 90, 110);
		// curDir = Direction.WEST;
	}

	

	public static int getNumOfImages() {
		return numOfImages;
	}

	// public Direction getDirection() {
	// return curDir;
	// }
	//
	// public void setDirection(Direction direction) {
	// this.curDir = direction;
	// }
	//
	public void updateXCoordinate(int x) {
		this.setXLocation(this.getXLocation() - x);
	}

	public void updateYCoordinate(int y) {
		this.setYLocation(this.getYLocation() - y);
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public void eatLitter() {
		// TODO
		System.out.println("Animal eat litter!");
		// Get sick, change sprite, slow down, retreat, etc.
	}
}
