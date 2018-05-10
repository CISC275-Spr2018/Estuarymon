package MapObjects;
import java.awt.Rectangle;

/**
 * Highest class in the Game object Heirarchy. Contains basic attributes for location and dimensions, as well as collision detection.
 * 
 * @author Zack Klodnicki
 *
 */
public class Interactable implements java.io.Serializable{
	/** The x-location of the object */
	int xLocation;
	/** The y-location of the object */
	int yLocation;
	/** The width of the object */
	int width;
	/** The height of the object */
	int height;
	/** The collision box used by the object. Definition is relative to the 0,0 position of the object. */
	private Rectangle relativeCollisionBox;
	
	/** Create a new Interactable with the given position and dimensions.
	 *  @param x The x-location of the object
	 *  @param y The y-location of the object
	 *  @param width The width of the object
	 *  @param height The height of the object
	 */
	public Interactable(int x, int y, int width, int height) {
		this.xLocation = x;
		this.yLocation = y;
		this.width = width;
		this.height = height;
	}

	/** Returns a rectangle representing the collision box of the Interactable, <em>relative to world coordinate 0,0.</em>
	 *  Note that this is <em>different</em> from the {@link #relativeCollisionBox} used internally.
	 *  @return The collision box of this Interactable relative to world coordinate 0,0. */
	public Rectangle getCollisionRect() {
		if(this.relativeCollisionBox == null) {
			System.out.println("Warning: no collision box set for "+this.toString());
			return new Rectangle(0,0,0,0);
		}
		Rectangle collisionBox = new Rectangle(this.relativeCollisionBox);
		collisionBox.translate(xLocation, yLocation);
		return collisionBox;
	}

	/** Sets the relative collision box of this interactable. Should only be called by subclasses.
	 *  Note that these coordinates are relative to the 0,0 position of the Interactable at any given time.
	 *  @param dx The x-coordinate of the left side of the box, relative to the Interactable's x-position.
	 *  @param dy The y-coordinate of the top side of the box, relative to the Interactable's y-position.
	 *  @param width The width of the box
	 *  @param height The height of the box
	 */
	protected void setRelativeCollisionRect(int dx, int dy, int width, int height) {
		this.relativeCollisionBox = new Rectangle(dx, dy, width, height);
	}

	/** Determines whether this Interactable and another are colliding according to their collision boxes.
	 *  @return True if the two interactables are colliding; false otherwise. 
	 */
	public boolean getCollidesWith(Interactable other) {
		return this.getCollisionRect().intersects(other.getCollisionRect());
	}
	
	/** Gets the {@link #height} of the Interactable
	 *  @return The {@link #height} of the Interactable
	 */
	public int getHeight() {
		return this.height;
	}
	
	/** Gets the {@link #width} of the Interactable
	 *  @return The {@link #width} of the Interactable
	 */
	public int getWidth() {
		return this.width;
	}
	
	/** Sets the x-coordinate of the Interactable
	 *  @param x The new x-coordinate
	 */
	public void setXLocation(int x) {
		this.xLocation = x;
	}
	
	/** Sets the y-coordinate of the Interactable
	 *  @param y The new y-coordinate
	 */
	public void setYLocation(int y) {
		this.yLocation = y;
	}

	/** Adds to the x-coordinate of the Interactable.
	 *  For example, if a <code>can</code> is at <code>x=200</code> and someone calls <code>can.addXLocation(50)</code>,
	 *  then the can will now be located at <code>x=250</code>.
	 *  @param dx The amount to add to the x-coordinate
	 */
	public void addXLocation(int dx) {
		this.xLocation += dx;
	}

	/** Adds to the y-coordinate of the Interactable.
	 *  For example, if a <code>can</code> is at <code>y=200</code> and someone calls <code>can.addYLocation(50)</code>,
	 *  then the can will now be located at <code>y=250</code>.
	 *  @param dy The amount to add to the y-coordinate
	 */
	public void addYLocation(int dy) {
		this.yLocation += dy;
	}
	
	/** Gets the current x-location of the Interactable.
	 *  @return The current x-location of the Interactable.
	 */
	public int getXLocation() {
		return this.xLocation;
	}
	
	/** Gets the current y-location of the Interactable.
	 *  @return The current y-location of the Interactable.
	 */
	public int getYLocation() {
		return this.yLocation;
	}
}
