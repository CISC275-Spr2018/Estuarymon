import java.awt.Rectangle;

public class Interactable {
	int xLocation;
	int yLocation;
	final int width;
	final int height;

	public Interactable(int xloc, int yloc, int width, int height) {
		this.xLocation = xloc;
		this.yLocation = yloc;
		this.width = width;
		this.height = height;
	}

	public int getXLocation() {
		return this.xLocation;
	}

	public int getYLocation() {
		return this.yLocation;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void moveTo(int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
	}

	public Rectangle getCollisionRect() {
		return new Rectangle(xLocation, yLocation, width, height);
	}

	public boolean getCollidesWith(Interactable other) {
		return this.getCollisionRect().intersects(other.getCollisionRect());
	}
}
