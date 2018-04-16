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
}
