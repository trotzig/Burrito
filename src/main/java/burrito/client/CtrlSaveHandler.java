package burrito.client;
/**
 * Register instances of this interface in {@link Burrito} to listen to "ctrl+s" key presses.
 * 
 * @author henper
 *
 */
public interface CtrlSaveHandler {

	/**
	 * Called when user presses "ctrl" and "s" at the same time.
	 */
	void onCtrlSave();
	
}
