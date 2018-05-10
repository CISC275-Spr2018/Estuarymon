package MVC;

/** Represents the current phase of the game the player is in. Could be something like "title screen" or "tutorial". */
public enum GamePhase {
	/** Game is displaying the Title Screen */
	TITLE_SCREEN,
	/** Game is showing the tutorial */
	TUTORIAL,
	/** Game is in normal play */
	NORMAL,
	/** Game has ended */
	GAME_END
}
