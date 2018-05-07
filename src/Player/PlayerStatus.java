package Player;
/** PlayerStatus represents what the Player is currently doing. As of now, it only represents whether the player is {@link #IDLE} or {@link #WALKING}. In the future, more statuses may be added. */
public enum PlayerStatus {
	/** The Player is standing still, doing nothing. */
	IDLE,
	/** The Player is walking. */
	WALKING
}
