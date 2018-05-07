package MVC;
/** This class kickstarts the game using it's {@link #main} method */
public class MVCMain{
	/** Kickstarts the game, ignoring arguments. Sinply instantiates a Controller and starts it.
	 *  @param args Ignored
	 */
	public static void main(String[] args){
		Controller myController = new Controller();
		myController.start();
	}
}


