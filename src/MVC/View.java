package MVC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.TexturePaint;
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

	/** The current health of the player */
	private int playerHealth = 0;
	/** The current health of the animal */
	private int animalHealth = 0;

	/** The river */
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

	/** The number of Litter objects that the Player has picked up throughout the game */
	private int totalLitterCollected = 0;
	/** The number of Plants that the Player has replanted throughout the game */
	private int totalPlantsPlanted = 0;
	/** Whether the player has lost or won the game. True if lost, false if won. */
	private boolean hasLost;

	/** A array of every pixel in the image {@link Sprite.ID.RIVER_TEXTURE}, set the first time {@link #drawRiver} is run. */
	private int[] riverTexturePixels;
	/** A array of every pixel in the image {@link Sprite.ID.RIVER_ALPHA}, set the first time {@link #drawRiver} os run. */
	private int[] riverAlphaMapPixels;
	

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
		Graphics2D g2 = (Graphics2D) g;
		// Increment animations to the next frame
		Sprite.incrementFrameCounter();
		// Draw the background
		this.setPaint(g2, Sprite.ID.BACKGROUND);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2.setPaint(Color.WHITE);
		// Draw the river
		this.drawRiver(g2, this.river.getXLocation());
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

	/** Draws the litter container overlay
	 * 
	 * @param g
	 *            The {@link java.awt.Graphics} object to use for painting
	 */
	private void drawLitterContainerOverlay(Graphics g) {

		// Draw the litter in the box
		drawImage(g, Sprite.ID.LITTERFRAME,0,5);
		if(hasLitter) {
			drawImage(g,getSpriteID(pickedUpAttr.get(1),pickedUpAttr.get(0)),15,15);
		}
	}
	/** Draws the tutorial overlay
	 * 
	 * @param g
	 *            The {@link java.awt.Graphics} object to use for painting
	 */
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

	/** Draws a box on the screen appropriate for title screen, end score, etc.
	 * 
	 * @param g
	 *            The {@link java.awt.Graphics} object to use for painting
	 */
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

	/** Draws a timer on the screen. Represented as a garbage truck moving along a line.
	 * 
	 * @param g
	 *            The {@link java.awt.Graphics} object to use for painting
	 */
	private void drawTimer(Graphics g) {
		drawImage(g, Sprite.ID.REDPATH, 0, WORLD_HEIGHT - 64);
		drawImage(g, Sprite.ID.FLAG, WORLD_WIDTH - 128, WORLD_HEIGHT - 164);

		int truckX = (int) (Math
				.floor(((System.currentTimeMillis() - startTime) / (double) endTime) * (WORLD_WIDTH - 128)));
		drawImage(g, Sprite.ID.GARBAGETRUCK, truckX, WORLD_HEIGHT - 128);
	}

	/** Draws the start screen text onto the screen. Does not draw the box.
	 * 
	 * @param g
	 *            The {@link java.awt.Graphics} object to use for painting
	 */
	private void drawStartScreenText(Graphics g) {
		this.drawImage(g, Sprite.ID.TITLE_SCREEN,
			WORLD_WIDTH/20,
			WORLD_HEIGHT/20);
	}

	/** Draws the end screen text onto the screen. Does not draw the underlying box.
	 * 
	 * @param g
	 *            The {@link java.awt.Graphics} object to use for painting
	 */
	private void drawEndScreenOverlay(Graphics g) {
		if(this.hasLost) {
			this.drawImage(g, Sprite.ID.TRY_AGAIN_SCREEN,
				WORLD_WIDTH/20,
				WORLD_HEIGHT/20);
		} else {
			this.drawImage(g, Sprite.ID.END_SCREEN,
				WORLD_WIDTH/20,
				WORLD_HEIGHT/20);
		}

		g.setColor(Color.BLACK);

		this.drawString(g, "# of Plants Replanted: ", WORLD_WIDTH / 2, WORLD_HEIGHT * 140 / 216, WORLD_HEIGHT / 16, HorizLocation.RIGHT, VertLocation.BOTTOM);
		this.drawString(g, "# of Litter Collected: ", WORLD_WIDTH / 2, WORLD_HEIGHT * 100 / 216, WORLD_HEIGHT / 16, HorizLocation.RIGHT, VertLocation.BOTTOM);

		int offset = (int) (System.currentTimeMillis() - this.endScreenTimestamp);
		String printLitter;
		String printPlants;
		if(offset > END_SCREEN_SCORE_TRANSITION_DURATION) {
			printLitter = String.valueOf(this.totalLitterCollected);
			printPlants = String.valueOf(this.totalPlantsPlanted);
			g.setColor(Color.BLACK);
		} else {
			double percent = (double) (System.currentTimeMillis() - this.endScreenTimestamp) / END_SCREEN_SCORE_TRANSITION_DURATION;
			double multiplier = (Math.sin((percent/2+0.5) * Math.PI / 2) - 0.5) * 2;
			multiplier *= multiplier; // square it, make curve more dramatic
			printLitter = String.valueOf((int) (this.totalLitterCollected * multiplier));
			printPlants = String.valueOf((int) (this.totalPlantsPlanted * multiplier));
			g.setColor(new Color(0, 0, 0, 128));
		}

		// Fit these even vertically between 25% of dialog and 75% of dialog. So they should vertically be at 41% and 57% of the dialog.
		// So they should bet 100/216 and 140/216 of the world. Math.

		this.drawString(g, printLitter, WORLD_WIDTH / 2, WORLD_HEIGHT * 100 / 216, WORLD_HEIGHT / 16, HorizLocation.LEFT, VertLocation.BOTTOM);
		this.drawString(g, printPlants, WORLD_WIDTH / 2, WORLD_HEIGHT * 140 / 216, WORLD_HEIGHT / 16, HorizLocation.LEFT, VertLocation.BOTTOM);
	}

	/** Draws the river on the screen. Some complex rendering tecniques used here!
	 *  @param g The graphics object to use for rendering
	 *  @param xpos_world The x-position of the River, in world coordinates
	 */
	private void drawRiver(Graphics2D g, int xpos_world) {
		// ---------- Constants for tuning appearance!
		final int RIVER_X_DIVISOR = -82;
		final int RIVER_Y_DIVISOR = 75;
		final int ALPHA_X_DIVISOR = -60;
		final int ALPHA_Y_DIVISOR = 80;
		final int SIN_DIVISOR = 2102;
		final int SIN_MULTIPLIER = 50;
		final int TRANSITION_WIDTH = 200;
		final int TRANSITION_OFFSET = TRANSITION_WIDTH/2;

		long t = System.currentTimeMillis();
		int xpos = this.worldXToPixelX(xpos_world) + (int) (SIN_MULTIPLIER * Math.sin((double) t / SIN_DIVISOR));

		BufferedImage solidTexture = Sprite.getRawImage(Sprite.ID.RIVER_TEXTURE);
		BufferedImage alphaMap = Sprite.getRawImage(Sprite.ID.RIVER_ALPHA);

		int riverWidth = solidTexture.getWidth();
		int riverHeight = solidTexture.getHeight();
		int alphaWidth = alphaMap.getWidth();
		int alphaHeight = alphaMap.getHeight();

		int riverXOffset = (int) ((t / RIVER_X_DIVISOR) % riverWidth);
		int riverYOffset = (int) ((t / RIVER_Y_DIVISOR) % riverHeight);
		int alphaXOffset = (int) ((t / ALPHA_X_DIVISOR) % alphaWidth);
		alphaXOffset = 0;
		int alphaYOffset = (int) ((t / ALPHA_Y_DIVISOR) % alphaHeight);

		// ---------- Draw solid texture on right side
		// ----- From xpos + 100 to right side

		g.setPaint(
			new TexturePaint(solidTexture, 
				new Rectangle(
					riverXOffset,
					riverYOffset,
					solidTexture.getWidth(), 
					solidTexture.getHeight())));

		g.fillRect(xpos+TRANSITION_OFFSET, 0, this.getWidth() - xpos - TRANSITION_OFFSET, this.getHeight());
		g.setPaint(Color.WHITE);

		// ---------- Load pixels if necessary.
		if(this.riverTexturePixels == null)
			this.riverTexturePixels = solidTexture.getRaster().getPixels(0, 0, solidTexture.getWidth(), solidTexture.getHeight(), this.riverTexturePixels);
		if(this.riverAlphaMapPixels == null) {
			this.riverAlphaMapPixels = alphaMap.getRaster().getPixels(0, 0, alphaWidth, alphaHeight, this.riverAlphaMapPixels);
		}

		// ---------- Construct the transition BufferedImage
		BufferedImage transitionImage = new BufferedImage(TRANSITION_WIDTH, this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int[] transitionPixels = new int[TRANSITION_WIDTH * this.getHeight() * 4];
		transitionPixels = transitionImage.getRaster().getPixels(0, 0, transitionImage.getWidth(), transitionImage.getHeight(), transitionPixels);
		for(int y = 0; y < this.getHeight(); y++) {
			for(int x = 0; x < TRANSITION_WIDTH; x++) {
				int transitionOffset = (x + y * TRANSITION_WIDTH) * 4;
				int riverOffset = (Math.floorMod(x + xpos - TRANSITION_OFFSET - riverXOffset, riverWidth) + Math.floorMod(y - riverYOffset, riverHeight) * riverWidth) * 4;
				int alphaMapOffset = (Math.floorMod(x + xpos - TRANSITION_OFFSET - alphaXOffset, alphaWidth) + Math.floorMod(y - alphaYOffset, alphaHeight) * alphaWidth) * 4;

				float percent = ((float) x / TRANSITION_WIDTH) * 11/12;
				int alphaMapRed = this.riverAlphaMapPixels[alphaMapOffset];
				int alphaValue = (((int) (alphaMapRed + percent * 0x120) - 0x80) * 2) - 0x10;
				if(percent < 0.25);
					alphaValue *= percent * 4;
				if(alphaValue > 0xFF) alphaValue = 0xFF;
				else if(alphaValue < 0 ) alphaValue = 0;

				transitionPixels[transitionOffset] = this.riverTexturePixels[riverOffset];
				transitionPixels[transitionOffset+1] = this.riverTexturePixels[riverOffset+1];
				transitionPixels[transitionOffset+2] = this.riverTexturePixels[riverOffset+2];
				transitionPixels[transitionOffset+3] = alphaValue;
			}
		}
		transitionImage.getRaster().setPixels(0, 0, transitionImage.getWidth(), transitionImage.getHeight(), transitionPixels);

		g.drawImage(transitionImage, xpos - TRANSITION_OFFSET, 0, null);
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
	private void drawString(Graphics g, String str, int xpos, int ypos, int height, HorizLocation horizLocation, VertLocation vertLocation) {
		this.setFontSize(g, height);
		int pixel_x = worldXToPixelX(xpos);
		int pixel_y = worldYToPixelY(ypos);
		int pixel_width = g.getFontMetrics().stringWidth(str);
		int pixel_height = worldHeightToPixelHeight(height);

		switch(horizLocation) {
			case LEFT: break;
			case CENTER: pixel_x -= pixel_width / 2; break;
			case RIGHT: pixel_x -= pixel_width; break;
		}

		switch(vertLocation) {
			case TOP: pixel_y += pixel_height; break;
			case MIDDLE: pixel_y += pixel_height / 2; break;
			case BOTTOM: break;
		}

		g.drawString(str, pixel_x, pixel_y);
	}

	/** Sets the paint of the given Graphics2D to be a tesselation of the given Sprite.ID, scaling to match rendering size. 
	 *  Scales the Sprite appropriately to make it apppear the same size as if it were drawn using @{link #drawImage}.
	 * 
	 * @param g
	 *            The {@link java.awt.Graphics} object to use for painting
	 * @param s
	 *            The {@link Sprite.ID} to use as the texture
	 */
	private void setPaint(Graphics2D g, Sprite.ID s) {
		BufferedImage img = Sprite.getImage(s, (double) this.getFrameWidth() / WORLD_WIDTH);
		g.setPaint(
			new TexturePaint(img, new Rectangle(0, 0, img.getWidth(), img.getHeight())));
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
	 *  Sets the font of the given {@link java.awt.Graphics} to match the given height in <em>world</em> coordinates.
	 *  Uses the font Times New Roman, Bold.
	 *
	 * @param g
	 *        The Graphics to set the font on
	 * @param worldHeight
	 *        The expected font height, in <em>world</em> coordinates.
	 */
	private int setFontSize(Graphics g, int worldHeight) {
		// If we already remember it, just return that.
		Integer storedSize = this.fontWorldToPt.get(worldHeight);
		if(storedSize != null) {
			g.setFont(new Font("TimesRoman", Font.BOLD, storedSize));
			return storedSize;
		}

		// Okay we need to calculate it.
		// Convert the given height to pixel coordinates
		int targetHeight = worldHeightToPixelHeight(worldHeight);
		System.out.println("Target "+targetHeight);

		// Now we're going to continue doubling the size until it's too big.
		int fontSize = 32; // A decent starting point

		do {
			fontSize *= 2;
			System.out.println("Increasing to "+fontSize);
			g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
		} while (g.getFontMetrics().getHeight() < targetHeight);

		// Now we do a binary search (bounded by the starting value of fontSize) to get the exact correct size.
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

		// Remember for later.
		this.fontWorldToPt.put(worldHeight, fontSize);
		// Return our answer.
		return fontSize;
	}
	
	
	/**Updates the View based on the given parameters.
	 * Updates the player's location, direction, and status. 
	 * Updates the crab's position. 
	 * Updates the most recently held {@link Litter}, whether the player is currently holding a {@link Litter}, and the most recent {@link Litter} eaten by the animal.
	 * Updates the current game score.
	 * 
	 * @param gamePhase
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
	 * @param playerPickedUpAttr
	 *            The attributes of the most recent litter object picked up by the
	 *            player.
	 * @param hasLitter
	 *            Boolean value representing if the Player is currently holding a
	 *            Litter object.
	 * @param plants
	 *            All plants to render
	 * @param tVictory
	 *            Whether the player has recently deposited a Trash.
	 * @param rVictory
	 *            Whether the player has recently deposited a Recyclable.
	 * @param animalHealth
	 *            The health of the Animal
	 * @param river
	 *            The river, including its position.
	 * @param tutorialState
	 *            The current state of the tutorial
	 * @param litterAttrSet
	 *            The attributes of all litter to render
	 * @param arrowKeyPrompt
	 *            Whether to render an arrow key tutorial prompt
	 * @param hoverLitter
	 *            Whether the player has touched the litter yet 
	 *            in the tutorial phase
	 * @param startTime
	 *            The <code>System.currentTimeMillis()</code> when the game was started. 
	 *            Should be 0 immediately after the tutorial ends and normal gameplay begins.
	 * @param endTime
	 *            The number of milliseconds that a game should last
	 * @param totalLitterCollected
	 *            The total number of litter objects collected throughout the game
	 * @param totalPlatsPlanted
	 *            The total number of plants that were replanted throughout the game
	 * @param hasLost
	 *            Whether the player has lost the game.
	 * @return None.
	 */
	public void update(GamePhase gamePhase, int playerX, int playerY, Direction dir, PlayerStatus status, int crabX,
			int crabY, ArrayList<Integer> pickedUpAttr, boolean hasLitter, ArrayList<Plant> plants, boolean tVictory,
			boolean rVictory, int playerHealth, int animalHealth, River river, TutorialState tutorialState,
			HashSet<ArrayList<Integer>> litterAttrSet, boolean arrowKeyPrompt, boolean hoverLitter, long startTime,
			int endTime, int totalLitterCollected, int totalPlantsPlanted, boolean hasLost) {
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

		this.totalLitterCollected = totalLitterCollected;
		this.totalPlantsPlanted = totalPlantsPlanted;

		this.hasLost = hasLost;

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
	 * @param lType
	 *            the Litter type whose Sprite ID will be chosen.
	 * @param imgID
	 *            The image id of the given litter.
	 * @return Sprite ID representing the parameter.
	 */
	public Sprite.ID getSpriteID(int lType, int imgID) {
		ArrayList<Sprite.ID> litterImgList = litterImgLists.get(lType);
		return litterImgList.get(imgID % (litterImgList.size()));
	}

	/**
	 * Populates trashImgList and recyclableImgList with Sprite.IDs.
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

	/** A x-location of something */
	private static enum HorizLocation { LEFT, CENTER, RIGHT }
	/** A y-location of something */
	private static enum VertLocation { TOP, MIDDLE, BOTTOM }
}
