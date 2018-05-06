package MapObjects;
import java.awt.Rectangle;

public class Interactable {
	int xLocation;
	int yLocation;
	int width;
	int height;
	private Rectangle relativeCollisionBox;
	
	public Interactable(int x, int y, int width, int height) {
		this.xLocation = x;
		this.yLocation = y;
		this.width = width;
		this.height = height;
	}

	public Rectangle getCollisionRect() {
		if(this.relativeCollisionBox == null) {
			System.out.println("Warning: no collision box set for "+this.toString());
			return new Rectangle(0,0,0,0);
		}
		Rectangle collisionBox = new Rectangle(this.relativeCollisionBox);
		collisionBox.translate(xLocation, yLocation);
		return collisionBox;
	}

	public void setRelativeCollisionRect(int dx, int dy, int width, int height) {
		this.relativeCollisionBox = new Rectangle(dx, dy, width, height);
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

	public void addXLocation(int dx) {
		this.xLocation += dx;
	}

	public void addYLocation(int dy) {
		this.yLocation += dy;
	}
	
	public int getXLocation() {
		return this.xLocation;
	}
	
	public int getYLocation() {
		return this.yLocation;
	}
}
