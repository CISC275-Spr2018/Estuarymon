package MVC;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

class Sprite {
	// ---------- STATIC stuff (all public stuff is here) ---------- //
	private static HashMap<Sprite.ID, Sprite> instances = new HashMap<>();

	private static int frameCounter = 0;

	public static enum ID {
		BACKGROUND("Map/Background.jpg", Controller.WORLD_WIDTH, Controller.WORLD_HEIGHT),

		ORC_WALK_NORTH("orc/orc_forward_north.png", 165, 165, 10, 1),
		ORC_WALK_SOUTH("orc/orc_forward_south.png", 165, 165, 10, 1),
		ORC_WALK_EAST("orc/orc_forward_east.png", 165, 165, 10, 1),
		ORC_WALK_WEST("orc/orc_forward_west.png", 165, 165, 10, 1),
		ORC_WALK_NORTHWEST("orc/orc_forward_northwest.png", 165, 165, 10, 1),
		ORC_WALK_NORTHEAST("orc/orc_forward_northeast.png", 165, 165, 10, 1),
		ORC_WALK_SOUTHWEST("orc/orc_forward_southwest.png", 165, 165, 10, 1),
		ORC_WALK_SOUTHEAST("orc/orc_forward_southeast.png", 165, 165, 10, 1),

		ORC_IDLE_NORTH("orc/orc_idle_north.png", 165, 165, 4, 1),
		ORC_IDLE_SOUTH("orc/orc_idle_south.png", 165, 165, 4, 1),
		ORC_IDLE_EAST("orc/orc_idle_east.png", 165, 165, 4, 1),
		ORC_IDLE_WEST("orc/orc_idle_west.png", 165, 165, 4, 1),

		CRAB("Animal/crab.png", 150, 100, 7, 5),
		PLANT("MapObjects/azalea.png", 130, 130),
		DECAY_PLANT("MapObjects/dazal.png", 120, 120),
		LITTERFRAME("MapObjects/litterbox.png", 75,75),
		BANANAPEEL("MapObjects/bananaSkin.png",40,40),
		SODACAN("MapObjects/Soda-Can.png",40,40),
		PAPER("MapObjects/paper.png",40,40),
		APPLE("MapObjects/CompostA.png",50,50);
		


		private String fname;
		private int worldWidth;
		private int worldHeight;
		private int numTilesWide;
		private int numTilesHigh;

		private ID(String fname, int worldWidth, int worldHeight) {
			this(fname, worldWidth, worldHeight, 1, 1);
		}

		private ID(String fname, int worldWidth, int worldHeight, int numTilesWide, int numTilesHigh) {
			this.fname = fname;
			this.worldWidth = worldWidth;
			this.worldHeight = worldHeight;
			this.numTilesWide = numTilesWide;
			this.numTilesHigh = numTilesHigh;
		}
	}

	public static BufferedImage getImage(Sprite.ID id, double scaleFactor) {
		Sprite s = instances.get(id);
		if(s == null) {
			s = new Sprite(id.fname, id.worldWidth, id.worldHeight, id.numTilesWide, id.numTilesHigh);
			instances.put(id, s);
		}

		return s.getImage(scaleFactor);
	}

	public static void incrementFrameCounter() {
		frameCounter++;
	}

	// ---------- NON-STATIC stuff (all private) ---------- //

	private String fname;
	private double scaleFactor = -1;
	private int worldWidth;
	private int worldHeight;
	private int numTilesWide;
	private int numTilesHigh;
	private BufferedImage source;
	private BufferedImage scaled;
	private BufferedImage[] scaledTiles;

	private Sprite(String fname, int worldWidth, int worldHeight, int numTilesWide, int numTilesHigh) {
		this.fname = fname;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.numTilesWide = numTilesWide;
		this.numTilesHigh = numTilesHigh;
	}

	private void loadSource() {
		if(this.source != null) return;

		System.out.println("Loading "+this.fname+" from disk.");
		try {
			this.source = ImageIO.read(new File("images/"+this.fname));
			this.scaled = null;
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void scale(double scaleFactor) {
		if(scaleFactor == this.scaleFactor) return;
		this.scaleFactor = scaleFactor;

		System.out.println("Scaling "+this.fname+" by scale factor " + scaleFactor);

		int scaledWidth = (int) (worldWidth*scaleFactor*this.numTilesWide);
		int scaledHeight = (int) (worldHeight*scaleFactor*this.numTilesHigh);

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

	private void tile() {
		this.scaledTiles = new BufferedImage[this.numTilesWide*this.numTilesHigh];

		double tileWidth = (double) this.scaled.getWidth() / this.numTilesWide;
		double tileHeight = (double) this.scaled.getHeight() / this.numTilesHigh;

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

	private BufferedImage getImage(double scaleFactor) {
		this.loadSource();
		this.scale(scaleFactor);
		this.tile();
		return this.scaledTiles[Sprite.frameCounter % this.scaledTiles.length];
	}
}
