import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

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
	Animation animation = Animation.IDLE; //initial animation of the orc
	
	ArrayList<JLabel> plantImgs = new ArrayList<JLabel>();
	
	Animal crab;
	BufferedImage[] img; //loading the images of the crab
	int crabPicNum = 0; // current images of the crab


	public View(Animal animal) {
		// Preload animations
		crab = animal;
		img = crab.loadImages();
		Animation.preload();
		
		

		//PLANT SECTION**************************************
		JLabel plant;
		int count = 0;
		
		int plantXloc;
		int plantYloc;
		
		for(int i = 0; i < 4; i++)
		{
			plant = new JLabel();
			//loads plant image,converts it to icon, adds icon to label, resizes image
			ImageIcon plantIcon = new ImageIcon("images/MapObjects/plant.png");
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
		g.drawImage(this.animation.getCurrentFrameForDirection(this.curDir), xloc, yLoc, BACKGROUND_COLOR, this);
		crabPicNum = (crabPicNum + 3) % crab.getNumOfImages(); //change the 3 to change the speed
		g.drawImage(img[crabPicNum], crab.getXLocation(), crab.getYLocation(), crab.getImageWidth(), crab.getImageHeight(), this); //drawing the crab onto the game
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
