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
		}
	}
	
	class checkPlantTask extends TimerTask 
	{
		public void run()
		{
			checkPlants();
		}
	}
	
	//create task that checks this every one second for revive
	public void checkPlants()
	{
		int plantNum = model.deletePlant;
		switch(plantNum)
		{
		//make first dissappear
		case 0:
			//if dead
			//change model.xloc and model.yloc to playercollision so coordinates dont have to be exact
			//player.getCollidesWith(plants[plantNum])
			if(model.plants[plantNum].health == 0 && model.xLoc == model.plants[plantNum].xLocation && model.yLoc == model.plants[plantNum].yLocation) 
			{
				view.revivePlant(plantNum);
				model.plants[plantNum].health = 100;
				model.randPlant = (int) Math.floor(Math.random() * 4);
			}
			else if(model.plants[plantNum].health == 0)
			{
				view.deletePlant(plantNum);
			}
			break;
		//second..etc
		case 1:
			if(model.plants[plantNum].health == 0 && model.xLoc == model.plants[plantNum].xLocation && model.yLoc == model.plants[plantNum].yLocation) 
			{
				view.revivePlant(plantNum);
				model.plants[plantNum].health = 100;
				model.randPlant = (int) Math.floor(Math.random() * 4);
			}
			else if(model.plants[plantNum].health == 0)
			{
				view.deletePlant(plantNum);
			}
			break;
		case 2:
			if(model.plants[plantNum].health == 0 && model.xLoc == model.plants[plantNum].xLocation && model.yLoc == model.plants[plantNum].yLocation) 
			{
				view.revivePlant(plantNum);
				model.plants[plantNum].health = 100;
				model.randPlant = (int) Math.floor(Math.random() * 4);
			}
			else if(model.plants[plantNum].health == 0)
			{
				view.deletePlant(plantNum);
			}
			break;
		case 3:
			if(model.plants[plantNum].health == 0 && model.xLoc == model.plants[plantNum].xLocation && model.yLoc == model.plants[plantNum].yLocation) 
			{
				view.revivePlant(plantNum);
				model.plants[plantNum].health = 100;
				model.randPlant = (int) Math.floor(Math.random() * 4);
			}
			else if(model.plants[plantNum].health == 0)
			{
				view.deletePlant(plantNum);
			}
			break;
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
		case KeyEvent.VK_J:
			//view.setAnimation(Animation.JUMP);
			break;
		case KeyEvent.VK_F:
			//view.setAnimation(Animation.FIRE);
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
			//System.out.println("Key was released");
			view.setAnimation(Animation.JUMP);
			//System.out.println("Animation set to Jump");
			break;
		case KeyEvent.VK_F:
			//System.out.println("Key was released");
			view.setAnimation(Animation.FIRE);
			//System.out.println("Animation set to fire");
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
			
		
			//spawns the litter on the screen
			
		}
	}
}
