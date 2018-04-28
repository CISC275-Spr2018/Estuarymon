import java.awt.image.BufferedImage;
import java.util.HashSet;

public abstract class Litter extends Interactable{
	
	BufferedImage litterImage;
	
	public static HashSet<Litter> litterSet = new HashSet<Litter>();
	public Litter() {
		this.width = 60;
		this.height = 60;
		this.setRelativeCollisionRect(10, 10, 40, 40);
	}
	
	public void setlitterImage(BufferedImage li) {
		this.litterImage = li;
	}
	
	public BufferedImage getlitterImage() {
		return this.litterImage;
	}
	
	public abstract Class targetReceptacle();
	
	@Override
	public int hashCode() {
		return this.xLocation;
	}
	
	@Override
	public boolean equals(Object o) {//Litter is exactly the same if they have the same coordinates. 
		if(o instanceof Litter) {
			Litter l2 = (Litter) o;
			return this.getXLocation() == l2.getYLocation() && this.getYLocation() == l2.getYLocation();
		}
		return false;
	}
	public String toString() { //testing purposes
		return "Rec at x: " + this.getXLocation() + " y: " + this.getYLocation();
	}
}
