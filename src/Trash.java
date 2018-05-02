import java.util.HashSet;

/**Subclass of Litter. 
 * 
 * @author Juan Villacis
 *
 */
public class Trash extends Litter {
	

	/**Method that gets the appropriate Receptacle for this type of Litter
	 * @return TrashBin
	 */
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
