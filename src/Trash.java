import java.util.HashSet;

public class Trash extends Litter {
	


	@Override
	public Class targetReceptacle() {
		// TODO Auto-generated method stub
		return TrashBin.class;
	}
	
	@Override
	public String toString() { //testing purposes
		return "Trash at x: " + this.getXLocation() + " y: " + this.getYLocation();
	}
	
}
