import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;


public class Controller implements KeyListener {
	private Model model;
	private View view;
	private Timer stepTimer;
	private Animal crab = new Animal();
	
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
		view.update(model.getX(), model.getY(), model.getDirect());
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
		view = new View(crab);
		view.setKeyListener(this);
		model = new Model(view.getWidth(), view.getHeight(), view.getImageWidth(), view.getImageHeight(), crab);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				stepTimer = new Timer(DRAW_DELAY, stepAction);
				stepTimer.start();
				taskTimer.scheduleAtFixedRate(new damagePlantTask(),500,1000);//damages plants every ten seconds
				taskTimer.scheduleAtFixedRate(new checkPlantTask(),500,100);//evaluates plants with player every second
				trashTimer.scheduleAtFixedRate(new TrashTask(), 0, 6000);
			}
		});
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_UP:
			model.setAttributes(1, Direction.NORTH, 0, 10);
			view.setAnimation(Animation.WALKING);
			break;
		case KeyEvent.VK_DOWN:
			model.setAttributes(2, Direction.SOUTH, 0, 10);
			view.setAnimation(Animation.WALKING);
			break;
		case KeyEvent.VK_RIGHT:
			model.setAttributes(3, Direction.EAST, 10, 0);
			view.setAnimation(Animation.WALKING);
			break;
		case KeyEvent.VK_LEFT:
			model.setAttributes(4, Direction.WEST, 10, 0);
			view.setAnimation(Animation.WALKING);
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
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_LEFT:
			model.stop();
			view.setAnimation(Animation.IDLE);
			break;
		case KeyEvent.VK_J:
			view.setAnimation(Animation.JUMP);
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
	
	class TrashTask extends TimerTask{
		public void run() {
			
			if(Math.random() < 0.5) { //coin flip as to whether it will be trash or recyclable
				Recyclable newLitter = new Recyclable();
				model.genLitterCords(newLitter);
				view.setLitterImage(newLitter);
				
			}
			else {
				Trash newLitter = new Trash();
				model.genLitterCords(newLitter);
				view.setLitterImage(newLitter);
			}
			
		
		
			
		}
	}
}