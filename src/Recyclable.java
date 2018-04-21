
public class Recyclable extends Litter{

	

	@Override
	public Class targetReceptacle() {
		
		return RecycleBin.class;
	}
	
	public String toString() { //testing purposes
		return "Recyclable at x: " + this.getXLocation() + " y: " + this.getYLocation();
	}
}
