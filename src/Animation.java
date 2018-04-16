import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// This class contains useful public static final Animations.
// You cannot create additional Animations in other files; they should all be here.
// The only public method of an Animation is getCurrentFrameForDirection(d)

public abstract class Animation {
	// public static final Animations - the most important part of this class.
	public static final Animation WALKING = new WalkingAnimation();
	public static final Animation IDLE = new IdleAnimation();
	public static final Animation JUMP = new JumpingAnimation();
	public static final Animation FIRE = new FiringAnimation();
	public static Animation PREV;
	// All subclasses must implement these methods.
	protected abstract void load(); // Load everything necessary from disk
	public abstract BufferedImage getCurrentFrameForDirection(Direction d); // Return the appropriate BufferedImage for this frame

	private Animation() {} // Prevent instantiation

	// The only other public thing - a public static void preload() method
	public static void preload() {
		WALKING.load();
		IDLE.load();
		JUMP.load();
		FIRE.load();
	}
	
	public static boolean fireEnd = false;
	public static boolean jumpEnd = false;
	
	protected void setFireEnd(boolean b) {
		this.fireEnd = b;
	}
	
	protected boolean getFireEnd() {
		return this.fireEnd;
	}
	
	protected void setJumpEnd(boolean b) {
		this.jumpEnd = b;
	}
	
	protected boolean getJumpEnd() {
		return this.jumpEnd;
	}

	//---------- Convenience methods for inside subclasses:

	protected int frameNum = 0;

	// int getFrameNum()
	// Return the frame number. Every time it is called, its return increases by one.
	protected int getFrameNum() {
		return frameNum++;
	}

	// BufferedImage sequentialFrames(BufferedImage[] imgs)
	// When called the first time, will return imgs[0].
	// Next time, imgs[1]. Et cetera. 
	// When it reaches the end, it will jump back to the beginning.
	protected BufferedImage sequentialFrames(BufferedImage[] imgs) {
		return imgs[this.getFrameNum() % imgs.length];
	}

	// loadImg(String name): Basically a convenience wrapper around ImageIO.read().
	protected BufferedImage loadImg(String name) {
		try {
			return ImageIO.read(new File("images/orc/"+name+".png"));
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	// splitTiled(BufferedImage source, int xframes, int yframes)
	// Splits a BufferedImage into multiple BufferedImages using a tiling pattern.
	// xframes is the number of tiles horizontally across the source image.
	// yframes is the number of tiles vertically across the source image.
	// The tiles or ordered in the array left-to-right, top-to-bottom
	// (like reading a book)
	protected BufferedImage[] splitTiled(BufferedImage source, int xframes, int yframes) {
		BufferedImage[] result = new BufferedImage[xframes*yframes];
		int frameW = source.getWidth()/xframes;
		int frameH = source.getHeight()/yframes;
		for(int x=0; x<xframes; x++)
			for(int y=0; y<yframes; y++)
				result[x+xframes*y] = source.getSubimage(
					x*frameW, y*frameH, frameW, frameH);
		return result;
	}

	//---------- Subclasses themselves

	private static class IdleAnimation extends Animation {
		private BufferedImage[] frames;

		@Override
		protected void load() {
			frames = this.splitTiled(
				this.loadImg("orc_idle_ewns"),
				4, 4);
		}

		public BufferedImage getCurrentFrameForDirection(Direction d) {
			int start;
			switch(d) {
				case EAST: start = 0; break;
				case WEST: start = 4; break;
				case NORTH: start = 8; break;
				case SOUTH: start = 12; break;
				default:
					throw new RuntimeException("Idle animation is not aware of the Direction "+d);
			}
			return frames[start + (this.getFrameNum() % 4)];
		}
	}

	private static abstract class BasicAnimation extends Animation {
		private BufferedImage[][] directions;

		protected void load() {
			directions = new BufferedImage[Direction.LENGTH][];
			for(Direction d : Direction.values()) {
				directions[d.ordinal()] = splitTiled(
					loadImg(this.getNameForDir(d)),
					this.getXFramesForDir(d),
					this.getYFramesForDir(d));
			}
		}
		
		protected abstract String getNameForDir(Direction dir);
		protected abstract int getXFramesForDir(Direction dir);
		protected abstract int getYFramesForDir(Direction dir);

		public BufferedImage getCurrentFrameForDirection(Direction d) {
			return sequentialFrames(directions[d.ordinal()]);
		}
	}

	private static class WalkingAnimation extends BasicAnimation {
		@Override
		protected String getNameForDir(Direction dir) {
			return "orc_forward_"+dir.getName();
		}

		@Override
		protected int getXFramesForDir(Direction dir) {
			return 10;
		}

		@Override
		protected int getYFramesForDir(Direction dir) {
			return 1;
		}
	}
	
	private static class JumpingAnimation extends BasicAnimation{
		
		@Override
		protected String getNameForDir(Direction dir) {
			return "orc_jump_" + dir.getName();
		}
		
		@Override
		protected int getXFramesForDir(Direction dir) {
			return 8;
			
			
		}
		
		@Override
		protected int getYFramesForDir(Direction dir) {
			return 1;
		}
		
		@Override
		protected BufferedImage sequentialFrames(BufferedImage[] imgs) {
			//System.out.println(this.frameNum);
			if(this.frameNum< 8) {
			//System.out.println(this.frameNum);
			return imgs[this.getFrameNum() % imgs.length];
			}
			else {
				//System.out.println("Jump Animation Looped");
				this.setFireEnd(true); 
				//System.out.println("setting jumpEnd to true");
				this.frameNum = 0;
				return imgs[this.getFrameNum() % imgs.length];
			}
			
		}
	}
	
	private static class FiringAnimation extends BasicAnimation{
		@Override
		protected String getNameForDir(Direction dir) {
			return "orc_fire_" + dir.getName();
		}
		
		@Override
		protected int getXFramesForDir(Direction dir) {
			return 4;
			
			
		}
		
		@Override
		protected int getYFramesForDir(Direction dir) {
			return 1;
		}
		@Override
		protected BufferedImage sequentialFrames(BufferedImage[] imgs) {
			//System.out.println("We are in the Overriden sequential frames");
			if(this.frameNum< 4) {
				//System.out.println(this.frameNum %imgs.length);
			return imgs[this.getFrameNum() % imgs.length];
			}
			else {
				//System.out.println("Fire Animation Looped");
				fireEnd = true;
				this.frameNum = 0;
				return imgs[this.frameNum % imgs.length];
			}
			}
		}

	}

