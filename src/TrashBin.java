
public class TrashBin extends Receptacle {	
	
	public TrashBin(int xLoc, int yLoc, int rWidth, int rHeight) {
		this.xLocation = xLoc;
		this.yLocation = yLoc;
		this.width = rWidth;
		this.height = rHeight;
		this.setRelativeCollisionRect(0, 0, rWidth, rHeight);
	}
	
}

//Icons made by Smashicons https://www.flaticon.com is licensed by "http://creativecommons.org/licenses/by/3.0/" 
