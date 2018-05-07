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
 * Model: Contains all the state and logic Does not contain anything about
 * images or graphics, must ask {@link View} for that
 *
 * has methods to detect collision with boundaries decide next direction provide
 * direction provide location
 **/
public class Model {
	/** The world width, copied from Controller for convenience */
	private static final int WIDTH = Controller.WORLD_WIDTH;
	/** The world height, copied from Controller for convenience */
	private static final int HEIGHT = Controller.WORLD_HEIGHT;
	
	/** The only controllable object in the game. 
	 *  @see Player */
	private Player player = new Player(0,0, 165,165);
	
	/** Whether the space key is currently pressed down. */
	private boolean spacePressed = false;

	/** The current movement direction of the {@link #crab}. */
	private int crabDirection = 3;
	
	/** The amount of health to detract from the Plant every time it is damaged */
	private static final int plantDamage = 10;
	/** The initial amount of health of each Plant */
	private static final int plantHealth = 100;
	
	/** The trash bin */
	private Receptacle tBin = new Receptacle(128,128,ReceptacleType.TRASHBIN);
	/** The recycle bin */
	private Receptacle rBin = new Receptacle(128,128,ReceptacleType.RECYCLINGBIN);
	
	/** Whether the trash bin recently received a piece of Litter */
	public static boolean trashVictory = false;
	/** Whether the recycle bin recently received a piece of Litter */
	public static boolean recycleVictory = false;

	/** The Crab, currently the only Animal in the game. */
	private Animal crab;
	/** Every animal in the world. Currently only the crab. */
	private HashSet<Animal> animals;

	/** The horizontal speed of the Crab */
	private int animalXIncr = 4;
	/** The vertical speed of the Crab */
	private int animalYIncr = 4;

	/** Whether the player is allowed to move this frame. Set to false under specific circumstances, i.e. when colliding with an Animal */
	private boolean playerMove = true;

	/** The current game score */
	private int score = 0;

	/** The last Litter to be picked up by the {@link #player} */
	private Litter pickedUp;
	/** The last Litter to be picked up by an {@link #animals animal} */
	private Litter animalEatenLitter;
	
	/**
	 * Initializes all fields. Also instantiates {@link #crab} and puts the instance in {@link #animals}. Also instantiates four {@link Plant}s in {@link Plant#plants}.
	 */
	public Model() {
		this.crab = new Animal();
		animals = new HashSet<Animal>();
		animals.add(crab);
		
		int count = 0;
		//fills plant array
		for(int i = 0; i < 4; i++)
		{
			Plant.plants[i] = new Plant(plantHealth, WIDTH - (WIDTH/3), 50+(WIDTH / 90) + count);//sets location of plants
			count = count + 200;
		}
	}
	
	/**
	 * Simple getter method that retrieves the amount the x coordinate should be
	 * incremented by.
	 * 
	 * @param empty
	 * @return The amount the animal increments horizontally
	 */
	public int getAnimalXIncr() {
		return animalXIncr;
	}

	/**
	 * Simple getter method that retrieves the amount the y coordinate should be
	 * incremented by.
	 * 
	 * @param empty
	 * @return The amount the animal increments vertically.
	 */
	public int getAnimalYIncr() {
		return animalYIncr;
	}

	/**
	 * Gets the Player of the game
	 * 
	 * @return Player object representing the Player in the game.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the Animal of the game
	 * 
	 * @return the Animal object representing the Animal in the game.
	 */
	public Animal getAnimal() {
		return crab;
	}

	/**
	 * Gets the Litter most recently picked up by the Player
	 * 
	 * @return Litter object most recently picked up by the Player
	 */
	public Litter getPickedUpLitter() {
		return this.pickedUp;
	}

	/** Gets whether the space key is pressed down.
	 *  @return Whether the space key is pressed down.
	 */
	public boolean getSpacePressed() {
		return this.spacePressed;
	}

	/** Gets {@link #crab}'s current movement direction
	 *  @return {@link #crab}'s current movement direction
	 */
	public int getCrabDirection() {
		return this.crabDirection;
	}

	/** Gets the {@link Litter} most recently eaten by an {@link Animal}.
	 *  @return The {@link Litter} most recently eaten by an {@link Animal}.
	 */
	public Litter getAnimalEatenLitter() {
		return this.animalEatenLitter;
	}

	/** Gets the {@link #rBin recycle bin}.
	 *  @return The {@link #rBin recycle bin}.
	 */
	public Receptacle getRBin() {
		return rBin;
	}

	/** Gets the {@link #tBin trash bin}.
	 *  @return The {@link #tBin trash bin}.
	 */
	public Receptacle getTBin() {
		return tBin;
	}

	/** Sets {@link #crab}'s direction.
	 *  @param i The new direction for {@link #crab} to move.
	 */
	public void setCrabDirection(int i) {
		this.crabDirection = i;
	}
			
	/** Advances the Model by one frame. 
	 *  Moves {@link #player}, checks for collisions, runs collision handlers, and moves the {@link #animals}. 
	 *  Should be called once per expected screen frame. */
	public void updateModel() {
		if(playerMove)
			this.player.move();
		this.checkCollision();
		updatingAnimalLocation();
		
	}
	
	/** Signals that the space key was pressed down */
	public void spaceKeyPressed() {
		this.spacePressed = true;
	}
	
	/** Signals that the space key was released */
	public void spaceKeyReleased() {
		this.spacePressed = false;
	}
	
	/**
	 * Method that checks whether the crab has hit a wall. If the crab has hit a
	 * wall it also checks to see what the direction of the crab was, so that it can
	 * change the direction and keep the crab on screen.
	 * 
	 * @param empty
	 * @return empty
	 */
	public void animalWallCollision() {
		if (crab.getXLocation() <= 0 && crab.getDirection() == Direction.WEST) { // when the left wall is hit
			crab.setDirection(Direction.EAST);
		} else if (crab.getXLocation() >= WIDTH - 400 && crab.getDirection() == Direction.EAST) { // when the right wall
																									// is hit
			crab.setDirection(Direction.WEST);
		}

		else if (crab.getYLocation() <= 0 && crab.getDirection() == Direction.NORTH) { // when the top wall is hit
			crab.setDirection(Direction.SOUTH);
		} else if (crab.getYLocation() >= HEIGHT - 170 && crab.getDirection() == Direction.SOUTH) { // when the bottom
																									// wall is hit
			crab.setDirection(Direction.NORTH);
		}

		else if (crab.getXLocation() >= WIDTH - 400 && crab.getDirection() == Direction.NORTHEAST) { // when the right
																										// wall is hit
			crab.setDirection(Direction.NORTHWEST);
		} else if (crab.getXLocation() >= WIDTH - 400 && crab.getDirection() == Direction.SOUTHEAST) { // when the right
																										// wall is hit
			crab.setDirection(Direction.SOUTHWEST);
		}

		else if (crab.getYLocation() <= 0 && crab.getDirection() == Direction.NORTHEAST) { // when the top wall is hit
			crab.setDirection(Direction.SOUTHEAST);
		} else if (crab.getYLocation() <= 0 && crab.getDirection() == Direction.NORTHWEST) { // when the top wall is hit
			crab.setDirection(Direction.SOUTHWEST);
		}

		else if (crab.getYLocation() >= HEIGHT - 170 && crab.getDirection() == Direction.SOUTHEAST) { // when the bottom
																										// wall is hit
			crab.setDirection(Direction.NORTHEAST);
		} else if (crab.getYLocation() >= HEIGHT - 170 && crab.getDirection() == Direction.SOUTHWEST) { // when the
																										// bottom wall
																										// is hit
			crab.setDirection(Direction.NORTHWEST);
		}

		else if (crab.getXLocation() <= 0 && crab.getDirection() == Direction.NORTHWEST) { // when the left wall is hit
			crab.setDirection(Direction.NORTHEAST);
		} else if (crab.getXLocation() <= 0 && crab.getDirection() == Direction.SOUTHWEST) { // when the left wall is
																								// hit
			crab.setDirection(Direction.SOUTHEAST);
		}
	}
	
	/**
	 * Method that updates the x and y coordinates of the crab depending on its
	 * current direction.
	 * 
	 * @param empty
	 * @return empty
	 */
	public void updatingAnimalLocation() {
		// System.out.println(crab.getDirection().ordinal());
		switch (crab.getDirection().ordinal()) {
		case 0: // going north
			crab.setYLocation(crab.getYLocation() - animalYIncr);
			break;
		case 1: // going east
			crab.setXLocation(crab.getXLocation() + animalXIncr);
			break;
		case 2: // going south
			crab.setYLocation(crab.getYLocation() + animalYIncr);
			break;
		case 3: // going west
			crab.setXLocation(crab.getXLocation() - animalXIncr);
			break;
		case 4: // going northeast
			crab.setYLocation(crab.getYLocation() - animalYIncr);
			crab.setXLocation(crab.getXLocation() + animalXIncr);
			break;
		case 5: // going northwest
			System.out.println("in 6");
			crab.setYLocation(crab.getYLocation() - animalYIncr);
			crab.setXLocation(crab.getXLocation() - animalXIncr);
			break;
		case 6: // going southeast
			crab.setYLocation(crab.getYLocation() + animalYIncr);
			crab.setXLocation(crab.getXLocation() + animalXIncr);
			break;
		case 7: // going southwest
			crab.setYLocation(crab.getYLocation() + animalYIncr);
			crab.setXLocation(crab.getXLocation() - animalXIncr);
			break;
		default:
			crab.setXLocation(0);
			crab.setYLocation(0);

		}
	}

	/**
	 * Simple getter method that retrieves the score. Created so that the View knows
	 * what the score is and can display it.
	 * 
	 * @param empty
	 * @return The current score
	 */
	public int getScore() {
		return score;
	}
	
	// damage plant every 10 seconds
		/**
		 * Method called to decrement plant health by the plantdamage integer value
		 * 
		 * @param
		 * @return
		 */
		public void damagePlant() {
			if (Plant.plants[Plant.randPlant].getHealth() > 0) {
				Plant.plants[Plant.randPlant].health = Plant.plants[Plant.randPlant].health - plantDamage;
			}

		}
		
		/**
		 * Generates a new Litter object with random x and y coordinates, as well as
		 * generates a random imgID for the object.
		 * 
		 * @return the new Litter object created.
		 * 
		 */
		public Litter spawnLitter() {
			Random r = new Random();
			Litter l = new Litter();
			l.setType(LitterType.randomLitter());
			int litterXCoord = r.nextInt((WIDTH - l.getWidth()));// generates random coordinates
			int litterYCoord = r.nextInt((HEIGHT - l.getHeight()));
			l.setXLocation(litterXCoord);//
			l.setYLocation(litterYCoord);
			l.setImgID(Math.abs(r.nextInt()));
			Litter.litterSet.add(l);// Adds them to hashset of litter, prevents exact duplicates in terms of
									// coordinates.
			System.out.println(l);
			return l;

		}

	/** A public version of {@link #checkCollision} only for use by the {@link ModelTest} class.
	 *  @see #checkCollision
	 *  @see ModelTest
	 */
	public boolean testCheckColl() {
		return checkCollision();
	}
	
	/**
	 * Method that deals with all the various collisions in the game. If the player
	 * collides with the crab, the player is slowed down, the crab goes in the same
	 * direction as the player, and the score is decreased. In addition, the crab
	 * speeds up while the player is colliding with it to give it the effect that it
	 * is scared. If the crab collides with a piece of trash or recycling, the piece
	 * of trash or recycling is removed and the score is decreased.
	 * 
	 * @param empty
	 * @return whether a collision has been detected
	 */
	private boolean checkCollision() {

		if (!Player.hasLitter) {
			for (Litter litter : new HashSet<Litter>(Litter.litterSet)) {
				if (litter.getCollidesWith(this.player)) {
					if (spacePressed)
						this.pickedUp = this.player.pickUpLitter(litter);
					return true;
				}

			}
		}

		for (int i = 0; i < 4; i++) {
			// add and health == 0
			if (Plant.plants[i].health == 0 && Plant.plants[i].getCollidesWith(this.player)) {
				this.player.growPlant(i);
				score += 10;
				return true;
			}

		}
		
		if(this.player.getHasLitter()) {
			if(this.player.getCollidesWith(this.tBin) && this.pickedUp.getType() == LitterType.TRASH) {
				this.tBin.takeLitter(this.player);
				//System.out.println("DEPOSITED TRASH");
				trashVictory = true;
				return true;
			}	
			if(this.player.getCollidesWith(this.rBin) && this.pickedUp.getType() == LitterType.RECYCLABLE) {
				this.rBin.takeLitter(this.player);
				//System.out.println("DEPOSITED RECYCLABLE");
				recycleVictory = true;
				return true;
			}
				
		}

		if (this.player.getCollidesWith(this.crab)) {
			playerMove = false;
			animalXIncr = 6;
			animalYIncr = 6;
			score -= 5;
			if (player.getDirection() == Direction.EAST) {
				crab.setDirection(Direction.EAST);
			} else if (player.getDirection() == Direction.WEST) {
				crab.setDirection(Direction.WEST);
			} else if (player.getDirection() == Direction.NORTH) {
				crab.setDirection(Direction.NORTH);
			} else if (player.getDirection() == Direction.SOUTH) {
				crab.setDirection(Direction.SOUTH);
			} else if (player.getDirection() == Direction.NORTHEAST) {
				crab.setDirection(Direction.NORTHEAST);
			} else if (player.getDirection() == Direction.NORTHWEST) {
				crab.setDirection(Direction.NORTHWEST);
			} else if (player.getDirection() == Direction.SOUTHWEST) {
				crab.setDirection(Direction.SOUTHWEST);
			} else if (player.getDirection() == Direction.SOUTHEAST) {
				crab.setDirection(Direction.SOUTHEAST);
			}
			return true;

		} else {
			playerMove = true;
			animalXIncr = 4;
			animalYIncr = 4;
		}

		animalWallCollision();

		Iterator<Litter> litterIterator = Litter.litterSet.iterator();
		while (litterIterator.hasNext()) {
			Litter litter = litterIterator.next();
			for (Animal animal : this.animals) {
				if (litter.getCollidesWith(animal)) {
					score -= 20;
					this.animalEatenLitter = litter;
					litterIterator.remove();
					return true;
				}
			}
		}
		return false;
	}

}
