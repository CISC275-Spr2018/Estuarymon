
public abstract class Litter extends Interactable{
	
	public Litter() {
		this.width = 60;
		this.height = 60;
	}
	
	
	public abstract Class targetReceptacle();
	
	@Override
	public int hashCode() {
		return this.xLocation;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Litter) {
			Litter l2 = (Litter) o;
			return this.getXLocation() == l2.getYLocation() && this.getYLocation() == l2.getYLocation();
		}
		return false;
	}
	
}
