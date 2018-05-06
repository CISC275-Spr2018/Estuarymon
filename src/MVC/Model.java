package MVC;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import MapObjects.Animal;
import MapObjects.Litter;
import MapObjects.LitterType;
import MapObjects.Plant;
import MapObjects.Receptacle;
import MapObjects.ReceptacleType;
import Player.Direction;
import Player.Player;

/**
 * Model: Contains all the state and logic
 * Does not contain anything about images or graphics, must ask view for that
 *
 *@author Juan Villacis
 *@author Matthew Gargano 
 *@author Hunter Suchyj
 *@author Kalloyan Stoyanov 
 *@author Zack Klodnicki 
 **/
public class Model
{
	private static final int WIDTH = Controller.WORLD_WIDTH;
	private static final int HEIGHT = Controller.WORLD_HEIGHT;
	

	Player player = new Player(0,0, 165,165);
	
	boolean spacePressed = false;

	int crabDirection = 3;
	
	//plantXloc = frameWidth - (frameWidth/3);
	//plantYloc = (frameHeight / 100) + count;
	
	int plantDamage = 10;
	int plantHealth = 100;
	
	String coords = "";

	Receptacle tBin = new Receptacle(0,450,128,128,ReceptacleType.TRASHBIN);
	private Receptacle rBin = new Receptacle(0,580,128,128,ReceptacleType.RECYCLINGBIN);
	
	Animal crab;
	HashSet<Animal> animals;

	
	int animalXIncr = 4;
	int animalYIncr = 4;
	
	boolean playerMove = true;
	
	private int score = 0;
	
	Litter pickedUp;
	Litter animalEatenLitter;
	
	
	/**
	 * Constructor for the model class. Adds a new animal as well as 4 Plants to the screen
	 * 
	 * @param None
	 * @return New Model object. 
	 * 
	 */
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
	
	/**
	 * Returns the width of the Model. 
	 * 
	 * @param None
	 * @return The width of the model.
	 */
	public static int getWidth() {
		return WIDTH;
	}



	/**
	 * Returns the height of the Model.
	 * 
	 * @param None. 
	 * @return The height of the Model. 
	 */
	public static int getHeight() {
		return HEIGHT;
	}



	/**
	 * Returns the X increment of the animal.
	 * 
	 * @param None. 
	 * @return The X increment of the animal.
	 */
	public int getAnimalXIncr() {
		return animalXIncr;
	}

	/**
	 * Returns the Y increment of the animal.
	 * 
	 * @param None. 
	 * @return The Y increment of the animal.
	 */
	public int getAnimalYIncr() {
		return animalYIncr;
	}


	/**Gets the Player of the game
	 * 
	 * @param None.
	 * @return Player object representing the Player in the game. 
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the Animal of the game.
	 * 
	 * @param None.
	 * @return the Animal object representing the Animal in the game. 
	 */
	public Animal getAnimal() {
		return crab;
	}
	
	/**
	 * Gets the Litter most recently picked up by the Player.
	 * This will either be the Litter object the player is currently holding, or the last Litter object the player picked up if they are not currently holding one.
	 * 
	 * @param None. 
	 * @return Litter object most recently picked up by the Player
	 */
	public Litter getPickedUpLitter() {
		return this.pickedUp;
	}
	
	/**
	 * Returns the spacePressed boolean of the Model, which tells whether or not the space key is currently being pressed. 
	 * 
	 * @param None. 
	 * @return The spacePressed boolean. True if the spaceKey is currently being pressed, false otherwise. 
	 */
	public boolean getSpacePressed() {
		return this.spacePressed;
	}
	
	/**
	 * Returns the current Direction the Animal in the game is moving. 
	 * 
	 * @param None.
	 * @return Direction of the Animal.
	 */
	public int getCrabDirection() {
		return this.crabDirection;
	}
	
	/**
	 * Returns the Litter object most recently eaten by the Animal.
	 * 
	 * 
	 * @param None.
	 * @return Litter object most recently eaten by the Animal.
	 */
	public Litter getAnimalEatenLitter() {
		return this.animalEatenLitter;
	}
	
	/**
	 * Returns the RecyclingBin Receptacle in the game. 
	 * 
	 * @param None. 
	 * @return The RecyclingBin Receptacle. 
	 */
	public Receptacle getRBin() {
		return rBin;
	}
	
	/**
	 * Returns the TrashBin Receptacle in the game. 
	 * 
	 * @param None. 
	 * @return The TrashBin Receptacle. 
	 */
	public Receptacle getTBin() {
		return tBin;
	}
	
	//same method as updateLocationAndDirection()
	public void updateModel()
	{
		this.player.move(playerMove);
		this.checkCollision();
		updatingAnimalLocation();

	}
	
	/**
	 * Method called when the space key is pressed. Sets the spacePressed boolean value to true;
	 * 
	 * @param None.
	 * @return None.
	 * 
	 */
	public void spaceKeyPressed() {
		this.spacePressed = true;
	}
	
	/**Method called when the space key is released. Sets the spacePressed boolean value to false;
	 * @param None.
	 * @return None.
	 * 
	 */
	public void spaceKeyReleased() {
		this.spacePressed = false;
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
			//System.out.println("in 6");
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
	
	/**
	 * Returns the current score of the game. 
	 * 
	 * @param None. 
	 * @return Current score of the game. 
	 * 
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Method called to decrement plant health by the plantdamage integer value
	 * 
	 * @param None
	 * @return None
	 */
		public void damagePlant()
		{
			if(Plant.plants[Plant.randPlant].getHealth() > 0)
			{
				Plant.plants[Plant.randPlant].health = Plant.plants[Plant.randPlant].health - plantDamage;
			}

		}
		
	/**
	 * Generates a new Litter object with random x and y coordinates, as well as generates a random imgID for the object. 
	 * Adds the newly created Litter object to the static Litter.litterSet
	 * 
	 * @param None
	 * @return The new Litter object created. 
	 * 
	 */
	public Litter spawnLitter() {
		Random r = new Random();
		Litter l = new Litter();
		l.setType(LitterType.randomLitter());		
		int litterXCoord = r.nextInt((WIDTH-l.getWidth()));//generates random coordinates
		int litterYCoord = r.nextInt((HEIGHT-l.getHeight()));
		l.setXLocation(litterXCoord);//
		l.setYLocation(litterYCoord);
		l.setImgID(Math.abs(r.nextInt()));
		Litter.litterSet.add(l);//Adds them to hashset of litter, prevents exact duplicates in terms of coordinates.
		System.out.println(l);
		return l;
		
	}

	public boolean testCheckColl()
	{
		return checkCollision();
	}
	
	private boolean checkCollision() {
		
		if(!Player.hasLitter) {
			for(Litter litter : new HashSet<Litter>(Litter.litterSet)) {
				if(litter.getCollidesWith(this.player)) {
					if(spacePressed)
					this.pickedUp = this.player.pickUpLitter(litter);
					return true;
				}
					
			}
		}
		
		for(int i = 0; i < 4; i++)
		{
			//add and health == 0
			if(Plant.plants[i].health == 0 && Plant.plants[i].getCollidesWith(this.player))
			{
				this.player.growPlant(i);
				changeScore(10);
				return true;
			}
			
		}
		
		if(this.player.getHasLitter()) {
			if(this.player.getCollidesWith(this.tBin)) {
				this.tBin.takeLitter(this.player);
				return true;
			}	
			if(this.player.getCollidesWith(this.rBin)) {
				this.rBin.takeLitter(this.player);
				return true;
			}
				
		}
		
		if(this.player.getCollidesWith(this.crab)) {
			playerMove = false;
			animalXIncr = 6;// * crab.getSpeed();
			animalYIncr = 6;// * crab.getSpeed()
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
					changeScore(-20);
					this.animalEatenLitter = litter;
					litterIterator.remove();
					return true;
				}
			}
		}
		return false;
	}
	
	/**Method to change the game's score.
	 * 
	 * @param i The amount of points to be added (positive integer) or subtracted (negative integer) from the score. 
	 * @return None. 
	 */
	public void changeScore(int i) {
		if(this.score + i < 0) {
			this.score = 0;
		}
		else {
			this.score += i;
		}
	}

}
