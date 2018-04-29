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
	Plant [] plants;
	
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
			view.plant0H = model.plants[0].health;
			view.plant1H = model.plants[1].health;
			view.plant2H = model.plants[2].health;
			view.plant3H = model.plants[3].health;
		}
	}
	
	class checkPlantTask extends TimerTask 
	{
		public void run()
		{
			//view.plants = model.plants;
			checkPlants();
			//for alpha testing
			view.coords = model.coords;
		}
	}
	
	//create task that checks this every one second for revive
	public void checkPlants()
	{
		//int plantNum = model.deletePlant;
		int plantNum = 0;
		switch(plantNum)
		{
		//make first dissappear
		case 0:
			//if dead
			//change model.xloc and model.yloc to playercollision so coordinates dont have to be exact
			//player.getCollidesWith(model.plants[plantNum])
			//^this doesnt work.
			//we have to make sure players xloc and yloc are always being updated and sent
			//to rectangle
			//add this in beta
			System.out.println(model.myPlayer.getCollidesWith(plants[plantNum]));
			//System.out.println(model.myPlayer.getCollidesWith(plants[plantNum]));
			if(model.plants[plantNum].health == 0 && model.myPlayer.getCollidesWith(plants[plantNum])) 
			{
				if(pressP == true)
				{
					//view.revivePlant(plantNum);
					model.plants[plantNum].health = model.plantHealth;
					model.randPlant = (int) Math.floor(Math.random() * 4);
				}
			}
			else if(model.plants[plantNum].health == 0)
			{
				//view.deletePlant(plantNum);
			}
			break;
		//second..etc
		case 1:
			if(model.plants[plantNum].health == 0 && model.myPlayer.xLocation == model.plants[plantNum].xLocation && model.myPlayer.yLocation == model.plants[plantNum].yLocation) 
			{
				if(pressP == true)
				{
					//view.revivePlant(plantNum);
					model.plants[plantNum].health = model.plantHealth;
					model.randPlant = (int) Math.floor(Math.random() * 4);
				}
			}
			else if(model.plants[plantNum].health == 0)
			{
				//view.deletePlant(plantNum);
			}
			break;
		case 2:
			if(model.plants[plantNum].health == 0 && model.myPlayer.xLocation == model.plants[plantNum].xLocation && model.myPlayer.yLocation == model.plants[plantNum].yLocation) 
			{
				if(pressP == true)
				{
					//view.revivePlant(plantNum);
					model.plants[plantNum].health = model.plantHealth;
					model.randPlant = (int) Math.floor(Math.random() * 4);
				}
			}
			else if(model.plants[plantNum].health == 0)
			{
				//view.deletePlant(plantNum);
			}
			break;
		case 3:
			if(model.plants[plantNum].health == 0 && model.myPlayer.xLocation == model.plants[plantNum].xLocation && model.myPlayer.yLocation == model.plants[plantNum].yLocation) 
			{
				if(pressP == true)
				{
					//view.revivePlant(plantNum);
					model.plants[plantNum].health = model.plantHealth;
					model.randPlant = (int) Math.floor(Math.random() * 4);
				}
			}
			else if(model.plants[plantNum].health == 0)
			{
				//view.deletePlant(plantNum);
			}
			break;
		}
	}
	
	// run the simulation
	public void start() {
		model = new Model(crab);
		this.plants = model.plants;
		view = new View(crab, plants);
		view.setKeyListener(this);
		
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