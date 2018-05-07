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

/** Manages interfacing {@link View} and {@link Model}, as well as managing timed loops. */
public class Controller implements KeyListener {
	/** The width of the world in <em>world</em> coordinates */
	public static final int WORLD_WIDTH = 1000;
	/** The height of the world in <em>world</em> coordinates */
	public static final int WORLD_HEIGHT = 1000;
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

	/** The Action to run every frame. Simply calls the {@link #step} method. */
	private final Action stepAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			step();
		}
	};

	/** Updates the Model and then updates the View to reflect the new properties of the Model. */
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
	
	/** When run, simply calls the Model's {@link Model#damagePlant} method. */
	class damagePlantTask extends TimerTask 
	{
		/** Simply calls the Model's {@link Model#damagePlant} method. */
		public void run()
		{
			model.damagePlant();

		}
	}
	
	/** Starts the simulation */
	public void start() {
		view = new View();
		view.setKeyListener(this);
		model = new Model();
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				stepTimer = new Timer(DRAW_DELAY, stepAction);
				stepTimer.start();
				taskTimer.scheduleAtFixedRate(new damagePlantTask(),500,1000);//damages plants every ten seconds
				trashTimer.scheduleAtFixedRate(new TrashTask(), 0, 10000);
			}
		});
	}

	/** Changes the player's velocity according to the arrow keys being pressed, or marks that the space key is pressed down.
	 *  @param e The KeyEvent containing the key that way pressed. */
	@Override
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

	/** Changes the player's velocity according to the arrow keys being released, or mark that the space key is no longer pressed down.
	 *  @param e The KeyEvent containing the key that was released. */
	@Override
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

	/** Does nothing
	 *  @param e Ignored
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	/**TimerTask subclass that handles the spawning of Litter object around the map at the set interval it was scheduled at by calling the appropriate Model to View communication.
	 * 
	 * @author Juan Villacis
	 *
	 */
	class TrashTask extends TimerTask{
		/**Calls the view method that adds a Litter object to the HashMap of rendered Litter object. Its parameter is the Litter object the model method returns after creating a new Model object and setting its logical attributes. 
		 * 
		 * 
		 */
		public void run() {
			
			view.addLitter(model.spawnLitter());
			
		
		
			
		}
	}
}
