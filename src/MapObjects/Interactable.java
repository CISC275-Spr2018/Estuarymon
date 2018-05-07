package MapObjects;
import java.awt.Rectangle;

/**
 * Highest class in the Game object Heirarchy. Contains basic attributes for location and dimensions, as well as collision detection.
 * 
 * @author Zack Klodnicki
 *
 */
public class Interactable {
	/** X location */
	int xLocation;
	/** Y location */
	int yLocation;
	/** Width */
	int width;
	/** Height */
	int height;
	/** Rectangle used for collisions. */
	private Rectangle relativeCollisionBox;
	
	/** 
	 * Constructor for Interactable. Sets basic attributes about location (x, y coordinates), and dimensions (width and height)
	 * 
	 * @param x X location of the Interactable
	 * @param y Y location of the Interactable
	 * @param width Width of the Interactable.
	 * @param height Height of the Interactable. 
	 * @return a new Interactable object with the specified coordinates and dimensions. 
	 */
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
	
	/**
	 * Returns the height of the Interactable 
	 * 
	 * @param None
	 * @return The height of the Interactable
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Returns the width of the Interactable 
	 * 
	 * @param None. 
	 * @return The width of the Interactable. 
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Sets the xLocation attribute of the Interactable. 
	 * 
	 * @param x The x location
	 * @return None. 
	 */
	public void setXLocation(int x) {
		this.xLocation = x;
	}
	
	/**
	 * Sets the yLocation attribute of the Interactable. 
	 * 
	 * @param y The y location 
	 * @return None. 
	 */
	public void setYLocation(int y) {
		this.yLocation = y;
	}

	public void addXLocation(int dx) {
		this.xLocation += dx;
	}

	public void addYLocation(int dy) {
		this.yLocation += dy;
	}
	
	/**
	 * Returns the x location of this Interactable object. 
	 * 
	 * @param None. 
	 * @return The x location. 
	 */
	public int getXLocation() {
		return this.xLocation;
	}
	
	/**
	 * Returns the y location of this Interactable object.
	 * 
	 * @param None. 
	 * @return the Y location. 
	 */
	public int getYLocation() {
		return this.yLocation;
	}
}
