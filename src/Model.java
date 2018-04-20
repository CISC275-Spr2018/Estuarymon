import java.awt.event.KeyEvent;

/**
 * Model: Contains all the state and logic
 * Does not contain anything about images or graphics, must ask view for that
 *
 * has methods to
 *  detect collision with boundaries
 * decide next direction
 * provide direction
 * provide location
 **/
public class Model
{
	int winW;
	int winH;
	int imgW;
	int imgH;

	int xLoc = 0;
	int yLoc = 0;

	int xIncr = 0;
	int yIncr = 0;

	Direction curDir = Direction.EAST;
	
	int dir = 0;

	public Model(int winW, int winH, int imgW, int imgH) 
	{
		this.winW = winW;
		this.winH = winH;
		this.imgW = imgW;
		this.imgH = imgH;
	}

	//same method as updateLocationAndDirection()
	public void updateModel()
	{
		
		//System.out.println("xLoc: " + xLoc + " yLoc: " + yLoc);
		collisionDetection();
		updateLocation();

	}
	
	//used to be called updateDirection, but that no longer really applied because the orc
	//doesn't need to change directions when it hits the wall
	public void collisionDetection() {
		if((yLoc + imgH/5)<= 0 && curDir == Direction.NORTH) {
			dir = 1;
		}else if((yLoc + imgH) >= winH && curDir == Direction.SOUTH) {
			dir = 2;
		}else if((xLoc + imgW) >= winW && curDir == Direction.EAST) {
			dir = 3;
		}else if(xLoc <= 0 && curDir == Direction.WEST) {
			dir = 4;
		}
	}
	
	public void updateLocation() {
		switch(dir){
		case 0:
			xLoc += 0;
			yLoc += 0;
			break;
		case 1: //bottom wall; for him to go n
			if(yLoc + imgH/5 <= 0){
				yIncr = 0;
			}
			yLoc-=yIncr;
			break;
		case 2: //top wall; for him to go s
			if((yLoc + imgH) >= winH) {
				yIncr = 0;
			}
			yLoc+=yIncr;
			break;
		case 3: //left wall; for him to go e
			if((xLoc + imgW) >= winW) {
				xIncr = 0;
			}
			xLoc+=xIncr;
			break;
		case 4: //right wall; for him to go w
			if(xLoc <= 0) {
				xIncr = 0;
			}
			xLoc-=xIncr;
			break;
		}
	
	}
	
	public void setAttributes(int dir, Direction direction, int xIncr, int yIncr) {
		this.dir = dir;
		this.curDir = direction;
		this.xIncr = xIncr;
		this.yIncr = yIncr;
	}

	public void stop() {
		this.xIncr = 0;
		this.yIncr = 0;
	}

	public int getX()
	{
		return xLoc;
	}

	public int getY()
	{
		return yLoc;
	}

	public Direction getDirect()
	{
		return curDir;
	}


}
