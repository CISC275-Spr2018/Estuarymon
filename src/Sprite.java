import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

class Sprite {
	// ---------- STATIC stuff
	private static HashMap<Sprite.ID, Sprite> instances = new HashMap<>();

	public static enum ID {
		ORC_IDLE("orc_idle_ewns.png", 1000, 1000);

		private String fname;
		private int worldWidth;
		private int worldHeight;

		private ID(String fname, int worldWidth, int worldHeight) {
			this.fname = fname;
			this.worldWidth = worldWidth;
			this.worldHeight = worldHeight;
		}
	}

	public static BufferedImage getImage(Sprite.ID id, double scaleFactor) {
		Sprite s = instances.get(id);
		if(s == null) {
			s = new Sprite(id.fname, id.worldWidth, id.worldHeight);
			instances.put(id, s);
		}

		return s.getImage(scaleFactor);
	}

	// ---------- NON-STATIC stuff

	private String fname;
	private double scaleFactor = -1;
	private int worldWidth;
	private int worldHeight;
	private BufferedImage source;
	private BufferedImage scaled;

	private Sprite(String fname, int worldWidth, int worldHeight) {
		this.fname = fname;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}

	private void loadSource() {
		System.out.println("Loading "+this.fname+" from disk.");
		try {
			this.source = ImageIO.read(new File("images/orc/"+this.fname));
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

		int scaledWidth = (int) (worldWidth*scaleFactor);
		int scaledHeight = (int) (worldHeight*scaleFactor);

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

	private BufferedImage getImage(double scaleFactor) {
		if(this.source == null) this.loadSource();
		this.scale(scaleFactor);
		return this.scaled;
	}
}
