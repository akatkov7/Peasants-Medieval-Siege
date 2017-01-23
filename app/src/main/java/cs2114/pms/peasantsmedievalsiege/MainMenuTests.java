package cs2114.pms.peasantsmedievalsiege;

import android.content.Intent;
import android.widget.Button;
import student.AndroidTestCase;

/**
 * // -------------------------------------------------------------------------
 * /** Write a one-sentence summary of your class here. Follow it with
 * additional details about its purpose, what abstraction it represents, and how
 * to use it.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.12.07
 */
public class MainMenuTests
    extends AndroidTestCase<MainMenu>
{
    private Button start;
    private Button instructions;


    /**
     * Constructor for MainMenu
     */
    public MainMenuTests()
    {
        super(MainMenu.class);
    }

    /**
     * empty setUp on purpose
     */
    public void setUp()
    {
        // nothing here
    }


    /**
     * Tests pressing the back button on the actual android device
     */
    public void testOnBackPressed()
    {
        assertTrue(start.isEnabled());
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run()
            {
                getScreen().onBackPressed();
            }
        });
        assertTrue(start.isEnabled());
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run()
            {
                getScreen().onBackPressed();
            }
        });
    }


    /**
     * Tests force exiting the game
     */
    public void testExit()
    {
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run()
            {
                getScreen().onActivityResult(0, 0, null);
            }
        });
        assertTrue(start.isEnabled());
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run()
            {
                getScreen().onActivityResult(0, 1, null);
            }
        });
    }


    /**
     * Tests the start button
     */
    public void testStartClicked()
    {
        prepareForUpcomingActivity(Intent.ACTION_VIEW);
        click(start);
    }


    /**
     * Tests the instructions button
     */
    public void testInstructionsClicked()
    {
        prepareForUpcomingActivity(Intent.ACTION_VIEW);
        click(instructions);
        getActivity().finishActivity(5);
    }

}
