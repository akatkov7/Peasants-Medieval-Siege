package cs2114.pms;

import android.content.Intent;
import android.widget.Button;
import student.AndroidTestCase;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Andriy
 *  @version Dec 8, 2013
 */
public class PauseScreenTest
    extends AndroidTestCase<PauseScreen>
{
    private Button backToGame;
    private Button exit;

    /**
     * Constructor for InstuctionScreen
     */
    public PauseScreenTest()
    {
        super(PauseScreen.class);
    }

    public void testBackToGame()
    {
        assertTrue(backToGame.isEnabled());
        prepareForUpcomingActivity(Intent.ACTION_VIEW);
        click(backToGame);
    }

    public void testExit()
    {
        assertTrue(exit.isEnabled());
        prepareForUpcomingActivity(Intent.ACTION_VIEW);
        click(exit);
    }
}
