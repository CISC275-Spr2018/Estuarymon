import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animal extends Interactable {

	private int numOfImages = 35;
	BufferedImage[] img = new BufferedImage[numOfImages];
	// Direction curDir;
	private int imageHeight = 120;
	private int imageWidth = 140;

	public Animal() {
		this.setXLocation(200);
		this.setYLocation(400);
		this.width = imageHeight;
		this.height = imageWidth;
		this.setRelativeCollisionRect(15, 15, 90, 110);
		// curDir = Direction.WEST;
	}

	private BufferedImage createImage(int pictureIndex) {
		BufferedImage bufferedImage;
		// System.out.println(imgName + dir.getName());
		try {
			bufferedImage = ImageIO.read(new File("images/Animal/skeleton-idle_" + pictureIndex + ".png"));
			return bufferedImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public BufferedImage[] loadImages() {
		for (int i = 0; i < 35; i++) {
			img[i] = createImage(i);
		}
		return img;
	}

	public int getNumOfImages() {
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
