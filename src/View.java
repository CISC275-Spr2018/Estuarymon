import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
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
	private static int xloc = 0;
	private static int yLoc = 0;
	private static Direction curDir = Direction.EAST;
	private static final Color BACKGROUND_COLOR = Color.GRAY;
	static int bCount;
	Animation animation = Animation.IDLE;



	public View() {
		// Preload animations
		Animation.preload();

		JLabel plant;
		ArrayList<JLabel> plantImgs = new ArrayList<JLabel>();
		int count = 0;
		for(int i = 0; i < 4; i++)
		{
			plant = new JLabel();
			//loads plant image,converts it to icon, adds icon to label, resizes image
			ImageIcon plantIcon = new ImageIcon("images/MapObjects/plant.png");
			Image plantImg = plantIcon.getImage();
			Image newImg = plantImg.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH);
			
			plant.setIcon(new ImageIcon(newImg));
			plant.setBounds(frameWidth - (frameWidth/3), (frameHeight / 100) + count, 100, 100);
			//store images in a list to set vis false layer
			plantImgs.add(plant);
			//space out images by 200
			count = count + 200;
			frame.getContentPane().add(plant);
		}
		
		frame.setFocusable(true);
		frame.getContentPane().add(this);
		frame.getContentPane().setBackground(BACKGROUND_COLOR);
		this.setBackground(BACKGROUND_COLOR);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);
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
