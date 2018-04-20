import java.awt.Rectangle;

public class Interactable {
	int xLocation;
	int yLocation;
	int width;
	int height;

	public Rectangle getCollisionRect() {
		return new Rectangle(xLocation, yLocation, width, height);
	}

	public boolean getCollidesWith(Interactable other) {
		return this.getCollisionRect().intersects(other.getCollisionRect());
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setXLocation(int x) {
		this.xLocation = x;
	}
	
	public void setYLocation(int y) {
		this.yLocation = y;
	}
	
	public int getXLocation() {
		return this.xLocation;
	}
	
	public int getYLocation() {
		return this.yLocation;
	}
}
