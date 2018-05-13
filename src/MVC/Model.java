package MVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.swing.Timer;

import MapObjects.Animal;
import MapObjects.Litter;
import MapObjects.LitterType;
import MapObjects.Plant;
import MapObjects.Receptacle;
import MapObjects.ReceptacleType;
import MapObjects.River;
import Player.Direction;
import Player.Player;
import Player.PlayerStatus;

/**
 * Model: Contains all the state and logic Does not contain anything about
 * images or graphics, must ask {@link View} for that
 *
 * has methods to detect collision with boundaries decide next direction provide
 * direction provide location
 * 
 * @author Juan Villacis
 * @author Matthew Gargano
 * @author Hunter Suchyj
 * @author Kalloyan Stoyanov
 * @author Zack Klodnicki
 **/
public class Model implements java.io.Serializable {
	private static int WIDTH;
	private static int HEIGHT;

	/**
	 * The only controllable object in the game.
	 * 
	 * @see Player
	 */
	private Player player = new Player(0, 0, 165, 165);
	/** The only controllable object in the game. 
	 *  @see Player */
	//private Player player;
	/** The current game phase */
	private GamePhase gamePhase = GamePhase.TITLE_SCREEN;
	/** Current state of the tutorial */
	private TutorialState tutorialState;
	
	/** Whether the Player is carrying {@link Litter}. */
	private boolean hasLitter = false;

	/** Whether the space key is currently pressed down. */
	private boolean spacePressed = false;

	/** The current movement direction of the {@link #crab}. */
	private int crabDirection;

	/** The amount of health to detract from the Plant every time it is damaged */
	private static final int plantDamage = 10;
	/** The initial amount of health of each Plant */
	private static final int plantHealth = 100;

	/** The trash bin */
	private Receptacle tBin = new Receptacle(128, 128, ReceptacleType.TRASHBIN);
	/** The recycle bin */
	private Receptacle rBin = new Receptacle(128, 128, ReceptacleType.RECYCLINGBIN);

	/** Whether the trash bin recently received a piece of Litter */
	private static boolean trashVictory = false;
	/** Whether the recycle bin recently received a piece of Litter */

	public static boolean recycleVictory = false;
	/**
	 * A count of the number of frames the trash bin has been in glowing victory
	 * state for
	 */
	//private static boolean recycleVictory = false;
	/** A count of the number of frames the trash bin has been in glowing victory state for*/
	private int trashGlow = 1;
	/**
	 * A count of the number of frames the recycle bin has been in glowing victory
	 * state for
	 */
	private int recycleGlow = 1;

	/** The Crab, currently the only Animal in the game. */
	private Animal crab;
	/** Every animal in the world. Currently only the crab. */
	private HashSet<Animal> animals;
	/** HashSet of all of the current Litter objects in the game */
	private HashSet<Litter> litterSet = new HashSet<Litter>();
	/** HashSet where every element is an ArrayList containing the x and y coordinates of the Litter objects, as well as the imgID and LitterType to send to View */
	private HashSet<ArrayList<Integer>> litterAttrSet = new HashSet<ArrayList<Integer>>();
	
	/** The horizontal speed of the Crab */
	private int animalXIncr;
	/** The vertical speed of the Crab */
	private int animalYIncr;

	/**
	 * Whether the player is allowed to move this frame. Set to false under specific
	 * circumstances, i.e. when colliding with an Animal
	 */
	private boolean playerMove = true;

	/** The current game score */
	private int score = 0;

	/** The last Litter to be picked up by the {@link #player} */
	private Litter pickedUp;
	/** ArrayList of Litter imgID and LitterType of Model.pickedUp Litter attribute to send to View */
	ArrayList<Integer> pickedUpAttr = new ArrayList<Integer>();
	/** The last Litter to be picked up by an {@link #animals animal} */
	private Litter animalEatenLitter;
	/** Contains plant objects **/
	private ArrayList<Plant> plants = new ArrayList<Plant>();

	/** Random index of next plant **/
	private int randPlant = (int) Math.floor(Math.random() * 4);


	/** Boolean variable that represents whether the player has planted the plant that despawns in the tutorial */
	private boolean tutorialPlantGrown;
	/** Boolean variable that represents whether the animal has eaten the Litter in the tutorial */
	private boolean tutorialAnimalAteLitter;
	/** Boolean that represents whether the arrow key prompt should be shown on screen. */ 
	private boolean tutorialArrowKeyPrompt;
	/** Boolean that represents whether or not the Player is hovering, but not picking up a Litter object */
	private boolean tutorialHoverLitter;
	/**onscreen river**/
	River river;
	/**Boolean value to represent if the game is in progress or over */
	private boolean running = true;
	/** The time in milliseconds that the game has begun */
	private long startTime = -1;
	/** How many milliseconds the game should last */
	private int endTimeMilli = 6*10000;

	/**
	 * Constructor for the Model. It creates a new animal and initializes a hashset
	 * of animals just in case more than one animal is wanted in the game. Then the
	 * animal is added to the hashset. Adds 4 Plants to the screen as well.
	 *
	 * 
	 * 
	 * @param width
	 *            Width of the model.
	 * @param height
	 *            Height of the model.
	 * @return New Model object with the specified dimensions.
	 * 
	 */
	public Model(int width, int height) {
		this.crab = new Animal();
		animals = new HashSet<Animal>();
		animals.add(crab);
		this.HEIGHT = height;
		this.WIDTH = width;
		int count = 0;
		river = new River(WIDTH - 200, 0, WIDTH, HEIGHT);
		//fills plant array
		for(int i = 0; i < 4; i++)
		{
			plants.add(new Plant(plantHealth, WIDTH - (WIDTH/3), 50+(WIDTH / 90) + count));//sets location of plants

			count = count + 200;
		}

		this.resetEverything();
	}
	/**
	 * Returns the tutorialHoverLitter boolean of Model. 
	 * 
	 * @param
	 * @return True if the Player is hovering, but not picking up a Litter object, false otherwise. 
	 */
	public boolean isHoverLitter() {
		return tutorialHoverLitter;
	}

	/** Returns the tutorialArrowKeyPrompt
	 * 
	 * @param
	 * @return True if the player hasn't moved and the arrow key prompt needs to be shown, false otherwise. 
	 */
	public boolean isArrowKeyPrompt() {
		return tutorialArrowKeyPrompt;
	}

	
	/**
	 * Returns the ArrayList<Integer> corresponding to the Litter object of the player's most recently picked up Litter object
	 * 
	 * @param
	 * @return ArrayList<Integer> corresponding to the Litter object of the player's most recently picked up Litter object
	 */
	public ArrayList<Integer> getPickedUpAttr() {
		return pickedUpAttr;
	}

	
	public HashSet<ArrayList<Integer>> getLitterAttrSet(){
		return this.litterAttrSet;
	}
	
	/**Returns a boolean depending on whether or not the player is currently holding a Litter object that needs to be disposed of. 
	 * 
	 * @param None
	 * @return True if the player is holding a piece of Litter, false otherwise. 
	 */
	public boolean isHasLitter() {
		return hasLitter;
	}
	
	/**
	 * Sets the hasLitter boolean of the model 
	 * 
	 * @param hasLitter The boolean variable hasLitter will be set to. 
	 */
	public void setHasLitter(boolean hasLitter) {
		this.hasLitter = hasLitter;
	}
	
	/**
	 * Returns the current state of the tutorial, more specifically which stage of the tutorial the player is on. 
	 * If the player is finished the tutorial, this method will simply return the last stage of the tutorial. 
	 * 
	 * @return
	 */
	public TutorialState getTutorialState() {
		return tutorialState;
	}

	public void setTutorialState(TutorialState tutorialState) {
		this.tutorialState = tutorialState;
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
	 * Gets the Litter most recently picked up by the Player. This will either be
	 * the Litter object the player is currently holding, or the last Litter object
	 * the player picked up if they are not currently holding one.
	 * 
	 * @param None.
	 * @return Litter object most recently picked up by the Player
	 */
	public Litter getPickedUpLitter() {
		return this.pickedUp;
	}

	/**
	 * Gets whether the space key is pressed down.
	 * 
	 * @return Whether the space key is pressed down.
	 */
	public boolean getSpacePressed() {
		return this.spacePressed;
	}
	/**
	 * Gets the {@link Litter} most recently eaten by an {@link Animal}.
	 * 
	 * @return The {@link Litter} most recently eaten by an {@link Animal}.
	 */
	public Litter getAnimalEatenLitter() {
		return this.animalEatenLitter;
	}

	/**
	 * Gets the {@link #rBin recycle bin}.
	 * 
	 * @return The {@link #rBin recycle bin}.
	/** Gets the {@link #rBin recycle bin}.
	 *  @return The {@link #rBin recycle bin}.
	 */
	public Receptacle getRBin() {
		return rBin;
	}

	/**
	 * Gets the {@link #tBin trash bin}.
	 * 
	 * @return The {@link #tBin trash bin}.
	 */
	public Receptacle getTBin() {
		return tBin;
	}

	/**
	 * Gets the {@link #trashVictory boolean}.
	 * 
	 * @return The {@link #trashVictory boolean}.
	 */
	public boolean getTrashVictory() {
		return trashVictory;
	}

	/**
	 * Gets the {@link #recycleVictory boolean}.
	 * 
	 * @return The {@link #recycleVictory boolean}.
	 */
	public boolean getRecycleVictory() {
		return recycleVictory;
	}

	/**
	 * Checks the current state of the game, and calls the appropriate updateModel function depending on whether the game is currently
	 * in tutorial or regular mode
	 *  
	 *  @param
	 *  @return
	 *   */
	public void updateModel(long num) {
		checkPlayerAnimalCollision(num);
		if(!this.gamePhase.isPlayable()) return;

		if(playerMove) this.player.move();
		this.checkCollision();
		checkCollision();
		if(trashVictory && ((trashGlow++)% 14 < 1)) {
			trashVictory = false;
		}
		if (recycleVictory && ((recycleGlow++) % 14 < 1)) {
			recycleVictory = false;
		}
		
		if(gamePhase == GamePhase.TUTORIAL) {
			checkTutorialStates();
		}else if(player.getHealth() == 0 || crab.getHealth() == 0) {
			gamePhase = GamePhase.GAME_END;
		}
		else {
			updatingAnimalLocation();
			checkPlants();
			if(startTime != -1 && (System.currentTimeMillis()) - startTime >= endTimeMilli) {
				this.startEndGame();
			}
		}
	}

	/**
	 * Checks the current state of the tutorial, and calls tutorial events and changes tutorial states as the player progresses through the tutorial.
	 * 
	 * @param
	 * @return 
	 */
	public void checkTutorialStates() {
		switch(tutorialState) {
		case SPAWNTRASH:
			//
			spawnLitter(LitterType.TRASH);
			this.tutorialState = TutorialState.SIGNALTRASH;
			break;
		case SIGNALTRASH:
			if(player.getXLocation() != 240 || player.getYLocation() != 240)
				this.tutorialArrowKeyPrompt = false;
			if(hasLitter)
				this.tutorialState = TutorialState.SIGNALTRASHCAN;
			break;
		case SIGNALTRASHCAN:
			if(trashVictory) 
				this.tutorialState = TutorialState.SPAWNRECYCLABLE;
			break;
		case SPAWNRECYCLABLE:
			spawnLitter(LitterType.RECYCLABLE);
			this.tutorialState = TutorialState.SIGNALRECYCLABLE;
			break;
		case SIGNALRECYCLABLE:
			if(hasLitter)
				this.tutorialState = TutorialState.SIGNALRECYCLINGBIN;
			break;
		case SIGNALRECYCLINGBIN:
			if(recycleVictory)
				this.tutorialState = TutorialState.DAMAGEPLANT;
			break;
		case DAMAGEPLANT:
			this.damagePlant();
			if(plants.get(this.randPlant).getHealth() == 0) {
				this.tutorialState = TutorialState.SIGNALPLANT;
			}
			break;
		case SIGNALPLANT:
			if(this.tutorialPlantGrown) {
				spawnLitter(LitterType.RECYCLABLE);
				this.tutorialState = TutorialState.CRABEATLITTER;
			}
			break;
		case CRABEATLITTER:
			if(!this.tutorialAnimalAteLitter)
				updatingTutorialAnimalLocation();
			else
				this.startNormal();
			//decrease score, show animal sick etc. 
			
			break;
			
			
			
		}
	}
	
	/**
	 * Method called when the space key is pressed. Sets the spacePressed boolean
	 * value to true;
	 * 
	 * @param None.
	 * @return None.
	 * 
	 */
	public void spaceKeyPressed() {
		this.spacePressed = true;
	}


	/**
	 * Method called when the space key is released. Sets the spacePressed boolean
	 * value to false;
	/** Sets {@link #spacePressed} to false, and exits the title screen if necessary.
	 * 
	 * @param None.
	 * @return None.
	 * 
	 */
	public void spaceKeyReleased() {
		this.spacePressed = false;
		if(this.gamePhase == GamePhase.TITLE_SCREEN) {
			this.startTutorial();
		}
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
		System.out.println("Crab x: " + crab.getXLocation() + "Crab y: " + crab.getYLocation());
		if (crab.getXLocation() <= 0 && crab.getDirection() == Direction.WEST) { // when the left wall is hit
			crab.setDirection(Direction.EAST);
		} else if (crab.getXLocation() >= WIDTH - 600 && crab.getDirection() == Direction.EAST) { // when the right wall
			// is hit
			crab.setDirection(Direction.WEST);
		}

		else if (crab.getYLocation() <= 0 && crab.getDirection() == Direction.NORTH) { // when the top wall is hit
			crab.setDirection(Direction.SOUTH);
		} else if (crab.getYLocation() >= HEIGHT - 100 && crab.getDirection() == Direction.SOUTH) { // when the bottom
			// wall is hit
			crab.setDirection(Direction.NORTH);
		}

		else if (crab.getXLocation() >= WIDTH - 600 && crab.getDirection() == Direction.NORTHEAST) { // when the right
			// wall is hit
			crab.setDirection(Direction.NORTHWEST);
		} else if (crab.getXLocation() >= WIDTH - 600 && crab.getDirection() == Direction.SOUTHEAST) { // when the right
			// wall is hit
			crab.setDirection(Direction.SOUTHWEST);
		}

		else if (crab.getYLocation() <= 0 && crab.getDirection() == Direction.NORTHEAST) { // when the top wall is hit
			crab.setDirection(Direction.SOUTHEAST);
		} else if (crab.getYLocation() <= 0 && crab.getDirection() == Direction.NORTHWEST) { // when the top wall is hit
			crab.setDirection(Direction.SOUTHWEST);
		}

		else if (crab.getYLocation() >= HEIGHT - 100 && crab.getDirection() == Direction.SOUTHEAST) { // when the bottom
			// wall is hit
			crab.setDirection(Direction.NORTHEAST);
		} else if (crab.getYLocation() >= HEIGHT - 100 && crab.getDirection() == Direction.SOUTHWEST) { // when the
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

	public void updatingTutorialAnimalLocation() {
		crab.setYLocation(crab.getYLocation() + 10);
	}
	/**
	 * Method that updates the x and y coordinates of the crab depending on its
	 * current direction.
	 * 
	 * @param empty
	 * @return empty
	 */
	public void updatingAnimalLocation() {
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
		if (plants.get(randPlant).getHealth() > 0) {
			plants.get(randPlant).health = plants.get(randPlant).health - plantDamage;
		}
		else if(plants.get(randPlant).getHealth() == 0)
		{
			setRandPlant();
		}
	}


	/**
	 * Method called to damage a specific plant by the plantdamage integer value
	 * 
	 * @param i The index in the plant arrayList of the plant to be damaged. 
	 * @return None. 
	 */
	public void damagePlant(int i) {
		if(plants.get(i).getHealth() > 0)
			plants.get(i).health -= plantDamage;
	}
	
	/**
	 * Method called to set randPlant index
	 * 
	 * @param
	 * @return
	 */
	public void setRandPlant()
	{
		this.randPlant = (int) Math.floor(Math.random() * 4);
	}

	/**
	 * Method called to return randPlant index
	 * 
	 * @param
	 * @return
	 */
	public int getRandPlant() {
		return randPlant;
	}

	/**
	 * Method called to return plant array
	 * 
	 * @param
	 * @return plants the array of plants
	 */
	public ArrayList<Plant> getPlants()
	{
		return plants;
	}
	/**
	 * Method called to return river
	 * 
	 * @param
	 * @return river object that represents in game river
	 */
	public River getRiver()
	{
		return river;
	}
	
	public void floodRiver()
	{
		if(river.getXLocation() > WIDTH - 800)
			river.addXLocation(-5);
	}
	
	public void recedeRiver()
	{
		if(river.getXLocation() < WIDTH - 200)
			river.addXLocation(5);
	}

	public void checkPlants()
	{
		int sum = 0;
		for(Plant plant: plants)
		{	
			sum = sum + plant.health;

		}
		if(sum == 0)
		{
			floodRiver();
		}
		else if(sum > 0)
		{
			recedeRiver();
		}
	}

	
	/**
	 * Generates a new Litter object with random x and y coordinates, as well as
	 * generates a random imgID for the object.
	 * 
	 * @param 
	 * @return the new Litter object created.
	 * 
	 */
	public Litter spawnLitter() {
		Random r = new Random();
		Litter l = new Litter();
		l.setType(LitterType.randomLitter());
		int litterXCoord = r.nextInt((WIDTH - l.getWidth()) - (rBin.getXLocation() + rBin.getWidth()))
				+ rBin.getXLocation() + rBin.getWidth();// generates random coordinates
		int litterYCoord = r.nextInt((HEIGHT - l.getHeight()));
		l.setXLocation(litterXCoord);//
		l.setYLocation(litterYCoord);
		l.setImgID(Math.abs(r.nextInt()));
		this.litterSet.add(l);// Adds them to hashset of litter, prevents exact duplicates in terms of coordinates.
		ArrayList<Integer> litterAttr = new ArrayList<Integer>();
		litterAttr.add(l.getXLocation());
		litterAttr.add(l.getYLocation());
		litterAttr.add(l.getImgID());
		litterAttr.add(l.getType().getID());
		this.litterAttrSet.add(litterAttr);
		System.out.println(l);
		return l;
	}

	/**
	 * A public version of {@link #checkCollision} only for use by the
	 * {@link ModelTest} class.
	 * 
	 * @see #checkCollision
	 * @see ModelTest
=======
	/**
	 * Method that spawns Litter for tutorial purposes. Spawns the Litter at a set location with the specified type. 
	 * 
	 * @param lt The litterType of the new Litter object
	 * @return The new Litter object spawned on the map. 
	 */
	public Litter spawnLitter(LitterType lt) {
		Random r = new Random();
		Litter l = new Litter();
		l.setType(lt);
		l.setXLocation(360);
		l.setYLocation(480);
		l.setImgID(Math.abs(r.nextInt()));
		this.litterSet.add(l);
		this.litterAttrSet.add(getLitterAttr(l));
		return l;
	}
	/** A public version of {@link #checkCollision} only for use by the {@link ModelTest} class.
	 *  @see #checkCollision
	 *  @see ModelTest
>>>>>>> master
	 */
	public boolean testCheckColl() {
		return checkCollision();
	}

	private boolean checkPlayerAnimalCollision(long num) {
			if (this.player.getCollidesWith(this.crab)) { //this checks if the player has collided with the crab
			if(num % 2 == 0 && player.getStatus() == PlayerStatus.IDLE) {
			player.loseHealth();
			}else if(num % 3 == 0 && player.getStatus() == PlayerStatus.WALKING) {
				player.loseHealth();
			}
			//the if statements determine the direction the crab should move depending on the player
			playerMove = false; //this tells the player to stop moving so it cannot go through the crab
			animalXIncr = 6; //this changes the speed of the crab when there is a collision between the player
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
		
		return false;
	}

	/**
	 * Method that deals with all the various collisions in the game. If the player
	 * collides with the crab, the player is slowed down, the crab goes in the same
	 * direction as the player, and the score is decreased. In addition, the crab
	 * speeds up while the player is colliding with it to give it the effect that it
	 * is scared. If the crab collides with a piece of trash or recycling, the piece
	 * of trash or recycling is removed and the score is decreased. If a Player does
	 * not have Litter such that Player.hasLitter is false, this method will check
	 * for collisions with Litter object on the ground If a Player has a Litter
	 * object such that Player.hasLitter is true, this method will check if the
	 * Player is colliding with a Receptacle, check if that Receptacle is the
	 * correct one to deposit the current Litter in, and call the appropriate
	 * methods to deposit Litter if it is. Also checks if Player and any of the
	 * Plants on screen are colliding. If they are and the plant has no health, the
	 * appropriate methods are called to regrow the Plant.
	 * 
	 * @param empty
	 * @return whether a collision has been detected
	 */
	private boolean checkCollision() {

		if (!hasLitter) {
			for (Litter litter : new HashSet<Litter>(litterSet)) {
				if (litter.getCollidesWith(this.player)) {
					tutorialHoverLitter = true;
					if (spacePressed) {
						tutorialHoverLitter = false;
						this.pickedUp = pickUpLitter(litter);
						System.out.println(this.pickedUp);
						this.pickedUpAttr = new ArrayList<Integer>();
						pickedUpAttr.add(this.pickedUp.getImgID());
						pickedUpAttr.add(this.pickedUp.getType().getID());
					}
					return true;
					
				}

			}
		}

		for (Plant plant : plants) {
			// add and health == 0
			if (plant.health == 0 && plant.getCollidesWith(this.player)) {
				setRandPlant();
				plant.health = 100;
				this.tutorialPlantGrown = true;
				return true;
			}
		}
		
		if(this.player.getCollidesWith(river))
		{
			this.player.setSpeed(5);
		}
		else
		{
			this.player.setSpeed(10);
		}
		
		
		if(this.hasLitter) {
			if(this.player.getCollidesWith(this.tBin) && this.pickedUp.getType() == LitterType.TRASH) {
				this.tBin.takeLitter(this.player, this);
				System.out.println("DEPOSITED TRASH");
				//changeScore(10);
				trashVictory = true;
				return true;
			}
			}	
			if(this.player.getCollidesWith(this.rBin) && this.pickedUp.getType() == LitterType.RECYCLABLE) {
				this.rBin.takeLitter(this.player, this);
				System.out.println("DEPOSITED RECYCLABLE");
				//changeScore(10);
				recycleVictory = true;
				return true;
			}


		animalWallCollision();

		Iterator<Litter> litterIterator = litterSet.iterator();
		while (litterIterator.hasNext()) {
			Litter litter = litterIterator.next();
			for (Animal animal : this.animals) {
				if (litter.getCollidesWith(animal)) {
					if(gamePhase == GamePhase.NORMAL) {
					crab.loseHealth();
					}
					litterAttrSet.remove(getLitterAttr(litter));
					litterIterator.remove();
					this.tutorialAnimalAteLitter = true;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method to change the game's score.
	 * 
	 * @param i
	 *            The amount of points to be added (positive integer) or subtracted
	 *            (negative integer) from the score.
	 * @return None.
	 */

	/**"Picks up" a Litter object the Player is colliding with. Removes the Litter from the Litter hashSet and sets Model.hasLitter to true 
	 * 
	 * @param l The Litter object being picked up 
	 * @return The Litter object being picked up
	 */
	public Litter pickUpLitter(Litter l) {
		System.out.println("Player pick up litter " + l.toString());
		this.hasLitter = true;
		litterAttrSet.remove(getLitterAttr(l));
		litterSet.remove(l);
		return l;
	}
	
	/**
	 * Take a litter object and adds its attributes to an ArrayList of Integers that can be passed on to view 
	 * 
	 * @param l The litter object 
	 * @return An ArrayList of Integers of the Litter object's attributes. 
	 */
	public ArrayList<Integer> getLitterAttr(Litter l){
		ArrayList litterAttr = new ArrayList<Integer>();
		litterAttr.add(l.getXLocation());
		litterAttr.add(l.getYLocation());
		litterAttr.add(l.getImgID());
		litterAttr.add(l.getType().getID());
		return litterAttr;
	}
	
	/** Gets the width of the Model */
	public int getWidth() {
		return WIDTH;
	}

	/** Gets the height of the Model */
	public int getHeight() {
		return HEIGHT;
	}

	/**
	 * Sets the last picked up litter to the parameter
	 * 
	 * @param l
	 *            The new Litter
	/** Gets the current game phase of the Model */
	public GamePhase getGamePhase() {
		return this.gamePhase;
	}

	/** Sets the last picked up litter to the parameter
	 *  @param l The new Litter
	 */
	public void setPickedUpLitter(Litter l) {
		this.pickedUp = l;
	}

	/** Alters the player's velocity, only works in the NORMAL {@link GamePhase}. 
	 *  When outside that phase, does nothing.
	 *  @param ddx The change in x-velocity of the player
	 *  @param ddy The change in y-velocity of the player
	 */
	public void normalAlterPlayerVelocity(int ddx, int ddy) {
		if(this.gamePhase.isPlayable())
			this.getPlayer().alterVelocity(ddx, ddy);
	}

	/** Initializes the title screen */
	public void startTitleScreen() {
		this.resetEverything();
		this.gamePhase = GamePhase.TITLE_SCREEN;
	}

	/** Initializes the tutorial */
	public void startTutorial() {
		this.resetEverything();
		this.gamePhase = GamePhase.TUTORIAL;
		this.tutorialState = TutorialState.SPAWNTRASH;
		this.tutorialPlantGrown = false;
		this.tutorialAnimalAteLitter = false;
		this.tutorialArrowKeyPrompt = false;
		this.tutorialHoverLitter = false;
	}

	/** Moves to the Normal game state (does NOT reset!) */
	public void startNormal() {
		this.gamePhase = GamePhase.NORMAL;
		this.startTime = System.currentTimeMillis();
	}

	/** Moves to the ending game state (does NOT reset!) */
	public void startEndGame() {
		this.gamePhase = GamePhase.GAME_END;
		this.player.stop();
	}

	/** Resets everything to the 'initial game' state.
	 *  Resets the Player position.
	 *  Resets the Crab position.
	 *  Resets all Plants' health.
	 *  Removes all existing Litter */
	public void resetEverything() {
		this.player = new Player(240,240, 165,165);
		this.crabDirection = 3;
		this.score = 0;
		this.pickedUp = null;
		this.animalEatenLitter = null;
		this.animalXIncr = 4;
		this.animalYIncr = 4;
		for(Plant p : this.plants) {
			p.setHealth(100);
		}
		this.randPlant = 0;
		this.crab = new Animal();
		animals = new HashSet<Animal>();
		animals.add(crab);
	}

	/** Method to determine the game's status 
	 *  @return running A boolean which is true if the game is running false otherwise 
	 * */
	public boolean getModelStatus() {
		return running;
	}
	/** Method to determine the game's start time 
	 *  @return startTime A long representing when the game began in milliseconds 
	 * */
	public long getStartTime() {
		return startTime;
	}
	/** Method that returns how long the game should last in milliseconds 
	 *  @return endTime An integer representing the game length in milliseconds 
	 * */
	public int getEndTime() {
		return endTimeMilli;
	}
}
