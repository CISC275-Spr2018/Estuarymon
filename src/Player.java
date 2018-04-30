
public class Player extends Interactable {
	public static boolean hasLitter = false;
	public static Class litterType; 
	
	public Player(int xLoc, int yLoc, int rWidth, int rHeight) {
		this.xLocation = xLoc;
		this.yLocation = yLoc;
		this.width = rWidth;
		this.height = rHeight;
		this.setRelativeCollisionRect(40, 40, rWidth-80, rHeight-80);
	}
	
	public boolean shouldCollectLitter(Litter l) {
		if(this.getCollidesWith(l) && !hasLitter) {
			litterType = l.targetReceptacle();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean shouldDepositLitter(Receptacle r) {
		if(this.getCollidesWith(r) && hasLitter && r.getClass() == litterType) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean hasLitter() {
		return this.hasLitter;
	}

	public void pickUpLitter(Litter l) {
		// TODO
		System.out.println("Player pick up litter "+l.toString());
	}
	
	public void growPlant(int i) {
		// TODO
		System.out.println("Plant!");
		//restore health and pick new plant
		Plant.plants[i].health = 100;
		Plant.randPlant = (int) Math.floor(Math.random() * 4);
	}
}
