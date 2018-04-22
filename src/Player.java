
public class Player extends Interactable {
	public static boolean hasLitter = false;
	public static Class litterType; 
	public Player(int xLoc, int yLoc, int rWidth, int rHeight) {
		this.xLocation = xLoc;
		this.yLocation = yLoc;
		this.width = rWidth;
		this.height = rHeight;
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

}
