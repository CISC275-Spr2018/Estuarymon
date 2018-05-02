/**Subclass of Litter. 
 * 
 * @author Juan Villacis 
 *
 */
public class Recyclable extends Litter{

	
	/**Method that gets the appropriate Receptacle for this type of Litter
	 * @return RecycleBin
	 */
	@Override
	public Class targetReceptacle() {
		
		return RecycleBin.class;
	}
	
	public String toString() { //testing purposes
		return "Recyclable at x: " + this.getXLocation() + " y: " + this.getYLocation();
	}
}
