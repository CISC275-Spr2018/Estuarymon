
public class Receptacle extends Interactable{
	
	ReceptacleType rType;
	public Receptacle(int xLoc, int yLoc, int rWidth, int rHeight, ReceptacleType rType) {
		super(xLoc, yLoc, rWidth, rHeight);
		this.rType = rType;
		this.setRelativeCollisionRect(0, 0, rWidth, rHeight);
	}
	public void takeLitter(Player p) {
		System.out.println("Receptable "+ rType.getName() + " take litter!");
		// You can do something like:
		/*
		if(p.getLitterType().isInstance(this)) {
			// Successfully matched to trash or recycle!
		} else {
			// Deposited in wrong bin!
		}
		*/
	}
	
	public ReceptacleType getType() {
		return this.rType;
	}
}
