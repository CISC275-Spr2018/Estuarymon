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

	Player myPlayer = new Player(0,0, 165,165);
	//int xLoc = 0;
	//int yLoc = 0;

	int xIncr = 0;
	int yIncr = 0;

	Direction curDir = Direction.EAST;
	
	int dir = 0;
	
	//plantXloc = frameWidth - (frameWidth/3);
	//plantYloc = (frameHeight / 100) + count;
	
	Plant [] plants = new Plant[4]; 
	int randPlant = (int) Math.floor(Math.random() * 4);//between 0 and 3
	int plantDamage = 10;
	int plantHealth = 100;
	int deletePlant = 4;
	//for alphar testing
	String coords;

	TrashBin tBin = new TrashBin(0,450,128,128);
	RecycleBin rBin = new RecycleBin(0,580,128,128);
	
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
			//System.out.println(winW - (winW/3));
			//System.out.println((winH / 100) + count);
			this.plants[i] = new Plant(plantHealth, winW - (winW/3), (winH / 90) + count);//sets location of plants
			count = count + 200;
		}
	}

	//same method as updateLocationAndDirection()
	public void updateModel()
	{
		
		//System.out.println(myPlayer.xLocation + ":" + plants[randPlant].xLocation + " and " + myPlayer.yLocation + ":" + plants[randPlant].yLocation);
		//System.out.println(plants[0].health + " " + plants[1].health + " " + plants[2].health + " " + plants[3].health);
		coords = myPlayer.xLocation + ":" + plants[randPlant].xLocation + " and " + myPlayer.yLocation + ":" + plants[randPlant].yLocation;
		collisionDetection();
		updateLocation();

	}
	
	//used to be called updateDirection, but that no longer really applied because the orc
	//doesn't need to change directions when it hits the wall
	public void collisionDetection() {
		if((myPlayer.yLocation + imgH/5)<= 0 && curDir == Direction.NORTH) {
			dir = 1;
		}else if((myPlayer.yLocation + imgH) >= winH && curDir == Direction.SOUTH) {
			dir = 2;
		}else if((myPlayer.xLocation + imgW) >= winW && curDir == Direction.EAST) {
			dir = 3;
		}else if(myPlayer.xLocation <= 0 && curDir == Direction.WEST) {
			dir = 4;
		}
	}
	
	public void updateLocation() {
		switch(dir){
		case 0:
			myPlayer.xLocation += 0;
			myPlayer.yLocation += 0;
			break;
		case 1: //bottom wall; for him to go n
			if(myPlayer.yLocation + imgH/5 <= 0){
				yIncr = 0;
			}
			myPlayer.yLocation-=yIncr;
			break;
		case 2: //top wall; for him to go s
			if((myPlayer.yLocation + imgH) >= winH) {
				yIncr = 0;
			}
			myPlayer.yLocation+=yIncr;
			break;
		case 3: //left wall; for him to go e
			if((myPlayer.xLocation + imgW) >= winW) {
				xIncr = 0;
			}
			myPlayer.xLocation+=xIncr;
			break;
		case 4: //right wall; for him to go w
			if(myPlayer.xLocation <= 0) {
				xIncr = 0;
			}
			myPlayer.xLocation-=xIncr;
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
		return myPlayer.xLocation;
	}

	public int getY()
	{
		return myPlayer.yLocation;
	}

	public Direction getDirect()
	{
		return curDir;
	}
	
	//damage plant every 10 seconds
		public void damagePlant()
		{
			//System.out.println(randPlant);
			if(plants[randPlant].getHealth() > 0)
			{
				deletePlant = 4;//dont delete a plant, no switch case for 4
				plants[randPlant].health = plants[randPlant].health - plantDamage;
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
		
	
	public void genLitterCords(Litter l) {
		Random coordGenerator = new Random();
		int litterXCord = coordGenerator.nextInt((winW-l.getWidth()));//generates random coordinates
		int litterYCord = coordGenerator.nextInt((winH-l.getHeight()));
		l.setXLocation(litterXCord);//
		l.setYLocation(litterYCord);
		Litter.litterSet.add(l);//Adds them to hashset of litter, prevents exact duplicates in terms of coordinates.
		System.out.println(l);
		
	}
	



}
