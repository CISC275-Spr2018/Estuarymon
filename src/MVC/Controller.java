package MVC;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.TimerTask;

import javax.jws.WebParam.Mode;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;

import MapObjects.Animal;
import MapObjects.Plant;
import Player.Player;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/** Manages interfacing {@link View} and {@link Model}, as well as managing timed loops. */
public class Controller implements KeyListener {
	/** The instance of {@link Model}. */
	private Model model;
	/** The instance of {@link View}. */
	private View view;
	/** The main loop timer */
	private Timer stepTimer;
	
	/** A timer used to damage the plants, runs {@link damagePlantTask}*/
	private java.util.Timer taskTimer = new java.util.Timer();
	/** A timer used to spawn litter, runs {@link TrashTask} */
	private java.util.Timer trashTimer = new java.util.Timer();

	/** The delay between game frames */
	private static final int DRAW_DELAY = 1000/30; // 30fps

	/** The stage of counting "cheat" typing */
	private int cheatState = 0;
	
	/** The Action to run every frame. Simply calls the {@link #step} method. */
	private final Action stepAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			step();
		}
	};
	/**
	 * Method that updates the Model and changes the view based on the Model.
	 * 
	 * @param None
	 * @return None.
	 */
	private void step() {
		// increment the x and y coordinates, alter direction if necessary
		model.updateModel();
		view.update(		
			model.getGamePhase(),
			model.getPlayer().getXLocation(),
			model.getPlayer().getYLocation(),
			model.getPlayer().getDirection(),
			model.getPlayer().getStatus(),
			model.getAnimal().getXLocation(),
			model.getAnimal().getYLocation(),
			model.getPickedUpAttr(),
			model.isHasLitter(),
			model.getScore(),
			model.getPlants(),model.getTrashVictory(),model.getRecycleVictory(),
			model.getRiver(),
			model.getTutorialState(),
			model.getLitterAttrSet(),
			model.isArrowKeyPrompt(),
			model.isHoverLitter());
	}
	
	/**
	 * TimerTask that handles damaging plants in a certain interval. 
	 * 
	 * @author Hunter Suchyj
	 *
	 */
	class damagePlantTask extends TimerTask 
	{
		/**
		 * Method that calls the model method to damage the plant. 
		 * 
		 * @param None
		 * @return None. 
		 */
		public void run()
		{
			if(model.getGameState()==GameState.REGULARGAME)
				model.damagePlant();

		}
	}
	
	/**
	 * Method that creates a new Model and View, and starts the game. Also creates taskTimers for spawning Litter and damaging Plants. 
	 * 
	 * @param None. 
	 * @return None. 
	 */
	public void start() {
		view = new View();
		view.setKeyListener(this);
		model = new Model(View.WORLD_WIDTH, View.WORLD_HEIGHT);
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				stepTimer = new Timer(DRAW_DELAY, stepAction);
				stepTimer.start();
				taskTimer.scheduleAtFixedRate(new damagePlantTask(),500,1000);//damages plants every ten seconds
				trashTimer.scheduleAtFixedRate(new TrashTask(), 0, 6000);
			}
		});
	}

	/** Changes the player's velocity according to the arrow keys being pressed, or marks that the space key is pressed down.
	 * If spacebar is pressed trash is collected. If 1 is press game is saved. If 2 is pressed game is loaded.
	 *  @param e The KeyEvent containing the key that way pressed. */
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_UP:
			model.normalAlterPlayerVelocity(0, -1);
			break;
		case KeyEvent.VK_DOWN:
			model.normalAlterPlayerVelocity(0, 1);
			break;
		case KeyEvent.VK_RIGHT:
			model.normalAlterPlayerVelocity(1, 0);
			break;
		case KeyEvent.VK_LEFT:
			model.normalAlterPlayerVelocity(-1, 0);
			break;
		case KeyEvent.VK_SPACE:
			model.spaceKeyPressed();
			break;
		case KeyEvent.VK_1:
			saveGame();
			break;
		case KeyEvent.VK_2:
			loadGame();
			break;
		}
	}
	
	/**
	 * Method that serializes the state of model to a serial file.
	 * 
	 * @param None. 
	 * @return None. 
	 */
	public void saveGame()
	{
		try
		{
			FileOutputStream fos = new FileOutputStream("saveGame.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(model);
			oos.close();
		}
		
		catch (Exception ex)
		{
			System.out.println("Exception thrown during test: " + ex.toString());
		}
	}
	
	
	/**
	 * Method that loads the serializable file and changes the attributes in model.
	 * 
	 * @param None. 
	 * @return None. 
	 */
	 
	public void loadGame()
	{
		try
		{
			FileInputStream fis = new FileInputStream("saveGame.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			model = (Model) ois.readObject();
			ois.close();
		}
		catch (Exception ex)
		{
			System.out.println("Exception thrown during test: " + ex.toString());
		}
	}

	
	/** Changes the player's velocity according to the arrow keys being released, or mark that the space key is no longer pressed down.
	 *  @param e The KeyEvent containing the key that was released. */
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		switch(key) {
		case KeyEvent.VK_UP:
			model.normalAlterPlayerVelocity(0, 1);
			break;
		case KeyEvent.VK_DOWN:
			model.normalAlterPlayerVelocity(0, -1); 
			break;
		case KeyEvent.VK_RIGHT:
			model.normalAlterPlayerVelocity(-1, 0);
			break;
		case KeyEvent.VK_LEFT:
			model.normalAlterPlayerVelocity(1, 0);
			break;
		case KeyEvent.VK_SPACE:
			model.spaceKeyReleased();
			break;
		}
	}

	/** 
	 *  @param e Ignored
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		char key = e.getKeyChar();
		
		if(this.cheatState != 5) {
			switch(key) {
			// Typing "cheat"
			case 'c':
				if(this.cheatState == 0) this.cheatState = 1;
				else this.cheatState = 0;
				break;
			case 'h':
				if(this.cheatState == 1) this.cheatState = 2;
				else this.cheatState = 0;
				break;
			case 'e':
				if(this.cheatState == 2) this.cheatState = 3;
				else this.cheatState = 0;
				break;
			case 'a':
				if(this.cheatState == 3) this.cheatState = 4;
				else this.cheatState = 0;
				break;
			case 't':
				if(this.cheatState == 4) this.cheatState = 5;
				else this.cheatState = 0;
				break;
			default:
				this.cheatState = 0;
			}
		} else {
			// Actually run a cheat
			System.out.println("Running cheat code "+key);
			this.cheatState = 0;

			switch(key) {
			case 'T':
				this.model.startTitleScreen();
				break;
			case 't':
				this.model.startTutorial();
				break;
			case 'n':
				this.model.startNormal();
				break;
			case 'r':
				this.model.resetEverything();
				break;
			case 'e':
				this.model.startEndGame();
				break;
			default:
				System.out.println("Unrecognised cheat.");
			}
		}
	}
	
	
	/**
	 * TimerTask subclass that handles the spawning of Litter object around the map at the set interval it was scheduled at by calling the appropriate Model to View communication.
	 * 
	 *
	 */
	class TrashTask extends TimerTask{
		/**
		 * Calls the view method that adds a Litter object to the HashMap of rendered Litter object. Its parameter is the Litter object the model method returns after creating a new Model object and setting its logical attributes. 
		 * 
		 * @param None
		 * @return None
		 */
		public void run() {
			if(model.getGameState()==GameState.REGULARGAME)
				model.spawnLitter();
			
		
		
			
		}
	}
	
}
