package MVC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
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
import MapObjects.Receptacle;
import MapObjects.River;
import Player.Direction;
import Player.Player;
import Player.PlayerStatus;

/**
 * View: Contains everything about graphics and images Know size of screen,
 * which images to load etc
 *
 * @author Zack Klodnicki
 * @author Juan Villacis
 *
 *
 **/
public class View extends JPanel {
	/** The width of the game world */
	public static final int WORLD_WIDTH = 1500;
	/** The height of the game world */
	public static final int WORLD_HEIGHT = 1000;
	/** The dimensions of the computer screen, in pixels. */
	private final static Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
	/** The height of the computer screen, in pixels. */
	private final static double screenHeight = screenDimension.getHeight();
	/** The width of the computer screen, in pixels. */
	private final static double screenWidth = screenDimension.getWidth();
	/** The JFrame housing this View. */
	JFrame frame;
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
	/**
	 * The image attributes of the Litter object most recently held by the Player
	 */
	private ArrayList<Integer> pickedUpAttr = new ArrayList<Integer>();
	/** Whether the player is holding on to litter */
	private boolean hasLitter = false;

	/**
	 * A list containing lists of litter sprite ids. Organized into how they may be
	 * deposited, i.e. trash vs. recyclable.
	 */
	private static ArrayList<ArrayList<Sprite.ID>> litterImgLists = new ArrayList<ArrayList<Sprite.ID>>();
	/**
	 * Contains all Litter objects to be rendered onscreen, maps them to a
	 * Sprite.ID.
	 */
	private HashSet<ArrayList<Integer>> litterAttrSet = new HashSet<ArrayList<Integer>>();
	/** Contains all plant objects onscreen */
	private ArrayList<Plant> plants = new ArrayList<Plant>();
	/** contains all of the Litter objects on-screen */
	private HashSet<Litter> litterSet = new HashSet<Litter>();

	/**
	 * Gamestate variable that represents the current stage of the tutorial the
	 * player is at
	 */
	TutorialState tutorialState = TutorialState.SIGNALTRASH;
	/** A Boolean to decide if the trash bin is in the glowing deposit state */
	private boolean tGlow = false;
	/** A Boolean to decide if the recycling bin is in the glowing deposit state */
	private boolean rGlow = false;

	private int playerHealth = 0;
	private int animalHealth = 0;

	/** river onmap **/
	private River river = new River(0, 0, 0, 0);

	/**
	 * A long representing when the game started in order to draw the truck timer in
	 * the correct spot
	 */
	private long startTime;
	/**
	 * A long representing when the game should end in order to draw the truck timer
	 * in the correct spot
	 */
	private int endTime;

	/**
	 * Boolean that determines whether the arrow key prompt should be shown on
	 * screen.
	 */
	private boolean arrowKeyPrompt = false;
	/**
	 * Boolean that represents whether or not the Player is hovering, but not
	 * picking up a Litter object
	 */
	private boolean hoverLitter = false;

	/** Stores font sizes calculated using binary search. Should be reset whenever View is resized. */
	private Map<Integer, Integer> fontWorldToPt = new HashMap<>();

	/** The current phase of the game */
	GamePhase gamePhase = GamePhase.TITLE_SCREEN;

	/** The time that the phase GamePhase.GAME_END was entered. -1 when not in GAME_END. */
	private long endScreenTimestamp = -1;

	/** Duration over which to slowly raise the score when transitioning into the end game screen. In milliseconds. */
	private static final int END_SCREEN_SCORE_TRANSITION_DURATION = 5000;

	/** Creates a new View, places it in a new JPanel, arranges everything, and makes it visible. */
	public View() {	
		// Prepare for rendering litters
		preloadLitterImgs();

		// Set up the JFrame
		frame = new JFrame();
		frame.setFocusable(true);
		frame.setUndecorated(true);
		frame.add(this);
		frame.setBackground(BACKGROUND_COLOR);
		this.setBackground(BACKGROUND_COLOR);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((int) screenWidth, (int) screenHeight);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	/**
	 * Adds a key listener to the associated JFrame
	 * 
	 * @param listener
	 *            The listener to add
	 * @return None.
	 */
	public void setKeyListener(KeyListener listener) {
		frame.addKeyListener(listener);
	}

	/**
	 * Paints this view
	 * 
	 * @param g
	 *            The {@link java.awt.Graphics} object to use for painting
	 * @return None.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		// Increment animations to the next frame
		Sprite.incrementFrameCounter();
		// Draw the background
		drawImage(g, Sprite.ID.BACKGROUND, 0, 0);
		drawImage(g, Sprite.ID.RIVER, river.getXLocation(), river.getYLocation());

		// Draw all plants
		for (Plant plant : plants) {
			if (plant.getHealth() < 100 && plant.getHealth() != 0) // If decaying...
			{
				drawImage(g, Sprite.ID.DECAY_PLANT, plant.getXLocation(), plant.getYLocation());
			} else if (plant.getHealth() == 100) // If fully alive...
			{
				drawImage(g, Sprite.ID.PLANT, plant.getXLocation(), plant.getYLocation());
			} else {
				drawImage(g, Sprite.ID.DIRT, plant.getXLocation(), plant.getYLocation());
			}
		}

		// Draw Receptacles
		if (tGlow) {
			drawImage(g, Sprite.ID.TRASHGLOW, 0, Receptacle.trashYpos);
		} else {
			drawImage(g, Sprite.ID.TRASHBIN, 0, Receptacle.trashYpos);
		}
		if (rGlow) {
			drawImage(g, Sprite.ID.RECYCLEGLOW, 0, Receptacle.recyclingYpos);
		} else {
			drawImage(g, Sprite.ID.RECYCLEBIN, 0, Receptacle.recyclingYpos);
		}

		// traverse through litter set and draw them, had to make a copy of litter set
		// everytime to avoid ConcurrentModificationExceptions.

		for (ArrayList<Integer> arr : new HashSet<ArrayList<Integer>>(this.litterAttrSet))
			drawImage(g, getSpriteID(arr.get(3), arr.get(2)), arr.get(0), arr.get(1));
		// Draw the crab
		drawImage(g, Sprite.ID.CRAB, crabXLoc, crabYLoc);
		// Draw the player
		drawImage(g, getPlayerSprite(), playerXLoc, playerYLoc);

		// Draw overlays depending on the game phase
		switch (this.gamePhase) {
		case TITLE_SCREEN:
			this.drawOverlayBox(g);
			this.drawStartScreenText(g);
			break;
		case TUTORIAL:
			this.drawLitterContainerOverlay(g);
			this.drawTutorialOverlays(g);
			break;
		case NORMAL:
			this.drawLitterContainerOverlay(g);
			this.drawHealth(g);
			this.drawTimer(g);
			break;
		case GAME_END:
			this.drawOverlayBox(g);
			this.drawEndScreenOverlay(g);
			break;
		}
	}

	/** Draws the litter container overlay */
	private void drawLitterContainerOverlay(Graphics g) {

		// Draw the litter in the box
		drawImage(g, Sprite.ID.LITTERFRAME,0,5);
		if(hasLitter) {
			drawImage(g,getSpriteID(pickedUpAttr.get(1),pickedUpAttr.get(0)),15,15);
//
//		drawImage(g, Sprite.ID.LITTERFRAME, 0, 0);
//		if (hasLitter) {
//			drawImage(g, getSpriteID(pickedUpAttr.get(1), pickedUpAttr.get(0)), 10, 10);
		}
	}

	private void drawTutorialOverlays(Graphics g) {
		if (arrowKeyPrompt)
			drawImage(g, Sprite.ID.ARROWKEYS, 240, 200);

		switch (this.tutorialState) {
		case SIGNALTRASH:
		case SIGNALRECYCLABLE:
			if (hoverLitter) {
				drawImage(g, Sprite.ID.SPACEKEY, playerXLoc, playerYLoc - 20);
			} else {
				drawImage(g, Sprite.ID.ARROW, 360, 400);
			}
			break;
		case SIGNALPLANT:
			drawImage(g, Sprite.ID.ARROW, plants.get(0).getXLocation() + 30, plants.get(0).getYLocation()-50);
			break;
		case SIGNALTRASHCAN:
			drawImage(g, Sprite.ID.ARROW, 50, Receptacle.trashYpos - 60);
			break;
		case SIGNALRECYCLINGBIN:
			drawImage(g, Sprite.ID.ARROW, 50, Receptacle.recyclingYpos - 60);
			break;

		}
	}

	/**
	 * Draws the health bar and the crabs in the upper right hand corner of the
	 * screen. When the player loses life, the bar becomes more red and when the crab
	 * runs into a piece of trash or recyclable, a crab is lost.
	 * 
	 * @param g
	 *            The {@link java.awt.Graphics} object to use for painting
	 * @return None.
	 */
	private void drawHealth(Graphics g) {

		g.setColor(Color.green);
		g.fillRect(WORLD_WIDTH - 60, 20, 300, 50);

		g.setColor(Color.red);
		g.fillRect(WORLD_WIDTH - 60, 20, 300 - playerHealth, 50);

		drawImage(g, Sprite.ID.KIDHEAD, WORLD_WIDTH - 110, 0);

		if (animalHealth <= 90 && animalHealth > 60) {
			drawImage(g, Sprite.ID.CRABHEART, WORLD_WIDTH - 130, 60);
			drawImage(g, Sprite.ID.CRABHEART, WORLD_WIDTH - 215, 60);
			drawImage(g, Sprite.ID.CRABHEART, WORLD_WIDTH - 300, 60);
		} else if (animalHealth <= 60 && animalHealth > 30) {
			drawImage(g, Sprite.ID.CRABHEART, WORLD_WIDTH - 130, 60);
			drawImage(g, Sprite.ID.CRABHEART, WORLD_WIDTH - 215, 60);
		} else if (animalHealth <= 30 && animalHealth > 0) {
			drawImage(g, Sprite.ID.CRABHEART, WORLD_WIDTH - 130, 60);
		}
	}

	/** Draws a box on the screen appropriate for title screen, end score, etc. */
	private void drawOverlayBox(Graphics g) {
		g.setColor(new Color(255, 255, 255, 128));
		g.fillRoundRect(worldXToPixelX(WORLD_WIDTH / 20), // X
				worldYToPixelY(WORLD_HEIGHT / 20), // Y
				worldWidthToPixelWidth(WORLD_WIDTH * 18 / 20), // WIDTH
				worldHeightToPixelHeight(WORLD_HEIGHT * 18 / 20), // HEIGHT
				worldWidthToPixelWidth(150), // ARC WIDTH
				worldHeightToPixelHeight(150)); // ARC HEIGHT
		g.setColor(Color.WHITE);
	}

	private void drawTimer(Graphics g) {
		drawImage(g, Sprite.ID.REDPATH, 0, WORLD_HEIGHT - 64);
		drawImage(g, Sprite.ID.FLAG, WORLD_WIDTH - 128, WORLD_HEIGHT - 164);

		int truckX = (int) (Math
				.floor(((System.currentTimeMillis() - startTime) / (double) endTime) * (WORLD_WIDTH - 128)));
		drawImage(g, Sprite.ID.GARBAGETRUCK, truckX, WORLD_HEIGHT - 128);
	}

	/** Draws the start screen text onto the screen. Does not draw the box. */
	private void drawStartScreenText(Graphics g) {
		this.drawImage(g, Sprite.ID.TITLE_SCREEN,
			WORLD_WIDTH/20,
			WORLD_HEIGHT/20);
	}

	/** Draws the end screen text onto the screen. Does not draw that underlying box. */
	private void drawEndScreenOverlay(Graphics g) {
		this.drawImage(g, Sprite.ID.END_SCREEN,
			WORLD_WIDTH/20,
			WORLD_HEIGHT/20);

		// Now draw the score text.
		// Bottom left of the text is 2/3 from left, 1/2 from top.
		int worldX = WORLD_WIDTH*13/20; // Simplification of:
		                                // (WORLD_WIDTH*18/20)*2/3+(WORLD_WIDTH/20)
		int worldY = WORLD_HEIGHT/2;
		int pixelX = worldXToPixelX(worldX);
		int pixelY = worldYToPixelY(worldY);

		this.setFontSize(g, WORLD_HEIGHT/8); // Side-affect, adds font to Graphics.
		int score = 123;

		int offset = (int) (System.currentTimeMillis() - this.endScreenTimestamp);
		String print;
		if(offset > END_SCREEN_SCORE_TRANSITION_DURATION) {
			print = String.valueOf(score);
			g.setColor(Color.WHITE);
		} else {
			double percent = (double) (System.currentTimeMillis() - this.endScreenTimestamp) / END_SCREEN_SCORE_TRANSITION_DURATION;
			double multiplier = (Math.sin((percent/2+0.5) * Math.PI / 2) - 0.5) * 2;
			multiplier *= multiplier; // square it, make curve more dramatic
			print = String.valueOf((int) (score * multiplier));
			g.setColor(new Color(255, 255, 255, 128));
		}

		g.drawString(print, pixelX, pixelY);
		g.setColor(Color.WHITE);
	}

	/**
	 * Determines which {@link Sprite.ID} to use to render the player. Determines
	 * this based on the player's {@link #playerStatus status} and
	 * {@link #playerDirection direction}.
	 * 
	 * @param None.
	 * @return The appropriate {@link Sprite.ID} to use to render the player
	 */
	private Sprite.ID getPlayerSprite() {
		switch (this.playerStatus) {
		case IDLE:
			switch (this.playerDirection) {
			case NORTH:
			case NORTHEAST:
				return Sprite.ID.KID_IDLE_NORTH;
			case EAST:
			case SOUTHEAST:
				return Sprite.ID.KID_IDLE_EAST;
			case SOUTH:
			case SOUTHWEST:
				return Sprite.ID.KID_IDLE_SOUTH;
			case WEST:
			case NORTHWEST:
				return Sprite.ID.KID_IDLE_WEST;
			default: // Unknown player direction
				throw new RuntimeException("Unknkown player direction " + this.playerDirection);
			}
		case WALKING:
			switch (this.playerDirection) {
			case NORTH:
				return Sprite.ID.KID_WALK_NORTH;
			case SOUTH:
				return Sprite.ID.KID_WALK_SOUTH;
			case WEST:
				return Sprite.ID.KID_WALK_WEST;
			case EAST:
				return Sprite.ID.KID_WALK_EAST;
			case NORTHWEST:
				return Sprite.ID.KID_WALK_WEST;
			case NORTHEAST:
				return Sprite.ID.KID_WALK_EAST;
			case SOUTHWEST:
				return Sprite.ID.KID_WALK_WEST;
			case SOUTHEAST:
				return Sprite.ID.KID_WALK_EAST;
			default: // Unknown player direction
				throw new RuntimeException("Unknown player direction " + this.playerDirection);
			}
		default: // Unknown player status
			throw new RuntimeException("Unrecognised player status " + this.playerStatus);
		}
	}

	/**
	 * Renders a {@link Sprite.ID} onto the given Graphics by consuming
	 * <em>world</em> coordinates. Does all conversions necessary to render at the
	 * correct pixel coordinates.
	 * 
	 * @param g
	 *            The {@link Graphics} object to use for painting.
	 * @param s
	 *            The {@link Sprite.ID} to use to retrieve the {@link BufferedImage}
	 *            to draw.
	 * @param world_x
	 *            The x-coordinate of the {@link Sprite.ID} to draw, in
	 *            <em>world</em> coordinates.
	 * @param world_y
	 *            The y-coordinate of the {@link Sprite.ID} to draw, in
	 *            <em>world</em> coordinates.
	 * @return None.
	 */
	private void drawImage(Graphics g, Sprite.ID s, int world_x, int world_y) {
		g.drawImage(Sprite.getImage(s, (double) this.getFrameWidth() / WORLD_WIDTH), worldXToPixelX(world_x),
				worldYToPixelY(world_y), this);
	}

	/**
	 * Draws a string centered in the specified horizontal line by consuming
	 * <em>world</em> coordinates.
	 * 
	 * @param g
	 *            The {@link Graphics} object to use for painting.
	 * @param word
	 *            The text to render.
	 * @param width
	 *            The length of the line on which to render the text
	 * @param XPos
	 *            the x-coordinate of the left endpoint of the line, in
	 *            <em>world</em> coordinates.
	 * @param YPos
	 *            the y-coordinate of the left endpoint of the line, in
	 *            <em>world</em> coordinates.
	 */
	private void drawString(Graphics g, String word, int width, int XPos, int YPos) {
		g.setFont(new Font("TimesRoman", Font.BOLD, 25));
		int stringLength = (int) g.getFontMetrics().getStringBounds(word, g).getWidth();
		int start = worldXToPixelX(XPos + width / 2) - stringLength / 2;
		g.setColor(Color.BLACK);
		g.drawString(word, start, worldYToPixelY(YPos));
	}

	/**
	 * Consumes a x-coordinate in <em>world</em> coordinates, computes the expected
	 * x-coordinate in the window (i.e.&nbsp;<em>pixel</em> coordinates).
	 * 
	 * @param world_x
	 *            The x-coordinate in <em>world</em> coordinates.
	 * @return The x-coordinate in <em>pixel</em> coordinates.
	 */
	private int worldXToPixelX(int world_x) {
		return getFrameHorizOffset() + (world_x * getFrameWidth() / WORLD_WIDTH);
	}

	/**
	 * Consumes a y-coordinate in <em>world</em> coordinates, computes the expected
	 * y-coordinate in the window (i.e.&nbsp;<em>pixel</em> coordinates).
	 * 
	 * @param world_y
	 *            The y-coordinate in <em>world</em> coordinates.
	 * @return The x-coordinate in <em>pixel</em> coordinates.
	 */
	private int worldYToPixelY(int world_y) {
		return getFrameVertOffset() + (world_y * getFrameHeight() / WORLD_HEIGHT);
	}

	/**
	 * Consumes a width in <em>world</em> coordinates, computes the expected width
	 * in the window (i.e.&nbsp;<em>pixel</em> coordinates).
	 * 
	 * @param width
	 *            in <em>world</em> coordinates.
	 * @return The width in <em>pixel</em> coordinates.
	 */
	private int worldWidthToPixelWidth(int world_width) {
		return (world_width * getFrameWidth() / WORLD_WIDTH);
	}

	/**
	 * Consumes a height in <em>world</em> coordinates, computes the expected height
	 * in the window (i.e.&nbsp;<em>pixel</em> coordinates).
	 * 
	 * @param height
	 *            in <em>world</em> coordinates.
	 * @return The height in <em>pixel</em> coordinates.
	 */
	private int worldHeightToPixelHeight(int height) {
		return (height * getFrameHeight() / WORLD_HEIGHT);
	}

	/**
	 * Returns the width in pixels of the inner frame.
	 * 
	 * @return The width of the inner frame in pixels.
	 */
	private int getFrameWidth() {
		return this.getFrameDimensions().width;
	}

	/**
	 * Returns the height in pixels of the inner frame.
	 * 
	 * @return The height of the inner frame in pixels.
	 */
	private int getFrameHeight() {
		return this.getFrameDimensions().height;
	}

	/**
	 * Returns the distance in pixels between a side of the window and the inner
	 * frame. Can be <code>0</code> if the inner frame size is contrained by the
	 * width of the window.
	 * 
	 * @return The distance between a side of the window and a side of the inner
	 *         frame
	 */
	private int getFrameHorizOffset() {
		return (this.getWidth() - this.getFrameDimensions().width) / 2;
	}

	/**
	 * Returns the distance in pixels between the top/bottom of the window and the
	 * inner frame. Can be <code>0</code> if the inner frame size is contrained by
	 * the height of the window.
	 * 
	 * @return The distance between the top/bottom of the window and the top/bottom
	 *         of the inner frame.
	 */
	private int getFrameVertOffset() {
		return (this.getHeight() - this.getFrameDimensions().height) / 2;
	}

	/**
	 * Returns the dimensions of the inner frame in pixels.
	 * 
	 * @return The dimensions of the inner frame in pixels.
	 */
	private Dimension getFrameDimensions() {
		// Calculate aspect ratios
		final double frameAR = (double) WORLD_WIDTH / WORLD_HEIGHT;
		double screenAR = (double) this.getWidth() / this.getHeight();

		if (frameAR > screenAR) {
			// Frame is wider than screen; constrain on screen width.
			return new Dimension(this.getWidth(), this.getWidth() * WORLD_HEIGHT / WORLD_WIDTH);
		} else {
			// Screen is width than frame; constrain on screen height.
			return new Dimension(this.getHeight() * WORLD_WIDTH / WORLD_HEIGHT, this.getHeight());
		}
	}


	/**
	 * Updates the View based on the given parameters. Updates the player's
	 * location, direction, and status. Updates the crab's position. Updates the
	 * most recently held {@link Litter}, whether the player is currently holding a
	 * {@link Litter}, and the most recent {@link Litter} eaten by the animal.
	 */
	private int setFontSize(Graphics g, int worldHeight) {
		Integer storedSize = this.fontWorldToPt.get(worldHeight);
		if(storedSize != null) {
			g.setFont(new Font("TimesRoman", Font.BOLD, storedSize));
			return storedSize;
		}

		int targetHeight = worldHeightToPixelHeight(worldHeight);
		int fontSize = 32;
		System.out.println("Target "+targetHeight);
		do {
			fontSize *= 2;
			System.out.println("Increasing to "+fontSize);
			g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
		} while (g.getFontMetrics().getHeight() < targetHeight);

		int distance = fontSize/2;
		while(distance > 0) {
			g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
			int height = g.getFontMetrics().getHeight();
			System.out.println("Tryping "+fontSize+" : " + height);
			if(height > targetHeight) {
				fontSize -= distance;
			} else if(height < targetHeight) {
				fontSize += distance;
			} else {
				break;
			}
			distance /= 2;
		}

		System.out.println("Goin' with "+fontSize);

		this.fontWorldToPt.put(worldHeight, fontSize);
		return fontSize;
	}
	
	
	/**Updates the View based on the given parameters.
	 * Updates the player's location, direction, and status. 
	 * Updates the crab's position. 
	 * Updates the most recently held {@link Litter}, whether the player is currently holding a {@link Litter}, and the most recent {@link Litter} eaten by the animal.
	 * Updates the current game score.
	 * 
	 * @param phase
	 *            The current game phase.
	 * @param playerX
	 *            The Player's X-location in <em>world</em> coordinates.
	 * @param playerY
	 *            The Player's Y-location in <em>world</em> coordinates.
	 * @param dir
	 *            The current Direction of the Player.
	 * @param status
	 *            The Player's current status.
	 * @param crabX
	 *            The Animal's X-location in <em>world</em> coordinates.
	 * @param crabY
	 *            The Animal's Y-location in <em>world</em> coordinates.
	 * @param playerPickedUp
	 *            The most recent Litter object picked up by the Player
	 * @param hasLitter
	 *            Boolean value representing if the Player is currently holding a
	 *            Litter object.
	 * @param playerHealth
	 *            int value representing the life of the player
	 * @param animalHealth
	 *            int value representing the life of the animal
	 * @param animalEatenLitter
	 *            The most recent Litter object eaten by the animal.
	 * @param plants
	 *            the array of plants in the game
	 * @param tVictory
	 *            Whether the trash bin should be glowing
	 * @param rVictory
	 *            Whether the recycle bin should be glowing
	 * @param startTime
	 *            When the game began
	 * @param endTime
	 *            When the truck visual timer should end
	 * @return None.
	 */
	public void update(GamePhase gamePhase, int playerX, int playerY, Direction dir, PlayerStatus status, int crabX,
			int crabY, ArrayList<Integer> pickedUpAttr, boolean hasLitter, ArrayList<Plant> plants, boolean tVictory,
			boolean rVictory, int playerHealth, int animalHealth, River river, TutorialState tutorialState,
			HashSet<ArrayList<Integer>> litterAttrSet, boolean arrowKeyPrompt, boolean hoverLitter, long startTime,
			int endTime) {
		// Updating crab and player locations
		this.gamePhase = gamePhase;
		playerXLoc = playerX;
		playerYLoc = playerY;
		playerDirection = dir;
		playerStatus = status;
		crabXLoc = crabX;
		crabYLoc = crabY;
		this.plants = plants;
		this.river = river;
		tGlow = tVictory;
		rGlow = rVictory;

		this.tutorialState = tutorialState;
		this.litterAttrSet = litterAttrSet;

		this.pickedUpAttr = pickedUpAttr;
		this.hasLitter = hasLitter;

		this.playerHealth = playerHealth;
		this.animalHealth = animalHealth;

		this.arrowKeyPrompt = arrowKeyPrompt;
		this.hoverLitter = hoverLitter;
		this.startTime = startTime;
		this.endTime = endTime;

		if(gamePhase != GamePhase.GAME_END) {
			this.endScreenTimestamp = -1;
		} else if(this.endScreenTimestamp == -1) {
			this.endScreenTimestamp = System.currentTimeMillis();
		}
		frame.repaint();
	}

	/**
	 * Chooses a Sprite ID to represent a Litter object. The type of Litter image is
	 * chosen using the Litter's enum attribute that represents type, specific image
	 * chosen is done using the Litter object's imgID
	 * 
	 * @param l
	 *            the Litter object whose Sprite ID will be chosen.
	 * @return Sprite ID representing the parameter.
	 */
	public Sprite.ID getSpriteID(int lType, int imgID) {
		ArrayList<Sprite.ID> litterImgList = litterImgLists.get(lType);
		return litterImgList.get(imgID % (litterImgList.size()));
	}

	/**
	 * Loads in the different Litter images to be used in the game.
	 * 
	 * 
	 * @param None.
	 * @return None.
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

}
