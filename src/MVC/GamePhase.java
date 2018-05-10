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

	private boolean playable;

	private GamePhase(boolean playable) {
		this.playable = playable;
	}

	public boolean isPlayable() {
		return playable;
	}
}
