package MVC;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;

import MapObjects.Animal;
import MapObjects.Litter;
import MapObjects.LitterType;
import MapObjects.Plant;
import MapObjects.Receptacle;
import MapObjects.ReceptacleType;
import Player.Player;

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
	
	int animalXIncr = 3;
	int animalYIncr = 3;
	
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
	
	Litter pickedUp;
	Litter animalEatenLitter;
	
	
	
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
	

	public static int getWidth() {
		return WIDTH;
	}




	public static int getHeight() {
		return HEIGHT;
	}




	public int getAnimalXIncr() {
		return animalXIncr;
	}


	public int getAnimalYIncr() {
		return animalYIncr;
	}


	/**Gets the Player of the game
	 * 
	 * @return Player object representing the Player in the game. 
	 */
	public Player getPlayer() {
		return player;
	}

	/**Gets the Animal of the game
	 * 
	 * @return the Animal object representing the Animal in the game. 
	 */
	public Animal getAnimal() {
		return crab;
	}
	
	/**Gets the Litter most recently picked up by the Player
	 * 
	 * @return Litter object most recently picked up by the Player
	 */
	public Litter getPickedUpLitter() {
		return this.pickedUp;
	}
	
	public boolean getSpacePressed() {
		return this.spacePressed;
	}
	
	public int getCrabDirection() {
		return this.crabDirection;
	}
	
	public Litter getAnimalEatenLitter() {
		return this.animalEatenLitter;
	}
	public Receptacle getRBin() {
		return rBin;
	}

	public Receptacle getTBin() {
		return tBin;
	}
	
	public void setCrabDirection(int i) {
		this.crabDirection = i;
	}
	
	

	
	
	

	//same method as updateLocationAndDirection()
	public void updateModel()
	{
		this.player.move();
		this.checkCollision();
		animalWallCollision();
		updatingAnimalLocation();

	}
	
	public void spaceKeyPressed() {
		this.spacePressed = true;
	}
	
	public void spaceKeyReleased() {
		this.spacePressed = false;
	}
	
	public void animalWallCollision() {
		if(crab.getXLocation() <= 0) { //when the left wall is hit
			System.out.println("hit left wall");
			crabDirection = 1;
		}else if(crab.getXLocation() >= WIDTH - 400) { //when the right wall is hit
			System.out.println("hit right wall");
			crabDirection = 2;
		}else if(crab.getYLocation() <= 0) { //when the top wall is hit
			System.out.println("hit top wall");
			crabDirection = 4;
		}else if(crab.getYLocation() >= HEIGHT - 170) { //when the bottom wall is hit
			crabDirection = 3;
		}
	}
	
	
	
	public void updatingAnimalLocation() {
		switch(crabDirection) {
		case 1: //going east 
			crab.setXLocation(crab.getXLocation()+animalXIncr);
			break;
		case 2: //going west 
			crab.setXLocation(crab.getXLocation()-animalXIncr);
			break;
		case 3: //going south
			crab.setYLocation(crab.getYLocation()-animalYIncr);
			break;
		case 4: //going north
			crab.setYLocation(crab.getYLocation()+animalYIncr);
			break;
		
		}
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
		
	/**Generates a new Litter object with random x and y coordinates, as well as generates a random imgID for the object. 
	 * @return the new Litter object created. 
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

		
		
		
		
		Iterator<Litter> litterIterator = Litter.litterSet.iterator();
		while(litterIterator.hasNext()) {
			Litter litter = litterIterator.next();
			for(Animal animal : this.animals) {
				if(litter.getCollidesWith(animal)) {
					animal.eatLitter();
					this.animalEatenLitter = litter;
					litterIterator.remove();
					return true;
				}
			}
		}
		return false;
	}

}
