package MVC;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

/** 
 *  The Sprite class manages all sprites (images) that are drawn.
 *  It handles loading them from disk when necessary and scaling them when necessary.
 *  One can never handle instances of the Sprite class; it is not instantiable and has no factory method.
 *  To use an image, the user must call {@link #getImage(Sprite.ID, double)} passing in the id of the image they want and a scale factor.
 *
 *  <p>Use caution: {@link #getImage(Sprite.ID, double)} is a <code>public static</code> method, intended for use by the user. {@link #getImage(double)} is a <code>private</code> instance method, used internally.
 *  @author Zack Klodnicki  
 *  @see #getImage(Sprite.ID, double)
 */
public class Sprite {
	// ---------- STATIC stuff (all public stuff is here) ---------- //
	/** Contains all Sprite instances. No instance should ever be leaked outside this class. */
	private static HashMap<Sprite.ID, Sprite> instances = new HashMap<>();

	/** The frame number, used to calculate the frame to use in animated sprites. */
	private static int frameCounter = 0;

	/** Houses all Sprites' info. This is the only public way to represent a Sprite. */
	public static enum ID {
		/** The background image */
		BACKGROUND("Map/Background.jpg", View.WORLD_WIDTH, View.WORLD_HEIGHT),

		/** The Title Screen */
		TITLE_SCREEN("overlays/titleScreen.png", View.WORLD_WIDTH*18/20, View.WORLD_HEIGHT*18/20),

		/** Orc walkig north */
		ORC_WALK_NORTH("orc/orc_forward_north.png", 165, 165, 10, 1),
		/** Orc walking south */
		ORC_WALK_SOUTH("orc/orc_forward_south.png", 165, 165, 10, 1),
		/** Orc walking east */
		ORC_WALK_EAST("orc/orc_forward_east.png", 165, 165, 10, 1),
		/** Orc walking west */
		ORC_WALK_WEST("orc/orc_forward_west.png", 165, 165, 10, 1),
		/** Orc walking northwest */
		ORC_WALK_NORTHWEST("orc/orc_forward_northwest.png", 165, 165, 10, 1),
		/** Orc walking southeast */
		ORC_WALK_NORTHEAST("orc/orc_forward_northeast.png", 165, 165, 10, 1),
		/** Orc walking southwest */
		ORC_WALK_SOUTHWEST("orc/orc_forward_southwest.png", 165, 165, 10, 1),
		/** Orc walking southeast */
		ORC_WALK_SOUTHEAST("orc/orc_forward_southeast.png", 165, 165, 10, 1),

		/** Orc facing north */
		ORC_IDLE_NORTH("orc/orc_idle_north.png", 165, 165, 4, 1),
		/** Orc facing south */
		ORC_IDLE_SOUTH("orc/orc_idle_south.png", 165, 165, 4, 1),
		/** Orc facing east */
		ORC_IDLE_EAST("orc/orc_idle_east.png", 165, 165, 4, 1),
		/** Orc facing west */
		ORC_IDLE_WEST("orc/orc_idle_west.png", 165, 165, 4, 1),

		/** A crab */
		CRAB("Animal/crab.png", 150, 100, 7, 5),
		/** A star intended to house the current score */
		SCORESTAR("MapObjects/FlatIcon-074.png", 100, 100),
		/** A box intended to house a type of litter */
		LITTERFRAME("MapObjects/litterbox.png", 95,95),
		/** A plant */
		PLANT("MapObjects/azalea.png", 100, 100),
		/** A decaying plant */
		DECAY_PLANT("MapObjects/dazal.png", 100, 100),
		/** spot of dirt where plant resided*/	
		DIRT("MapObjects/dirtPatch.png", 100, 100),
		/** spot of mud where plant resided*/	
		MUD("MapObjects/mud.png", 100, 100),
		/** A banana peel */
		BANANAPEEL("MapObjects/bananaSkin.png",50,50),
		/** A soda can */
		SODACAN("MapObjects/Soda-Can.png",40,40),
		/** A crumpled up piece of paper */
		PAPER("MapObjects/paper.png",40,40),
		/** A eaten apple */
		APPLE("MapObjects/CompostA.png",75,75),
		/** A trash bin */
		TRASHBIN("MapObjects/garbage.png",128,128),
		/** A glowing trash bin */
		TRASHGLOW("MapObjects/glow-trash.png", 128,128),
		/** A recycle bin */
		RECYCLEBIN("MapObjects/recycling-bin.png",128,128),
		/** A glowing recycle bin */
		RECYCLEGLOW("MapObjects/recyclegreen.png",128,128),
		/** Onscreen river*/
		RIVER("MapObjects/river2.png", View.WORLD_HEIGHT, View.WORLD_HEIGHT),
		/** A garbage truck for timer  */
		GARBAGETRUCK("Map/garbage-truck.png",128,128),   // <div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
		/** The path on which the garbage truck timer travels */ 
		REDPATH("Map/Red-path.png",1500,60),
		/** Flag marking the destination of the garbage truck */
		FLAG("Map/finish-flag.png",128,128),
		/** The arrow key prompts for the tutorial*/
		ARROWKEYS("Tutorial/arrow_key.png",80,60),
		/** The arrow prompt for the tutorial */
		ARROW("Tutorial/arrow.png",40,60),
		SPACEKEY("Tutorial/Key_space_Icon.png",60,50),
		;
		
		// These values are set to the enum instances. When a Sprite object is instantiated, it copies these values.

		/** The filename (relative to images/) to find the source tileset. */
		private final String fname;
		/** The width of the sprite, in world coordinates. */
		private final int worldWidth;
		/** The height of the sprite, in world coordinates. */
		private final int worldHeight;
		/** The number of tiles in a single row of the tileset */
		private final int numTilesWide;
		/** The number of tiles in a single column of the tileset */
		private final int numTilesHigh;

		/** Creates a non-animated Sprite.ID with the given fname, world width, and world height. 
		 *  @param fname The path to the source image, relative to the images/ folder.
		 *  @param worldWidth The display width of the image, in world coordinates.
		 *  @param worldHeight The display height of the image, in world coordinates.
		*/
		private ID(String fname, int worldWidth, int worldHeight) {
			// Internally, making it not animate it just making an animation using a 1x1 tileset.
			this(fname, worldWidth, worldHeight, 1, 1);
		}

		/** Creates a animated Sprite.ID with the given filename, world width, world height, tileset width, and tileset height.
		 *  @param fname The path to the source image, relative to the images/ folder.
		 *  @param worldWidth The display width of the image, in world coordinates.
		 *  @param worldHeight The display height of the image, in world coordinates.
		 *  @param numTilesWide The number of tiles in a single row of the tileset.
		 *  @param numTilesHigh The number of tiles in a single column of the tileset.
		 */
		private ID(String fname, int worldWidth, int worldHeight, int numTilesWide, int numTilesHigh) {
			this.fname = fname;
			this.worldWidth = worldWidth;
			this.worldHeight = worldHeight;
			this.numTilesWide = numTilesWide;
			this.numTilesHigh = numTilesHigh;
		}
	}

	/**
	 *  Retrieve the correctly scaled image for the given id, according to the given scaleFactor, loading from disk if necessary, scaling if necessary, and tiling if necessary.
	 *  The scaleFactor is the size of the View relative to the Model. For example, if the Model is 100 units wide and the View is 200 pixels wide, scaleFactor should be 2.0.
	 *  @return The correctly scaled image
	 *  @param id The sprite id to get the image from
	 *  @param scaleFactor The size of the View relative to the Model.
	 */
	public static BufferedImage getImage(Sprite.ID id, double scaleFactor) {
		Sprite s = instances.get(id);
		if(s == null) {
			s = new Sprite(id.fname, id.worldWidth, id.worldHeight, id.numTilesWide, id.numTilesHigh);
			instances.put(id, s);
		}

		return s.getImage(scaleFactor);
	}

	/**
	 * Increment the frame counter used for animated Sprites. Should be called once per frame.
	 */
	public static void incrementFrameCounter() {
		frameCounter++;
	}

	// ---------- NON-STATIC stuff (all private) ---------- //

	/** The path to the source image, relative to the imges/ directory */
	private String fname;
	/** The scaleFactor used to generate the currently saved scaled image */
	private double scaleFactor = -1;
	/** The display width of the Sprite, in world coordinates */
	private int worldWidth;
	/** The display height of the Sprite, in world coordinates */
	private int worldHeight;
	/** The number of tiles in a single row of the tileset */
	private int numTilesWide;
	/** The number of tiles in a single column of the tileset */
	private int numTilesHigh;
	/** The completely unprocessed source image */
	private BufferedImage source;
	/** A scaled version of {@link #source}, scaled according to the scale factor {@link #scaleFactor}*/
	private BufferedImage scaled;
	/** A array of tiles, cropped out of {@link #scaled} */
	private BufferedImage[] scaledTiles;

	/** Creates a Sprite with the specified attributes, not to be called by the user,
	 *  @param fname The path to the source image, relative to the images/ folder.
	 *  @param worldWidth The display width of the image, in world coordinates.
	 *  @param worldHeight The display height of the image, in world coordinates.
	 *  @param numTilesWide The number of tiles in a single row of the tileset.
	 *  @param numTilesHigh The number of tiles in a single column of the tileset.
	 */
	private Sprite(String fname, int worldWidth, int worldHeight, int numTilesWide, int numTilesHigh) {
		this.fname = fname;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.numTilesWide = numTilesWide;
		this.numTilesHigh = numTilesHigh;
	}

	/** Ensures that the image is loaded from disk. Will only load from disk once on a single Sprite instance. 
	 *  Stores the image in {@link #source}.
	 */
	private void loadSource() {
		// If we've already loaded, then do nothing.
		if(this.source != null) return;

		// Load from disk.
		System.out.println("Loading "+this.fname+" from disk.");
		try {
			this.source = ImageIO.read(new File("images/"+this.fname));
			this.scaled = null; // At this point this.scaled should already be null. This line is just in case the class changes in the future.
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Ensures that the image has been scaled according to the specified scale factor. Will never scale the image the same way multiple times consecutively.
	 *  Stores the scaled image in {@link #scaled} and the scale factor used in {@link #scaleFactor}.
	 *  @param scaleFactor The ratio of the view width to the world width
	 *  @see #scaled
	 *  @see #scaleFactor
	 */
	private void scale(double scaleFactor) {
		// If we've already scaled according to this scale factor, then do nothing.
		if(scaleFactor == this.scaleFactor) return;

		// Remember what scale factor we've used!
		this.scaleFactor = scaleFactor;

		// Also forget the tiling so that we have to do it again.
		this.scaledTiles = null;

		System.out.println("Scaling "+this.fname+" by scale factor " + scaleFactor);

		// Width and height in pixels
		int scaledWidth = (int) (worldWidth*scaleFactor*this.numTilesWide);
		int scaledHeight = (int) (worldHeight*scaleFactor*this.numTilesHigh);

		// Special case: scaled is exactly the same dimensions as source!
		if(scaledWidth == this.source.getWidth() && scaledHeight == this.source.getHeight()) {
			this.scaled = this.source;
			return;
		}

		// Actually perform the scaling
		this.scaled = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = this.scaled.createGraphics();
		g.drawImage(this.source, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
	}

	/** Ensures that the image has been tiled into individual frames. Will never tile the same image multiple times consecutively.
	 *  Stores the resultant frames in {@link #scaledTiles}.
	 *  @see #scaledTiles
	 */
	private void tile() {
		// If we've already tiled, do nothing
		if(this.scaledTiles != null) return;

		// If the tileset is 1x1 (i.e. a single frame), deal with that more efficiently.
		if(this.numTilesWide == 1 && this.numTilesHigh == 1) {
			this.scaledTiles = new BufferedImage[1];
			this.scaledTiles[0] = this.scaled;
			return;
		}

		System.out.println("Tiling "+this.fname);

		// Allocate space for every frame
		this.scaledTiles = new BufferedImage[this.numTilesWide*this.numTilesHigh];

		// Dimensions of a single tile, in pixels.
		double tileWidth = (double) this.scaled.getWidth() / this.numTilesWide;
		double tileHeight = (double) this.scaled.getHeight() / this.numTilesHigh;

		// Actually perform the cropping
		for(int y = 0; y < this.numTilesHigh; y++) {
			for(int x = 0; x < this.numTilesWide; x++) {
				this.scaledTiles[y*numTilesWide + x] = this.scaled.getSubimage(
					(int) (x*tileWidth),
					(int) (y*tileHeight),
					(int) (tileWidth),
					(int) (tileHeight));
			}
		}
	}

	/** Retrieve the correctly scaled image according to the scaleFactor, loading from disk if necessary, scaling if necessary, and tiling if necessary.
	 *  @return The correct sized BufferedImage
	 *  @param scaleFactor The ratio of the size of the View to the size of the World, as in {@link #getImage(Sprite.ID, double)}
	 *  @see #getImage(Sprite.ID, double)
	 */
	private BufferedImage getImage(double scaleFactor) {
		this.loadSource(); // Ensure loaded from disk
		this.scale(scaleFactor); // Ensure scaled correctly
		this.tile(); // Ensure has been tiled
		return this.scaledTiles[Sprite.frameCounter % this.scaledTiles.length]; // Return a single frame
	}
}
