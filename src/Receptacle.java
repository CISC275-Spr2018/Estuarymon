
public abstract class Receptacle extends Interactable{
	public void takeLitter(Player p) {
		System.out.println("Receptable "+this.getClass().toString()+" take litter!");
		// You can do something like:
		/*
		if(p.getLitterType().isInstance(this)) {
			// Successfully matched to trash or recycle!
		} else {
			// Deposited in wrong bin!
		}
		*/
	}
}
