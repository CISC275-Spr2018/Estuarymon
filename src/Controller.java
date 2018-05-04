import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;


public class Controller implements KeyListener {
	public static final int WORLD_WIDTH = 1000;
	public static final int WORLD_HEIGHT = 1000;
	private Model model;
	private View view;
	private Timer stepTimer;
	
	java.util.Timer taskTimer = new java.util.Timer();
	java.util.Timer trashTimer = new java.util.Timer();

	private static final int DRAW_DELAY = 100;

	//for alpha only
	boolean pressP = false;
	
	private final Action stepAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			step();
		}
	};

	private void step() {
		// increment the x and y coordinates, alter direction if necessary
		model.updateModel();
		Player player = model.getPlayer();
		Animal animal = model.getAnimal();
		view.update(
			player.getXLocation(),
			player.getYLocation(),
			player.getDirection(),
			animal.getXLocation(),
			animal.getYLocation());
	}
	
	//plant stuff
	class damagePlantTask extends TimerTask 
	{
		public void run()
		{
			model.damagePlant();
			//show plant health on screen
			view.plant0H = Plant.plants[0].health;
			view.plant1H = Plant.plants[1].health;
			view.plant2H = Plant.plants[2].health;
			view.plant3H = Plant.plants[3].health;
		}
	}
	
	class checkPlantTask extends TimerTask 
	{
		public void run()
		{
			//checkPlants();
			//for alpha testing
			view.coords = model.coords;
		}
	}
	
	
	// run the simulation
	public void start() {
		view = new View();
		view.setKeyListener(this);
		model = new Model();
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				stepTimer = new Timer(DRAW_DELAY, stepAction);
				stepTimer.start();
				taskTimer.scheduleAtFixedRate(new damagePlantTask(),500,1000);//damages plants every ten seconds
				taskTimer.scheduleAtFixedRate(new checkPlantTask(),500,100);//evaluates plants with player every second
				trashTimer.scheduleAtFixedRate(new TrashTask(), 0, 10000);
			}
		});
	}

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
		case KeyEvent.VK_P:
			pressP = true;
			break;
		}
	}

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
		case KeyEvent.VK_P:
			pressP = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	/**TimerTask subclass that handles the spawning of Litter object around the map at the set interval it was scheduled at. 
	 * 
	 * @author Juan Villacis
	 *
	 */
	class TrashTask extends TimerTask{
		/**Method that psuedo-randomly chooses whether the new Litter object will be of Trash or Litter type, and calls the appropriate method in Model to generate its coordinates and in View to set its image.
		 * 
		 * 
		 */
		public void run() {
			
			view.setLitterImage(model.spawnLitter());
			
		
		
			
		}
	}
}
