
package MVC;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;


import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import MVC.Sprite.ID;
import MapObjects.Litter;
import MapObjects.Plant;
import Player.Direction;
import Player.PlayerStatus;

public class View extends JPanel{
	private final static Dimension  screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
	private final static double screenHeight = screenDimension.getHeight();
	private final static double screenWidth = screenDimension.getWidth();
	JFrame frame = new JFrame();
	private static int playerXLoc = 0; //player x location
	private static int playerYLoc = 0; //player y location
	private static PlayerStatus playerStatus = PlayerStatus.IDLE;
	private static int crabXLoc = 200;
	private static int crabYLoc = 400;
	
	private static Direction playerDirection = Direction.EAST;
	private static final Color BACKGROUND_COLOR = Color.GRAY;
	static int bCount;
	
	private static final int trashImgCount = 2;
	private static final int recImgCount = 2;
	private static final int litterCount = 20;
	private Sprite.ID[] trashImgs = new Sprite.ID[trashImgCount+1];
	private Sprite.ID[] recyclableImgs = new Sprite.ID[recImgCount+1];
	private Litter[] litterArr = new Litter[litterCount];
	private Litter pickedUpLitter;
	boolean hasLitter = false;
	static ArrayList<ArrayList<Sprite.ID>> litterImgLists = new ArrayList<ArrayList<Sprite.ID>>();
	static HashMap<Litter, Sprite.ID> litterImgMap = new HashMap<Litter, Sprite.ID>();

	//these plants vars for alpha testing
	int plant0H;
	int plant1H;
	int plant2H;
	int plant3H;
	String coords = "";
	
	private int score = 0;
	public View() {	
		preloadLitterImgs();

		frame.setFocusable(true);
		frame.setLayout(new GridBagLayout());
		frame.setUndecorated(true);
		frame.add(this);
		frame.setBackground(BACKGROUND_COLOR);
		this.setBackground(BACKGROUND_COLOR);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((int) screenWidth, (int) screenHeight);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
	
	
	public void setKeyListener(KeyListener listener) {
		frame.addKeyListener(listener);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Sprite.incrementFrameCounter();
		drawImage(g, Sprite.ID.BACKGROUND, 0, 0);
		
		

		for(Plant plant : Plant.plants) 
		{
			if(plant.health < 100 && plant.health != 0)
			{
				drawImage(g, Sprite.ID.DECAY_PLANT, plant.getXLocation(), plant.getYLocation());
			}
			else if(plant.health == 100)
			{
				drawImage(g, Sprite.ID.PLANT, plant.getXLocation(), plant.getYLocation());
			}
		}
		
		//traverse through litter set and draw them, had to make a copy of litter set everytime to avoid ConcurrentModificationExceptions.
		for(Map.Entry<Litter, Sprite.ID>entry: new HashMap<Litter,Sprite.ID>(litterImgMap).entrySet()) {
			drawImage(g,entry.getValue(),
				convertDimension(entry.getKey().getXLocation()),
				convertDimension(entry.getKey().getYLocation()));
			
		}

		drawImage(g, Sprite.ID.CRAB, crabXLoc, crabYLoc);
		drawImage(g, getPlayerSprite(), playerXLoc, playerYLoc);
		drawImage(g, Sprite.ID.SCORESTAR, 900, 0);
		drawString(g, Integer.toString(score), 100, 900, 65);
	
		drawImage(g, Sprite.ID.LITTERFRAME,0,0);
		if(hasLitter) {
			drawImage(g,getSpriteID(pickedUpLitter),10,10);
		}

		
		
	}
 //looks like this method just looks at the status of the player 
	private Sprite.ID getPlayerSprite() {
		switch(this.playerStatus) {
			case IDLE:
				switch(this.playerDirection) {
					case NORTH:
					case NORTHEAST: return Sprite.ID.ORC_IDLE_NORTH;
					case EAST:
					case SOUTHEAST: return Sprite.ID.ORC_IDLE_EAST;
					case SOUTH:
					case SOUTHWEST: return Sprite.ID.ORC_IDLE_SOUTH;
					case WEST:
					case NORTHWEST: return Sprite.ID.ORC_IDLE_WEST;
					default:
						throw new RuntimeException("Unknkown player direction "+this.playerDirection);
				}
			case WALKING:
				switch(this.playerDirection) {
					case NORTH: return Sprite.ID.ORC_WALK_NORTH;
					case SOUTH: return Sprite.ID.ORC_WALK_SOUTH;
					case WEST: return Sprite.ID.ORC_WALK_WEST;
					case EAST: return Sprite.ID.ORC_WALK_EAST;
					case NORTHWEST: return Sprite.ID.ORC_WALK_NORTHWEST;
					case NORTHEAST: return Sprite.ID.ORC_WALK_NORTHEAST;
					case SOUTHWEST: return Sprite.ID.ORC_WALK_SOUTHWEST;
					case SOUTHEAST: return Sprite.ID.ORC_WALK_SOUTHEAST;
					default:
									throw new RuntimeException("Unknown player direction "+this.playerDirection);
				}
			default:
				throw new RuntimeException("Unrecognised player status "+this.playerStatus);
		}
	}

	private void drawImage(Graphics g, Sprite.ID s, int world_x, int world_y) {
		g.drawImage(
			Sprite.getImage(s,
			(double) this.getWidth() / Controller.WORLD_WIDTH),
			convertDimension(world_x),
			convertDimension(world_y),
			this);
	}
	
	private void drawString(Graphics g, String word, int width, int XPos, int YPos) {
		g.setFont(new Font("TimesRoman", Font.BOLD, 25));
		int stringLength = (int) g.getFontMetrics().getStringBounds(word, g).getWidth();
		int start = convertDimension(width)/2 - stringLength/2;
		g.setColor(Color.BLACK);
		g.drawString(word, start + convertDimension(XPos), convertDimension(YPos));
	}

	// convertDimension: converts a dimension from world coordinates to pixel coordinates
	private int convertDimension(int world_dimension) {
		return (int) ((double) world_dimension / Controller.WORLD_WIDTH * this.getWidth());
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension parent = this.getParent().getSize();
		if(parent.width > parent.height) {
			return new Dimension(parent.height, parent.height);
		} else {
			return new Dimension(parent.width, parent.width);
		}
	}

	
	/**Updates the View based on parameters given by Model.
	 * Updates the Player's and Crab's x and y location, as well as stops the most recent Litter object the player picked up from being rendered on the ground.
	 * 
	 * @param playerX The Player's X location
	 * @param playerY The Player's Y location
	 * @param dir The current Direction of the Player. 
	 * @param status The Player's current status. 
	 * @param crabX The Animal's X location
	 * @param crabY The Animal's Y location
	 * @param playerPickedUp The most recent Litter object picked up by the Player
	 * @param hasLitter Boolean value representing if the Player is currently holding a Litter object. 
	 * @param animalEatenLitter The most recent Litter object eaten by the animal. 
	 */
	public void update(int playerX, int playerY, Direction dir, PlayerStatus status, int crabX, int crabY,Litter playerPickedUp,boolean hasLitter, Litter animalEatenLitter, int score) {
		//Updating crab and player locations
		playerXLoc = playerX;
		playerYLoc = playerY;
		playerDirection = dir;
		playerStatus = status;

		crabXLoc = crabX;
		crabYLoc = crabY;
		
		this.score = score;
		
		//Remove both litter parameter from HashMap so it does not get painted.
		litterImgMap.remove(playerPickedUp);
		litterImgMap.remove(animalEatenLitter);
		this.pickedUpLitter = playerPickedUp;
		this.hasLitter = hasLitter;
		frame.repaint();
	}
	
	/**Adds a Litter object to the other Litter objects being rendered on the View
	 * 
	 * @param t The Litter object that will be added for rendering.
	 */

	//don't worry about this
//	public void setLitterImage(Litter l) {
//		switch(l.getType()){
//		case TRASH:
//			l.setlitterImage(trashImgs[(int)(Math.random()*trashImgs.length)]);
//			break;
//		case RECYCLABLE:
//			l.setlitterImage(recyclableImgs[(int)(Math.random()*recyclableImgs.length)]);
//		}

	public void addLitter(Litter l) {
		Sprite.ID curSpriteID = getSpriteID(l);
		litterImgMap.put(l, curSpriteID);
		
		
	}
	
	/**Chooses a Sprite ID to represent a Litter object.
	 * The type of Litter image is chosen using the Litter's enum attribute that represents type, specific image chosen is done using the Litter object's imgID
	 * 
	 * @param l the Litter object whose Sprite ID will be chosen.
	 * @return Sprite ID representing the parameter. 
	 */
	public Sprite.ID getSpriteID(Litter l) {
		ArrayList<Sprite.ID> litterImgList = litterImgLists.get(l.getType().getID());
		return litterImgList.get(l.getImgID()%(litterImgList.size()));
	}
	
	
	/**Loads in the different Litter images to be used in the game. 
	 * 
	 */
	public void preloadLitterImgs() {
		ArrayList<Sprite.ID> trashImgList = new ArrayList<Sprite.ID>();
		trashImgList.add(Sprite.ID.BANANAPEEL);
		trashImgList.add(Sprite.ID.APPLE);
		litterImgLists.add(trashImgList);
		
		
		ArrayList<Sprite.ID> recyclableImgList = new ArrayList<Sprite.ID>();
		recyclableImgList.add(Sprite.ID.SODACAN);
		recyclableImgList.add(Sprite.ID.PAPER);
		litterImgLists.add(recyclableImgList);
		
		
	}
	
	/**Reads in an image file, and creates a corresponding BufferedImage
	 * 
	 * @param File name of the image without the file extension (ie: .png)
	 * @return Buffered image corresponding to the image file.
	 * @throws IOException if the image file cannot be found based on the name given. 
	 */
	protected BufferedImage loadImg(String name) {
		try {
			return ImageIO.read(new File("images/MapObjects/"+name+".png"));
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

}


/**
 * View: Contains everything about graphics and images
 * Know size of world, which images to load etc
 *
 * has methods to
 * provide boundaries
 * use proper images for direction
 * load images for all direction (an image should only be loaded once!!! why?)
 **/
