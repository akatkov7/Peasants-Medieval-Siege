package cs2114.pms;

import android.content.Intent;
import android.widget.Toast;
import sofia.app.ShapeScreen;

// -------------------------------------------------------------------------
/**
 * The MainMenu class is displayed upon app startup. It provides the user with
 * the options to start a game and view the instructions.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class MainMenu
    extends ShapeScreen
{
    private boolean exitFlag = false;

    @Override
    public void onBackPressed()
    {
        // the user must press back twice in order to exit the app
        if (!exitFlag)
        {
            exitFlag = true;
            Toast.makeText(
                this,
                "Press back again to exit...",
                Toast.LENGTH_SHORT).show();
        }
        else
        {
            finish();
        }
    }

    // ----------------------------------------------------------
    /**
     * Switches the screen from the main menu to the profile screen.
     */
    public void startClicked()
    {
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1)
        {
            finish();
        }
    }


    // ----------------------------------------------------------
    /**
     * Switches the screen from the main menu to the instructions screen.
     */
    public void instructionsClicked()
    {
        Intent intent = new Intent(this, InstructionScreen.class);
        // started with a code of 5 so that the test class can close it
        startActivityForResult(intent, 5);
    }
}
