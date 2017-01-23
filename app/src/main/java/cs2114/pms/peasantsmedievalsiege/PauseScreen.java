package cs2114.pms.peasantsmedievalsiege;

import android.content.Intent;
import sofia.app.Screen;

// -------------------------------------------------------------------------
/**
 * The pause screen that opens when the phone is locked or when the user presses
 * back on their device. Provides the option to exit the game or to resume play.
 * Used to maintain the state of the game on device lock.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class PauseScreen
    extends Screen
{

    // ----------------------------------------------------------
    /**
     * If the user wishes to return to the game, then simply invoke the back
     * press which would return to the previous activity (the game).
     */
    public void backToGameClicked()
    {
        super.onBackPressed();
        finish();
    }


    // ----------------------------------------------------------
    /**
     * If the user wishes to exit the game, then send the user to the main menu
     */
    public void exitClicked()
    {
        Intent i = new Intent(this, MainMenu.class);
        setResult(2);
        startActivity(i);
        finish();
    }
}
