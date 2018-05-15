package MVC;

/** Represents the current phase of the game the player is in. Could be something like "title screen" or "tutorial". */
public enum GamePhase {
	/** Game is displaying the Title Screen */
	TITLE_SCREEN(false),
	/** Game is showing the tutorial */
	TUTORIAL(true),
	/** Game is in normal play */
	NORMAL(true),
	/** Game has ended */
	GAME_END(false);

	/** Whether the game can be played normally during this phase. */
	private boolean playable;

	/** Constructs a GamePhase with the given {@link #playable} attribute.
	 *  @param playable Whether the game can be played normally during this phase.
	 */
	private GamePhase(boolean playable) {
		this.playable = playable;
	}

	/** Returns whether the game is playable during this phase. */
	public boolean isPlayable() {
		return playable;
	}
}
