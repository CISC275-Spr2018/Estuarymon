import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Iterator;

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
	int crabDirection = 3;
	
	//plantXloc = frameWidth - (frameWidth/3);
	//plantYloc = (frameHeight / 100) + count;
	
	int plantDamage = 10;
	int plantHealth = 100;
	
	String coords = "";

	TrashBin tBin = new TrashBin(0,450,128,128);
	RecycleBin rBin = new RecycleBin(0,580,128,128);
	
	Animal crab;
	HashSet<Animal> animals;
	
	public Model(int winW, int winH, int imgW, int imgH, Animal animal) 
	{
		this.winW = winW;
		this.winH = winH;
		this.imgW = imgW;
		this.imgH = imgH;
		this.crab = animal;
		
		int count = 0;
		//fills plant array
		for(int i = 0; i < 4; i++)
		{//health,xloc,yoc
			//System.out.println(winW - (winW/3));
			//System.out.println((winH / 100) + count);
			Plant.plants[i] = new Plant(plantHealth, winW - (winW/3), 50+(winH / 90) + count);//sets location of plants
			count = count + 200;
		}

		// Fill animals collection (temporary)
		this.animals = new HashSet<>();
		this.animals.add(this.crab);
	}

	//same method as updateLocationAndDirection()
	public void updateModel()
	{
		
		//System.out.println(myPlayer.xLocation + ":" + plants[randPlant].xLocation + " and " + myPlayer.yLocation + ":" + plants[randPlant].yLocation);
		//System.out.println(plants[0].health + " " + plants[1].health + " " + plants[2].health + " " + plants[3].health);
		this.checkCollision();
		//coords = " x=" + myPlayer.xLocation + ":" + plants[randPlant].xLocation + " y=" + myPlayer.yLocation + ":" + plants[randPlant].yLocation;
		collisionDetection();
		updateLocation();
		animalWallCollision();
		updatingAnimalLocation();

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
	
	public void animalWallCollision() {
		if(crab.getXLocation() <= 0) { //when the left wall is hit
			System.out.println("hit left wall");
			crabDirection = 1;
		}else if(crab.getXLocation() >= winW - 400) { //when the right wall is hit
			System.out.println("hit right wall");
			crabDirection = 2;
		}else if(crab.getYLocation() <= 0) { //when the top wall is hit
			System.out.println("hit top wall");
			crabDirection = 4;
		}else if(crab.getYLocation() >= winH - 170) { //when the bottom wall is hit
			crabDirection = 3;
		}
	}
	
	public void updatingAnimalLocation() {
		switch(crabDirection) {
		case 1: //going east 
			crab.updateXCoordinate(-3);
			break;
		case 2: //going west 
			crab.updateXCoordinate(3);
			break;
		case 3: //going south
			crab.updateYCoordinate(3);
			break;
		case 4: //going north
			crab.updateYCoordinate(-3);
			break;
		
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
	
	public Animal getAnimal() {
		return crab;
	}
	
	//damage plant every 10 seconds
		public void damagePlant()
		{
			//System.out.println(randPlant);
			if(Plant.plants[Plant.randPlant].getHealth() > 0)
			{
				//deletePlant = 4;//dont delete a plant, no switch case for 4
				Plant.plants[Plant.randPlant].health = Plant.plants[Plant.randPlant].health - plantDamage;
			}
			/*
			else if(Plant.plants[Plant.randPlant].getHealth() == 0)
			{
				//add in if we want more than one plant to die at onceS
			}
			*/
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

	public void testCheckColl()
	{
		checkCollision();
	}
	
	private void checkCollision() {
		for(Litter litter : Litter.litterSet) {
			if(litter.getCollidesWith(this.myPlayer))
				this.myPlayer.pickUpLitter(litter);
		}
		
		for(int i = 0; i < 4; i++)
		{
			//add and health == 0
			if(Plant.plants[i].health == 0 && Plant.plants[i].getCollidesWith(this.myPlayer))
			{
				this.myPlayer.growPlant(i);
			}
		}
		
		if(this.myPlayer.hasLitter()) {
			if(this.myPlayer.getCollidesWith(this.tBin))
				this.tBin.takeLitter(this.myPlayer);
			if(this.myPlayer.getCollidesWith(this.rBin))
				this.rBin.takeLitter(this.myPlayer);
		}

		
		
		
		
		Iterator<Litter> litterIterator = Litter.litterSet.iterator();
		while(litterIterator.hasNext()) {
			Litter litter = litterIterator.next();
			for(Animal animal : this.animals) {
				if(litter.getCollidesWith(animal)) {
					animal.eatLitter();
					litterIterator.remove();
				}
			}
		}
	}


}
