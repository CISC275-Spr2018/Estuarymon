
public class Player extends Interactable {
	public static boolean hasLitter = false;
	public LitterType litterType;
	private int dx = 0;
	private int dy = 0;
	private final int speed = 10;
	private Direction direction = Direction.EAST;
	private PlayerStatus status = PlayerStatus.IDLE;
	
	public Player(int xLoc, int yLoc, int rWidth, int rHeight) {
		super(xLoc, yLoc, rWidth, rHeight);
		this.setRelativeCollisionRect(40, 40, rWidth-80, rHeight-80);
	}
	
	public boolean shouldCollectLitter(Litter l) {
		if(this.getCollidesWith(l) && !hasLitter) {
			litterType = l.getType();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean shouldDepositLitter(Receptacle r) {
		if(this.getCollidesWith(r) && hasLitter && r.getType().ordinal() == litterType.ordinal()) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean hasLitter() {
		return this.hasLitter;
	}

	public void pickUpLitter(Litter l) {
		// TODO
		System.out.println("Player pick up litter "+l.toString());
	}
	
	public void growPlant(int i) {
		// TODO
		System.out.println("Plant!");
		//restore health and pick new plant
		Plant.plants[i].health = 100;
		Plant.randPlant = (int) Math.floor(Math.random() * 4);
	}

	public void alterVelocity(int ddx, int ddy) {
		this.dx += ddx;
		this.dy += ddy;
		// Catch when set too high/low because of key repeating
		if(this.dx < -1) this.dx = -1;
		else if(this.dx > 1) this.dx = 1;
		if(this.dy < -1) this.dy = -1;
		else if(this.dy > 1) this.dy = 1;

		// Fix direction and status
		this.status = PlayerStatus.WALKING;
		if(dx < 0) {
			// left
			if(dy < 0) {
				// up
				this.direction = Direction.NORTHWEST;
			} else if(dy > 0) {
				// down
				this.direction = Direction.SOUTHWEST;
			} else {
				// horizontal
				this.direction = Direction.WEST;
			}
		} else if(dx > 0) {
			// right
			if(dy < 0) {
				// up
				this.direction = Direction.NORTHEAST;
			} else if(dy > 0) {
				// down
				this.direction = Direction.SOUTHEAST;
			} else {
				// horizontal
				this.direction = Direction.EAST;
			}
		} else {
			// vertical
			if(dy < 0) {
				// up
				this.direction = Direction.NORTH;
			} else if(dy > 0) {
				// down
				this.direction = Direction.SOUTH;
			} else {
				// horizontal
				// DON'T MODIFY DIRECTION
				// But do fix status.
				this.status = PlayerStatus.IDLE;
			}
		}
	}

	public void move() {
		this.addXLocation(this.speed*this.dx);
		this.addYLocation(this.speed*this.dy);
	}

	public Direction getDirection() {
		return this.direction;
	}

	public PlayerStatus getStatus() {
		return this.status;
	}
}
