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
	private static final int WIDTH = Controller.WORLD_WIDTH;
	private static final int HEIGHT = Controller.WORLD_HEIGHT;

	Player player = new Player(0,0, 165,165);
	
	
	//plantXloc = frameWidth - (frameWidth/3);
	//plantYloc = (frameHeight / 100) + count;
	
	int plantDamage = 10;
	int plantHealth = 100;
	
	String coords = "";

	Receptacle tBin = new Receptacle(0,450,128,128,ReceptacleType.TRASHBIN);
	Receptacle rBin = new Receptacle(0,580,128,128,ReceptacleType.RECYCLINGBIN);
	
	Animal crab;
	HashSet<Animal> animals;

	
	int animalXIncr = 4;
	int animalYIncr = 4;
	
	boolean playerMove = true;
	
	private int score = 0;
	
	public Model()
	{
		this.crab = new Animal();
		animals = new HashSet<Animal>();
		animals.add(crab);
		
		
		
		int count = 0;
		//fills plant array
		for(int i = 0; i < 4; i++)
		{//health,xloc,yoc
			//System.out.println(winW - (winW/3));
			//System.out.println((winH / 100) + count);
			Plant.plants[i] = new Plant(plantHealth, WIDTH - (WIDTH/3), 50+(WIDTH / 90) + count);//sets location of plants
			count = count + 200;
		}

		// Fill animals collection (temporary)
	}
	

	//same method as updateLocationAndDirection()
	public void updateModel()
	{
		this.player.move(playerMove);
		this.checkCollision();
		updatingAnimalLocation();

	}
	
	public void animalWallCollision() {
		if(crab.getXLocation() <= 0 && crab.getDirection() == Direction.WEST) { //when the left wall is hit
			crab.setDirection(Direction.EAST);
		}else if(crab.getXLocation() >= WIDTH - 400 && crab.getDirection() == Direction.EAST) { //when the right wall is hit
			crab.setDirection(Direction.WEST);
		}
		
		else if(crab.getYLocation() <= 0 && crab.getDirection() == Direction.NORTH) { //when the top wall is hit
			crab.setDirection(Direction.SOUTH);
		}else if(crab.getYLocation() >= HEIGHT - 170 && crab.getDirection() == Direction.SOUTH) { //when the bottom wall is hit
			crab.setDirection(Direction.NORTH);
		}
		
		else if(crab.getXLocation() >= WIDTH - 400 && crab.getDirection() == Direction.NORTHEAST) { //when the right wall is hit
			crab.setDirection(Direction.NORTHWEST);
		}else if(crab.getXLocation() >= WIDTH - 400 && crab.getDirection() == Direction.SOUTHEAST) { //when the right wall is hit
			crab.setDirection(Direction.SOUTHWEST);
		}
		
		else if(crab.getYLocation() <= 0  && crab.getDirection() == Direction.NORTHEAST) { //when the top wall is hit
			crab.setDirection(Direction.SOUTHEAST);
		}else if(crab.getYLocation() <= 0  && crab.getDirection() == Direction.NORTHWEST) { //when the top wall is hit
			crab.setDirection(Direction.SOUTHWEST);
		}
		
		else if(crab.getYLocation() >= HEIGHT - 170 && crab.getDirection() == Direction.SOUTHEAST) { //when the bottom wall is hit
			crab.setDirection(Direction.NORTHEAST);
		}else if(crab.getYLocation() >= HEIGHT - 170 && crab.getDirection() == Direction.SOUTHWEST) { //when the bottom wall is hit
			crab.setDirection(Direction.NORTHWEST);
		}
		
		else if(crab.getXLocation() <= 0 && crab.getDirection() == Direction.NORTHWEST) { //when the left wall is hit
			crab.setDirection(Direction.NORTHEAST);
		}else if(crab.getXLocation() <= 0 && crab.getDirection() == Direction.SOUTHWEST) { //when the left wall is hit
			crab.setDirection(Direction.SOUTHEAST);
		}
	}
	
	public void updatingAnimalLocation() {
		//System.out.println(crab.getDirection().ordinal());
		switch(crab.getDirection().ordinal()) {
		case 0: //going north
			crab.setYLocation(crab.getYLocation()-animalYIncr);
			break;
		case 1: //going east 
			crab.setXLocation(crab.getXLocation()+animalXIncr);
			break;
		case 2: //going south
			crab.setYLocation(crab.getYLocation()+animalYIncr);
			break;
		case 3: //going west 
			crab.setXLocation(crab.getXLocation()-animalXIncr);
			break;
		case 4: //going northeast
			crab.setYLocation(crab.getYLocation()-animalYIncr);
			crab.setXLocation(crab.getXLocation()+animalXIncr);
			break;
		case 5: //going northwest
			System.out.println("in 6");
			crab.setYLocation(crab.getYLocation()-animalYIncr);
			crab.setXLocation(crab.getXLocation()-animalXIncr);
			break;
		case 6: //going southeast
			crab.setYLocation(crab.getYLocation()+animalYIncr);
			crab.setXLocation(crab.getXLocation()+animalXIncr);
			break;
		case 7: //going southwest
			crab.setYLocation(crab.getYLocation()+animalYIncr);
			crab.setXLocation(crab.getXLocation()-animalXIncr);
			break;
		default:
			crab.setXLocation(0);
			crab.setYLocation(0);
		
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public Player getPlayer() {
		return player;
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
		
	/**Generates a new Litter object with random x and y coordinates, as well as adds it to the HashSet Litter.litterSet
	 * @return the new Litter object created. 
	 * 
	 */
	public Litter spawnLitter() {
		Random coordGenerator = new Random();
		Litter l = new Litter();
		l.setType(LitterType.randomLitter());		
		int litterXCord = coordGenerator.nextInt((WIDTH-l.getWidth()));//generates random coordinates
		int litterYCord = coordGenerator.nextInt((HEIGHT-l.getHeight()));
		l.setXLocation(litterXCord);//
		l.setYLocation(litterYCord);
		Litter.litterSet.add(l);//Adds them to hashset of litter, prevents exact duplicates in terms of coordinates.
		System.out.println(l);
		return l;
		
	}

	public void testCheckColl()
	{
		checkCollision();
	}
	
	private void checkCollision() {
		for(Litter litter : Litter.litterSet) {
			if(litter.getCollidesWith(this.player))
				this.player.pickUpLitter(litter);
		}
		
		for(int i = 0; i < 4; i++)
		{
			//add and health == 0
			if(Plant.plants[i].health == 0 && Plant.plants[i].getCollidesWith(this.player))
			{
				this.player.growPlant(i);
				score += 10;
			}
		}
		
		if(this.player.hasLitter()) {
			if(this.player.getCollidesWith(this.tBin)) {
				this.tBin.takeLitter(this.player);
				score += 10;
			}
			if(this.player.getCollidesWith(this.rBin)) {
				this.rBin.takeLitter(this.player);
				score += 10;
			}
		}
		
		if(this.player.getCollidesWith(this.crab)) {
			playerMove = false;
			animalXIncr = 6;// * crab.getSpeed();
			animalYIncr = 6;// * crab.getSpeed();
			score -= 5;
			if(player.getDirection() == Direction.EAST) {
				crab.setDirection(Direction.EAST);
			}else if(player.getDirection() == Direction.WEST) {
				crab.setDirection(Direction.WEST);
			}else if(player.getDirection() == Direction.NORTH) {
				crab.setDirection(Direction.NORTH);
			}else if(player.getDirection() == Direction.SOUTH) {
				crab.setDirection(Direction.SOUTH);
			}else if(player.getDirection() == Direction.NORTHEAST) {
				crab.setDirection(Direction.NORTHEAST);
			}else if(player.getDirection() == Direction.NORTHWEST) {
				crab.setDirection(Direction.NORTHWEST);
			}else if(player.getDirection() == Direction.SOUTHWEST) {
				crab.setDirection(Direction.SOUTHWEST);
			}else if(player.getDirection() == Direction.SOUTHEAST) {
				crab.setDirection(Direction.SOUTHEAST);
			}	
			
		}else {
			playerMove = true;
			animalXIncr = 4;// * crab.getSpeed();
			animalYIncr = 4;// * crab.getSpeed();
		}

		animalWallCollision();
		
		
		
		Iterator<Litter> litterIterator = Litter.litterSet.iterator();
		while(litterIterator.hasNext()) {
			Litter litter = litterIterator.next();
			for(Animal animal : this.animals) {
				if(litter.getCollidesWith(animal)) {
					animal.eatLitter();
					score -= 20;
					litterIterator.remove();
				}
			}
		}
	}


}
