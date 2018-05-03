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
	private final static int imgWidth = 165;
	private final static int imgHeight = 165;
	private final static Dimension  screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
	private final static double screenHeight = screenDimension.getHeight();
	private final static double screenWidth = screenDimension.getWidth();
	JFrame frame = new JFrame();
	private static int xloc = 0; //player x location
	private static int yLoc = 0; //player y location
	private static Direction curDir = Direction.EAST; //the direction the orc faces when it begins
	private static final Color BACKGROUND_COLOR = Color.GRAY;
	static int bCount;
	private static final int trashImgCount = 2;
	private static final int recImgCount = 1;
	private static final int litterCount = 20;
	Animation animation = Animation.IDLE;
	private BufferedImage[] trashImgs = new BufferedImage[trashImgCount+1];
	private BufferedImage[] recyclableImgs = new BufferedImage[recImgCount+1];
	private Litter[] litterArr = new Litter[litterCount];
	
	ArrayList<JLabel> plantImgs = new ArrayList<JLabel>();
	
	//these plants vars for alpha testing
	int plant0H;
	int plant1H;
	int plant2H;
	int plant3H;
	String coords = "";
	Animal crab;
	BufferedImage[] img; //loading the images of the crab
	int crabPicNum = 0; // current images of the crab


	public View(Animal animal) {
		// Preload animations
		crab = animal;
		img = crab.loadImages();
		Animation.preload();
		
		
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
		frame.setSize(Controller.WORLD_WIDTH, Controller.WORLD_HEIGHT);
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
	
	
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void setKeyListener(KeyListener listener) {
		frame.addKeyListener(listener);
	}

	ImageIcon plantIcon = new ImageIcon("images/MapObjects/azalea.png");
	Image plantImg = plantIcon.getImage();
	Image newImg = plantImg.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH);
	
	public void paint(Graphics g) {
		super.paint(g);
		ImageIcon backg = new ImageIcon("images/Map/Background.jpg");
		g.drawImage(backg.getImage(),0,0,this);
		
		crabPicNum = (crabPicNum + 3) % crab.getNumOfImages(); //change the 3 to change the speed
		
		
		if(Plant.plants[0].health > 0)
		{
			g.drawImage(newImg, Plant.plants[0].getXLocation(),Plant.plants[0].getYLocation(),this);	
		}
		if(Plant.plants[1].health > 0)
		{
			g.drawImage(newImg,Plant.plants[1].getXLocation(),Plant.plants[1].getYLocation(),this);	
		}
		if(Plant.plants[2].health > 0)
		{
			g.drawImage(newImg, Plant.plants[2].getXLocation(),Plant.plants[2].getYLocation(),this);	
		}
		if(Plant.plants[3].health > 0)
		{
			g.drawImage(newImg, Plant.plants[3].getXLocation(),Plant.plants[3].getYLocation(),this);	
		}
		
		//traverse through litter set and draw them, had to make a copy of litter set everytime to avoid ConcurrentModificationExceptions.
		for(Litter l: new HashSet<Litter>(Litter.litterSet)) {
			g.drawImage(l.getlitterImage(), l.getXLocation(), l.getYLocation(),l.getHeight(),l.getWidth(), this);
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
		g.drawImage(img[crabPicNum], crab.getXLocation(), crab.getYLocation(), crab.getImageWidth(), crab.getImageHeight(), this); //drawing the crab onto the game
		drawImage(g, this.animation.getCurrentFrameForDirection(this.curDir), xloc, yLoc);
		
		
	}

	private void drawImage(Graphics g, BufferedImage image, int world_x, int world_y) {
		g.drawImage(image, cD(world_x), cD(world_y), this);
	}

	private void drawImage(Graphics g, BufferedImage image, int world_x, int world_y, int world_w, int world_h) {
		g.drawImage(image, cD(world_x), cD(world_y), cD(world_w), cD(world_h), this);
	}

	// convertDimension: converts a dimension from world coordinates to pixel coordinates
	private int convertDimension(int world_dimension) {
		return (int) ((double) world_dimension / Controller.WORLD_WIDTH * this.getWidth());
	}

	// Shorthand syntax for convertDimension
	private int cD(int d) {
		return convertDimension(d);
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

	public int getImageHeight() {
		return imgHeight;
	}

	public int getImageWidth() {
		return imgWidth;
	}

	public void update(int x, int y, Direction dir) {
		xloc = x;
		yLoc = y;
		curDir = dir;
		//System.out.println(animation.jumpEnd);
		if(this.animation.getFireEnd() || this.animation.getJumpEnd()) {
			//System.out.println("Setting Animation to Idle");
			setAnimation(Animation.IDLE);
			this.animation.setFireEnd(false);
			this.animation.setJumpEnd(false);
		}
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
