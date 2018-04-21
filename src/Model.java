import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;

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
	
	//plantXloc = frameWidth - (frameWidth/3);
	//plantYloc = (frameHeight / 100) + count;
	
	Plant [] plants = new Plant[4]; 
	int randPlant = (int) Math.floor(Math.random() * 4);//between 0 and 3
	int deletePlant = 4;
	HashSet<Litter> litterSet = new HashSet<>();
	
	public Model(int winW, int winH, int imgW, int imgH) 
	{
		this.winW = winW;
		this.winH = winH;
		this.imgW = imgW;
		this.imgH = imgH;
		
		int count = 0;
		//fills plant array
		for(int i = 0; i < 4; i++)
		{//health,xloc,yoc
			System.out.println(winW - (winW/3));
			System.out.println((winH / 100) + count);
			this.plants[i] = new Plant(100, winW - (winW/3), (winH / 90) + count);//sets location of plants
			count = count + 200;
		}
	}

	//same method as updateLocationAndDirection()
	public void updateModel()
	{
		
		//System.out.println(xLoc + ":" + plants[0].xLocation + " and " + yLoc + ":" + plants[0].yLocation);
		System.out.println(plants[0].health + " " + plants[1].health + " " + plants[2].health + " " + plants[3].health);
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
	
	//damage plant every 10 seconds
		public void damagePlant()
		{
			System.out.println(randPlant);
			if(plants[randPlant].getHealth() > 0)
			{
				deletePlant = 4;//dont delete a plant, no switch case for 4
				plants[randPlant].health = plants[randPlant].health - 10;
			}
			else if(plants[randPlant].getHealth() == 0)
			{
				//send randplant number
				//update view corresponding to which plant reached zero
				deletePlant = randPlant;
				//wait until player revives plant
				
				//randPlant = (int) Math.floor(Math.random() * 4);
			}
		}
		
	public void spawnLitter() {
		Random coordGenerator = new Random();
		Litter newLitter = new Trash();
		if(Math.random() < 0.5) {
			newLitter = new Recyclable();
		}
		int litterXCord = coordGenerator.nextInt((winW-newLitter.getWidth()));
		int litterYCord = coordGenerator.nextInt((winH-newLitter.getHeight()));
		newLitter.setXLocation(litterXCord);
		newLitter.setYLocation(litterYCord);
		litterSet.add(newLitter);
		
	}


}
