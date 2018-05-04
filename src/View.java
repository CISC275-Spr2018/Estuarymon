import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	private static final int recImgCount = 1;
	private static final int litterCount = 20;
	private BufferedImage[] trashImgs = new BufferedImage[trashImgCount+1];
	private BufferedImage[] recyclableImgs = new BufferedImage[recImgCount+1];
	private Litter[] litterArr = new Litter[litterCount];

	//these plants vars for alpha testing
	int plant0H;
	int plant1H;
	int plant2H;
	int plant3H;
	String coords = "";

	public View() {
		preloadLitterImgs();
		
		
		JLabel trashBin = new JLabel();
		ImageIcon trashIcon = new ImageIcon("images/MapObjects/garbage.png");
		Image trashImg = trashIcon.getImage();
		Image secondTrashImg = trashImg.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH) ;
		trashBin.setIcon(new ImageIcon(secondTrashImg));
		trashBin.setBounds(0,450,100,100);
		//frame.getContentPane().add(trashBin);
		
		JLabel recycleBin = new JLabel();
		ImageIcon recycleIcon = new ImageIcon("images/MapObjects/recycling-bin.png");
		Image recycleImg = recycleIcon.getImage();
		Image secondRecycleImg = recycleImg.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH) ;
		recycleBin.setIcon(new ImageIcon(secondRecycleImg));
		recycleBin.setBounds(0,580,100,100);
		//frame.getContentPane().add(recycleBin);
				
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

	/*//makes plant dissappear if health is zero
	public void deletePlant(int pick)
	{
		plantImgs.get(pick).setVisible(false);
	}
	//make plant reappear
	public void revivePlant(int pick)
	{
		plantImgs.get(pick).setVisible(true);
	}*/
	
	
	
	public void setKeyListener(KeyListener listener) {
		frame.addKeyListener(listener);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Sprite.incrementFrameCounter();
		drawImage(g, Sprite.ID.BACKGROUND, 0, 0);

		for(Plant plant : Plant.plants) {
			drawImage(g, Sprite.ID.PLANT, plant.getXLocation(), plant.getYLocation());
		}
		
		//traverse through litter set and draw them, had to make a copy of litter set everytime to avoid ConcurrentModificationExceptions.
		for(Litter l: new HashSet<Litter>(Litter.litterSet)) {
			g.drawImage(l.getlitterImage(),
				convertDimension(l.getXLocation()),
				convertDimension(l.getYLocation()),
				l.getHeight(),
				l.getWidth(),
				this);
		}

		g.setColor(Color.RED);
		g.setFont(new Font("TimesRoman", Font.BOLD, 25)); 
		g.drawString(""+plant0H, 550, 100);//change to make it the spacing as the plant jlabels
		g.drawString(""+plant1H, 550, 260);
		g.drawString(""+plant2H, 550, 460);
		g.drawString(""+plant3H, 550, 660);
		g.setColor(Color.PINK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString(coords, 10, 20);
		drawImage(g, Sprite.ID.CRAB, crabXLoc, crabYLoc);
		drawImage(g, getPlayerSprite(), playerXLoc, playerYLoc);
		
		
	}

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

	// convertDimension: converts a dimension from world coordinates to pixel coordinates
	private int convertDimension(int world_dimension) {
		return (int) ((double) world_dimension / Controller.WORLD_WIDTH * this.getWidth());
	}

	private BufferedImage createImage(int pictureIndex) {
		BufferedImage bufferedImage;
		// System.out.println(imgName + dir.getName());
		try {
			bufferedImage = ImageIO.read(new File("images/Animal/skeleton-idle_" + pictureIndex + ".png"));
			return bufferedImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

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

	public void update(int playerX, int playerY, Direction dir, PlayerStatus status, int crabX, int crabY) {
		playerXLoc = playerX;
		playerYLoc = playerY;
		playerDirection = dir;
		playerStatus = status;

		crabXLoc = crabX;
		crabYLoc = crabY;
		
		frame.repaint();
	}
	
	/**Sets the Litter object to a randomly selected image that is appropriate for its LitterType
	 * 
	 * @param t The Litter object the image will be set for.
	 */
	public void setLitterImage(Litter l) {
		switch(l.getType()){
		case TRASH:
			l.setlitterImage(trashImgs[(int)(Math.random()*trashImgs.length)]);
			break;
		case RECYCLABLE:
			l.setlitterImage(recyclableImgs[(int)(Math.random()*recyclableImgs.length)]);
		}
		
	}
	
	
	/**Loads in the different Litter images to be used in the game. 
	 * 
	 */
	public void preloadLitterImgs() {
		trashImgs[0] = loadImg("bananaSkin");
		trashImgs[1] = loadImg("CompostA");
		
		recyclableImgs[0] = loadImg("Soda-Can");
		recyclableImgs[1] = loadImg("paper");
		
		
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
