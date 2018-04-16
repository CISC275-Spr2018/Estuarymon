

public enum Direction {

	NORTH("north"),
	EAST("east"),
	SOUTH("south"),
	WEST("west");

	public static final int LENGTH = Direction.values().length;
	
	private String name = null;
	
	private Direction(String s){
		name = s;
	}
	public String getName() {
		return name;
	}


}
