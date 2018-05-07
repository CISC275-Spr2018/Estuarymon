
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
	/** The dimensions of the computer screen, in pixels. */
	private final static Dimension  screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
	/** The height of the computer screen, in pixels. */
	private final static double screenHeight = screenDimension.getHeight();
	/** The width of the computer screen, in pixels. */
	private final static double screenWidth = screenDimension.getWidth();
	/** The JFrame housing this View. */
	JFrame frame = new JFrame();
	/** The x location of the player in world coordinates */
	private static int playerXLoc = 0;
	/** The y location of the player in world coordinates */
	private static int playerYLoc = 0;
	/** The status of the player, i.e.&nbdp;idle, moving, etc. */
	private static PlayerStatus playerStatus = PlayerStatus.IDLE;
	/** The x location of the crab in world coordinates */
	private static int crabXLoc = 200;
	/** The y location of the crab in world coordinates */
	private static int crabYLoc = 400;
	
	/** The current direction of the player */
	private static Direction playerDirection = Direction.EAST;
	/** The background color of the screen */
	private static final Color BACKGROUND_COLOR = Color.GRAY;
	
	/** The number of distinct trash sprites */
	private static final int trashImgCount = 2;
	/** The number of distinct recyclable sprites */
	private static final int recImgCount = 2;
	/** The number of distinct litter sprites */
	private static final int litterCount = trashImgCount + recImgCount;
	/** The litter currently being help by the player, or null. */
	private Litter pickedUpLitter = null;
	/** Whether the player is holding on to litter */
	private boolean hasLitter = false;
	
	/** A list containing lists of litter sprite ids. Organized into how they may be deposited, i.e. trash vs. recyclable. */
	private static ArrayList<ArrayList<Sprite.ID>> litterImgLists = new ArrayList<ArrayList<Sprite.ID>>();
	/** Contains all Litter objects to be rendered onscreen, maps them to a Sprite.ID. */
	private static HashMap<Litter, Sprite.ID> litterImgMap = new HashMap<Litter, Sprite.ID>();

	/** The current score of the game */
	private int score = 0;

	/** Creates a new View, places it in a new JPanel, arranges everything, and makes it visible. */
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
	
	/** Adds a key listener to the associated JFrame 
	 *  @param listener The listener to add
	 */
	public void setKeyListener(KeyListener listener) {
		frame.addKeyListener(listener);
	}
	
	/** Paints this view
	 * @param g The {@link java.awt.Graphics} object to use for painting
	 */
	public void paint(Graphics g) {
		super.paint(g);
		// Increment animations to the next frame
		Sprite.incrementFrameCounter();
		// Draw the background
		drawImage(g, Sprite.ID.BACKGROUND, 0, 0);
		
		// Draw all plants
		for(Plant plant : Plant.plants) 
		{
			if(plant.health < 100 && plant.health != 0) // If decaying...
			{
				drawImage(g, Sprite.ID.DECAY_PLANT, plant.getXLocation(), plant.getYLocation());
			}
			else if(plant.health == 100) // If fully alive...
			{
				drawImage(g, Sprite.ID.PLANT, plant.getXLocation(), plant.getYLocation());
			}
		}
		
		//traverse through litter set and draw them, had to make a copy of litter set everytime to avoid ConcurrentModificationExceptions.
		for(Map.Entry<Litter, Sprite.ID>entry: new HashMap<Litter,Sprite.ID>(litterImgMap).entrySet()) {
			drawImage(g,entry.getValue(), entry.getKey().getXLocation(), entry.getKey().getYLocation());
		}

		// Draw the crab
		drawImage(g, Sprite.ID.CRAB, crabXLoc, crabYLoc);
		// Draw the player
		drawImage(g, getPlayerSprite(), playerXLoc, playerYLoc);
		// Draw the score
		drawImage(g, Sprite.ID.SCORESTAR, 900, 0);
		drawString(g, Integer.toString(score), 100, 900, 65);
		// Draw the litter in the box
		drawImage(g, Sprite.ID.LITTERFRAME,0,0);
		if(hasLitter) {
			drawImage(g,getSpriteID(pickedUpLitter),10,10);
		}
	}

	/** Determines which {@link Sprite.ID} to use to render the player. Determines this based on the player's {@link #playerStatus status} and {@link #playerDirection direction}.
	 *  @return The appropriate {@link Sprite.ID} to use to render the player
	 */
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
					default: // Unknown player direction
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
					default: // Unknown player direction
						throw new RuntimeException("Unknown player direction "+this.playerDirection);
				}
			default: // Unknown player status
				throw new RuntimeException("Unrecognised player status "+this.playerStatus);
		}
	}

	/** Renders a {@link Sprite.ID} onto the given Graphics by consuming <em>world</em> coordinates. Does all conversions necessary to render at the correct pixel coordinates.
	 *  @param g The {@link Graphics} object to use for painting.
	 *  @param s The {@link Sprite.ID} to use to retrieve the {@link BufferedImage} to draw.
	 *  @param world_x The x-coordinate of the {@link Sprite.ID} to draw, in <em>world</em> coordinates.
	 *  @param world_y The y-coordinate of the {@link Sprite.ID} to draw, in <em>world</em> coordinates.
	 */
	private void drawImage(Graphics g, Sprite.ID s, int world_x, int world_y) {
		g.drawImage(
			Sprite.getImage(s,
			(double) this.getWidth() / Controller.WORLD_WIDTH),
			convertDimension(world_x),
			convertDimension(world_y),
			this);
	}
	
	/** Draws a string centered in the specified horizontal line by consuming <em>world</em> coordinates.
	 *  @param g The {@link Graphics} object to use for painting.
	 *  @param word The text to render.
	 *  @param width The length of the line on which to render the text
	 *  @param XPos the x-coordinate of the left endpoint of the line, in <em>world</em> coordinates.
	 *  @param YPos the y-coordinate of the left endpoint of the line, in <em>world</em> coordinates.
	 */
	private void drawString(Graphics g, String word, int width, int XPos, int YPos) {
		g.setFont(new Font("TimesRoman", Font.BOLD, 25));
		int stringLength = (int) g.getFontMetrics().getStringBounds(word, g).getWidth();
		int start = convertDimension(width)/2 - stringLength/2;
		g.setColor(Color.BLACK);
		g.drawString(word, start + convertDimension(XPos), convertDimension(YPos));
	}

	/** Converts a dimension (i.e.&nbsp;half of a coordinate) from the <em>world</em> coordinate system to the <em>pixel</em> coordinate system.
	 *  @param world_dimension The dimension (i.e. half of a coordinate) to convert, in the <em>world</em> coordinate system.
	 *  @return The same dimension as the parameter, in the <em>pixel</em> coordinate system.
	 */
	private int convertDimension(int world_dimension) {
		return (int) ((double) world_dimension / Controller.WORLD_WIDTH * this.getWidth());
	}

	/** Returns the largest possible square that can fit in the layout. Note that {@link Dimension} in this case is a duple of a width and a height, as opposed to the terminology "dimension" used in {@link #convertDimension}.
	 *  @return The largest possible square that can fit in the layout
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension parent = this.getParent().getSize();
		if(parent.width > parent.height) {
			return new Dimension(parent.height, parent.height);
		} else {
			return new Dimension(parent.width, parent.width);
		}
	}

	
	/**Updates the View based on the given parameters.
	 * Updates the player's location, direction, and status. 
	 * Updates the crab's position. 
	 * Updates the most recently held {@link Litter}, whether the player is currently holding a {@link Litter}, and the most recent {@link Litter} eaten by the animal.
	 * Updates the current game score.
	 * 
	 * @param playerX The Player's X-location in <em>world</em> coordinates.
	 * @param playerY The Player's Y-location in <em>world</em> coordinates.
	 * @param dir The current Direction of the Player. 
	 * @param status The Player's current status. 
	 * @param crabX The Animal's X-location in <em>world</em> coordinates.
	 * @param crabY The Animal's Y-location in <em>world</em> coordinates.
	 * @param playerPickedUp The most recent Litter object picked up by the Player
	 * @param hasLitter Boolean value representing if the Player is currently holding a Litter object. 
	 * @param animalEatenLitter The most recent Litter object eaten by the animal. 
	 * @param score The current game score.
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
	 * @param l The Litter object that will be added for rendering.
	 */
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
