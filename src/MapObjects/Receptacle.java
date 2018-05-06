package MapObjects;
import Player.Player;

public class Receptacle extends Interactable{
	ReceptacleType rType;
	public static int trashYpos = 450;
	public static int recyclingYpos = 580;
	public Receptacle(int xLoc, int yLoc, int rWidth, int rHeight, ReceptacleType rType) {
		super(xLoc, yLoc, rWidth, rHeight);
		this.rType = rType;
		this.setRelativeCollisionRect(0, 0, rWidth, rHeight);
	}
	public void takeLitter(Player p) {
		Player.hasLitter = false;
	}
	
	public ReceptacleType getType() {
		return this.rType;
	}
}
