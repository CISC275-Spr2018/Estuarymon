import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class View extends JPanel{
	private final static int frameWidth = 900;
	private final static int frameHeight = 900;
	private final static int imgWidth = 165;
	private final static int imgHeight = 165;
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
	private BufferedImage[] recImgs = new BufferedImage[recImgCount+1];
	private Litter[] litterArr = new Litter[litterCount];
	
	ArrayList<JLabel> plantImgs = new ArrayList<JLabel>();
	
	Animal crab;
	BufferedImage[] img; //loading the images of the crab
	int crabPicNum = 0; // current images of the crab


	public View(Animal animal) {
		// Preload animations
		crab = animal;
		img = crab.loadImages();
		Animation.preload();
		
		
		preloadLitterImgs();
		
		
		
		

		//PLANT SECTION**************************************
		JLabel plant;
		int count = 0;
		
		int plantXloc;
		int plantYloc;
		
		for(int i = 0; i < 4; i++)
		{
			plant = new JLabel();
			//loads plant image,converts it to icon, adds icon to label, resizes image
			ImageIcon plantIcon = new ImageIcon("images/MapObjects/azalea.png");
			Image plantImg = plantIcon.getImage();
			Image newImg = plantImg.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH);
			
			plant.setIcon(new ImageIcon(newImg));
		
			//set this in plant
			plantXloc = frameWidth - (frameWidth/3);
			plantYloc = (frameHeight / 100) + count;
			
			//set plant in array vars to these^
			plant.setBounds(plantXloc, plantYloc, 100, 100);
			//store images in a list to set vis false layer
			plantImgs.add(plant);
			//space out images by 200
			count = count + 200;
			frame.getContentPane().add(plant);
		}
		//********************************************************************8
		
		
		frame.setFocusable(true);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(this);
		frame.getContentPane().setBackground(BACKGROUND_COLOR);
		this.setBackground(BACKGROUND_COLOR);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);
	}

	//makes plant dissappear if health is zero
	public void deletePlant(int pick)
	{
		plantImgs.get(pick).setVisible(false);
	}
	//make plant reappear
	public void revivePlant(int pick)
	{
		plantImgs.get(pick).setVisible(true);
	}
	
	
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void setKeyListener(KeyListener listener) {
		frame.addKeyListener(listener);
	}

	public void paint(Graphics g) {
		super.paint(g);
		ImageIcon backg = new ImageIcon("images/Map/Background.jpg");
		g.drawImage(backg.getImage(),0,0,this);
		g.drawImage(this.animation.getCurrentFrameForDirection(this.curDir), xloc, yLoc, BACKGROUND_COLOR, this);
		crabPicNum = (crabPicNum + 3) % crab.getNumOfImages(); //change the 3 to change the speed
		g.drawImage(img[crabPicNum], crab.getXLocation(), crab.getYLocation(), crab.getImageWidth(), crab.getImageHeight(), this); //drawing the crab onto the game
		
		//traverse through litter set and draw them, had to make a copy of litter set everytime to avoid ConcurrentModificationExceptions.
		for(Litter l: new HashSet<Litter>(Litter.litterSet)) {
			g.drawImage(l.getlitterImage(), l.getXLocation(), l.getYLocation(), BACKGROUND_COLOR, this);
		}
	}

	public int getHeight() {
		return frameHeight;
	}

	public int getWidth() {
		return frameWidth;
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
	
	//made a BufferedImage attribute in the Litter class, this method assigns a random image to the litter after it has been created. 
	public void setLitterImage(Trash t) {
		t.setlitterImage(trashImgs[(int)(Math.random()*trashImgs.length)]);
	}
	
	public void setLitterImage(Recyclable r) {
		r.setlitterImage(recImgs[(int)(Math.random()*recImgs.length)]);
	}
	
	//method to load in different trash or recyclable images that can be displayed. 
	public void preloadLitterImgs() {
		trashImgs[0] = loadImg("bananaSkin");
		trashImgs[1] = loadImg("CompostA");
		
		recImgs[0] = loadImg("Soda-Can");
		recImgs[1] = loadImg("paper");
		
		for(int i = 0; i < trashImgCount; i++) {
			BufferedImage tempImg = new BufferedImage(60,60,trashImgs[i].getType());
			Graphics2D g2d = tempImg.createGraphics();
			g2d.drawImage(trashImgs[i], 0,0,null);
			g2d.dispose();
			trashImgs[i] = tempImg;
		}
		
		for(int i = 0; i < recImgCount; i++) {
			BufferedImage tempImg = new BufferedImage(60,60,recImgs[i].getType());
			Graphics2D g2d = tempImg.createGraphics();
			g2d.drawImage(recImgs[i], 0,0,null);
			g2d.dispose();
			recImgs[i] = tempImg;
		}
		
		
		
	}
	
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
