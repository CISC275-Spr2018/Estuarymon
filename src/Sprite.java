import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Sprite {
	private String fname;
	private double scaleFactor = -1;
	private int worldWidth;
	private int worldHeight;
	private BufferedImage source;
	private BufferedImage scaled;

	public Sprite(String fname, int worldWidth, int worldHeight) {
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

	public BufferedImage getImage(double scaleFactor) {
		if(this.source == null) this.loadSource();
		this.scale(scaleFactor);
		return this.scaled;
	}
}
