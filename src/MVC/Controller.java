package MVC;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;

import javax.jws.WebParam.Mode;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;

import MapObjects.Animal;
import MapObjects.Plant;
import Player.Player;

/**
 * Controller class that handles creating Model and View, as well as communication needed between the two. 
 * Contains TimerTasks for the various events in the game. 
 * 
 * @author Zack Klodnicki 
 *
 */
public class Controller implements KeyListener {
	/** The Model of the game */
	private Model model;
	/** The View of the game */
	private View view;
	/** The Timer that handles stepping through the game*/
	private Timer stepTimer;
	/** Timer that calls the appropriate methods to damage plants */
	java.util.Timer taskTimer = new java.util.Timer();
	/** Timer that calls the appropriate methods to spawn Litter */
	java.util.Timer trashTimer = new java.util.Timer();
	
	private static final int DRAW_DELAY = 1000/30; // 30fps

	//for alpha only
	
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
			model.getPlayer().getXLocation(),
			model.getPlayer().getYLocation(),
			model.getPlayer().getDirection(),
			model.getPlayer().getStatus(),
			model.getAnimal().getXLocation(),
			model.getAnimal().getYLocation(),
			model.getPickedUpLitter(),
			model.getPlayer().getHasLitter(),
			model.getAnimalEatenLitter(),
			model.getScore());
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
		model = new Model(View.WORLD_HEIGHT, View.WORLD_WIDTH);
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				stepTimer = new Timer(DRAW_DELAY, stepAction);
				stepTimer.start();
				taskTimer.scheduleAtFixedRate(new damagePlantTask(),500,1000);//damages plants every ten seconds
				trashTimer.scheduleAtFixedRate(new TrashTask(), 0, 10000);
			}
		});
	}
	
	
	@Override
	/**
	 * Method that listens gets keyboard input and calls the appropriate model method depending on what key is pressed.
	 * 
	 * @param e The KeyEvent of the key pressed. 
	 * @return None. 
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_UP:
			model.getPlayer().alterVelocity(0, -1);
			break;
		case KeyEvent.VK_DOWN:
			model.getPlayer().alterVelocity(0, 1);
			break;
		case KeyEvent.VK_RIGHT:
			model.getPlayer().alterVelocity(1, 0);
			break;
		case KeyEvent.VK_LEFT:
			model.getPlayer().alterVelocity(-1, 0);
			break;
		case KeyEvent.VK_SPACE:
			model.spaceKeyPressed();
			break;
		}
	}
	
	
	@Override
	/**
	 * Method called when a key is released and calls the appropriate Model methods depending on what key was pressed. 
	 * 
	 * @param e KeyEvent that corresponds to the key released.
	 * @return None. 
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		switch(key) {
		case KeyEvent.VK_UP:
			model.getPlayer().alterVelocity(0, 1);
			break;
		case KeyEvent.VK_DOWN:
			model.getPlayer().alterVelocity(0, -1);
			break;
		case KeyEvent.VK_RIGHT:
			model.getPlayer().alterVelocity(-1, 0);
			break;
		case KeyEvent.VK_LEFT:
			model.getPlayer().alterVelocity(1, 0);
			break;
		case KeyEvent.VK_SPACE:
			model.spaceKeyReleased();
			break;
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
			
			view.addLitter(model.spawnLitter());
			
		
		
			
		}
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
