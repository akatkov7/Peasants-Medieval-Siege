package cs2114.pms.peasantsmedievalsiege;

import android.util.Log;
import student.AndroidTestCase;

/**
 * // -------------------------------------------------------------------------
 * /** Due to the fact that the game pauses after each test, only one test can
 * be ran at a time. Comment and uncomment the tests to actually test them
 *
 * @author Tony
 * @version Dec 7, 2013
 */
public class GameScreenTests
    extends AndroidTestCase<GameScreen>
{

    // ----------------------------------------------------------
    /**
     * Create a new GameScreenTests object.
     */
    public GameScreenTests()
    {
        super(GameScreen.class);
    }


    /**
     * The setUp method, blank on purpose
     */
    public void setUp()
    {
        // blank on porpoise
    }


    /**
     * Tests the various aspects of scrolling
     */
    public void testScrolling()
    {
        Tower referenceTower =
            getScreen().getPlayer(PlayerType.LEFT).getBaseTower();
        float initX = referenceTower.targetableX();
        float initY = referenceTower.targetableY();
        // tests not able to scroll
        touchDown(30, 50);
        moveTouch(50, 50);
        assertEquals(initX, referenceTower.targetableX(), .01);
        assertEquals(initY, referenceTower.targetableY(), .01);
        // tests scrolling left
        touchDown(30, 50);
        moveTouch(10, 50);
        assertEquals(initX - 20, referenceTower.targetableX(), .01);
        assertEquals(initY, referenceTower.targetableY(), .01);
        // final size = 192*2
        touchDown(172, 50);
        moveTouch(0, 53);
        assertEquals(initX - (192), referenceTower.targetableX(), .01);
        assertEquals(initY, referenceTower.targetableY(), .01);
        // can't scroll left anymore?
        touchDown(30, 50);
        moveTouch(10, 50);
        assertEquals(initX - (192), referenceTower.targetableX(), .01);
        assertEquals(initY, referenceTower.targetableY(), .01);
    }


    /**
     * Tests the on touch down method for the right player. NOTE: the
     * Thread.sleep()s are so the tester can actually see if the test is working
     * correctly
     *
     * @throws Exception
     *             so that the user can see the tests are working by the small
     *             pauses
     */
    public void testRightOnTouchDown()
        throws Exception
    {
        Player rightPlayer = getScreen().getPlayer(PlayerType.RIGHT);
        rightPlayer.getProfile().setLevel(1);
        rightPlayer.getProfile().setXP(0);
        // Tests the all the touching for the right player
        touchDown(172.65f, 95.47f);//finish left's turn
        //start by scrolling all the way to the right
        touchDown(192, 50);
        moveTouch(0, 50);//scroll
        float initX = rightPlayer.getBaseTower().targetableX();

        touchDown(109.55f, 95);// click on right tower button
        Thread.sleep(1000);
        touchDown(0, 0);// click aimlessly
        Thread.sleep(1000);
        touchDown(109.55f, 95);// click on right tower button
        Thread.sleep(1000);
        touchDown(109.55f, 95);// click on right tower button
        Thread.sleep(1000);
        touchDown(62, 95);// click on right minion button
        Thread.sleep(1000);
        touchDown(0, 0);// click aimlessly
        Thread.sleep(1000);
        touchDown(51.55f, 85);// click in minion popup menu (not on minion)
        Thread.sleep(1000);
        touchDown(62, 95);// click on right minion button
        Thread.sleep(1000);
        touchDown(99.54f, 60f);// click on first tower
        Thread.sleep(1000);
        touchDown(0, 0);// click aimlessly
        Thread.sleep(1000);
        touchDown(15.2f, 95);// click on red button
        Thread.sleep(1000);
        touchDown(109.55f, 95);// click on right tower popup
        Thread.sleep(1000);
        touchDown(99.54f, 63.8f);// click on first tower
        Thread.sleep(1000);
        touchDown(29.1f, 95);// click on green button
        Thread.sleep(1000);
        assertEquals(2, rightPlayer.getTowers().size());
        touchDown(62.1f, 99.8f);// click on the minion popup
        Thread.sleep(1000);
        touchDown(51.55f, 63.65f);// click on the first minion
        Thread.sleep(1000);
        assertEquals(1, rightPlayer.getMinions().size());
        touchDown(51.55f, 63.65f);// click on the first minion
        assertEquals(2, rightPlayer.getMinions().size());
        // out of money, shouldn't be able to place minion or tower
        touchDown(51.55f, 63.65f);// click on the first minion
        assertEquals(2, rightPlayer.getMinions().size());
        touchDown(62.1f, 95);// click on right minion popup
        Thread.sleep(1000);
        touchDown(109.55f, 95);// click on the right tower popup
        Thread.sleep(1000);
        touchDown(99.54f, 63.8f);// click on the first tower
        touchDown(109.55f, 95);// click on the right tower popup
        assertEquals(2, rightPlayer.getTowers().size());
        // try to click on the nonexistant tower/minion
        touchDown(109.55f, 95);// click on the right tower popup
        Thread.sleep(1000);
        touchDown(110.36f, 63.56f);// click on (invisible) second tower
        Thread.sleep(1000);
        // update the player to be lvl 2 and have enough money
        rightPlayer.addXP(40000);
        rightPlayer.updateCoins(175 - rightPlayer.getCoins());
        touchDown(109.55f, 95);// click on right popup
        Thread.sleep(1000);
        touchDown(110.36f, 63.56f);// click on visible second tower
        Thread.sleep(1000);
        touchDown(29.1f, 95);// click accept (shouldn't work)
        assertEquals(2, rightPlayer.getTowers().size());
        Thread.sleep(1000);
        touchDown(102, 75.73f);// start to drag
        for (int i = -1; i > -48; i--)// drag the tower (slowly) off the first one
        {
            moveTouch(102 + i, 76);
        }
        Thread.sleep(1000);
        touchDown(102, 79);// try to scroll below halfway with tower out (can't)
        for (int i = 1; i < 50; i++)
        {
            moveTouch(102 + i, 80);
        }
        // make sure it hasn't scrolled
        assertEquals(initX, rightPlayer.getBaseTower().targetableX(), .01);
        touchDown(102, 10);
        for (int i = 1; i < 50; i++)// scroll above halfway with tower out
        {
            moveTouch(102 + i, 10);
        }
        assertEquals(initX + 49, rightPlayer.getBaseTower().targetableX(), .01);
        touchDown(102, 10);
        moveTouch(53, 10);// move back to initial scroll (all the way left)
        touchDown(29.1f, 95);// now place the tower
        assertEquals(3, rightPlayer.getTowers().size());
        // now update it for the second minion
        rightPlayer.updateCoins(100);
        touchDown(62.1f, 95);// click left minion popup
        Thread.sleep(1000);
        touchDown(62.8f, 63.56f);// click left second minion
        Thread.sleep(1000);
        assertEquals(3, rightPlayer.getMinions().size());
    }


    /**
     * Tests the on touch down method for the left player. NOTE: the
     * Thread.sleep()s are so the tester can actually see if the test is working
     * correctly
     *
     * @throws Exception
     *             so that the user can see the tests are working by the small
     *             pauses
     */
    public void testLeftOnTouchDown()
        throws Exception
    {
        Player leftPlayer = getScreen().getPlayer(PlayerType.LEFT);
        leftPlayer.getProfile().setLevel(1);
        leftPlayer.getProfile().setXP(0);
        float initX = leftPlayer.getBaseTower().targetableX();
        // Tests the all the touching for the left player
        touchDown(70, 95);// click on left tower button
        Thread.sleep(1000);
        touchDown(0, 0);// click aimlessly
        Thread.sleep(1000);
        touchDown(70, 95);// click on left tower button
        Thread.sleep(1000);
        touchDown(70, 95);// click on left tower button
        Thread.sleep(1000);
        touchDown(120, 95);// click on left minion button
        Thread.sleep(1000);
        touchDown(0, 0);// click aimlessly
        Thread.sleep(1000);
        touchDown(130, 70);// click in minion popup menu (not on minion)
        Thread.sleep(1000);
        touchDown(120, 95);// click on left minion button
        Thread.sleep(1000);
        touchDown(66.4f, 63.8f);// click on first tower
        Thread.sleep(1000);
        touchDown(0, 0);// click aimlessly
        Thread.sleep(1000);
        touchDown(185, 95);// click on red button
        Thread.sleep(1000);
        touchDown(70, 95);// click on left tower popup
        Thread.sleep(1000);
        touchDown(66.4f, 63.8f);// click on first tower
        Thread.sleep(1000);
        touchDown(164, 95);// click on green button
        Thread.sleep(1000);
        assertEquals(2, leftPlayer.getTowers().size());
        touchDown(122.6f, 99.8f);// click on the minion popup
        Thread.sleep(1000);
        touchDown(118, 63.65f);// click on the first minion
        Thread.sleep(1000);
        assertEquals(1, leftPlayer.getMinions().size());
        touchDown(118, 63.65f);// click on the first minion
        assertEquals(2, leftPlayer.getMinions().size());
        // out of money, shouldn't be able to place minion or tower
        touchDown(118, 63.65f);// click on the first minion
        assertEquals(2, leftPlayer.getMinions().size());
        touchDown(120, 95);// click on left minion popup
        Thread.sleep(1000);
        touchDown(70, 95);// click on the left tower popup
        Thread.sleep(1000);
        touchDown(66.4f, 63.8f);// click on the first tower
        touchDown(70, 95);// click on the left tower popup
        assertEquals(2, leftPlayer.getTowers().size());
        // try to click on the nonexistant tower/minion
        touchDown(70, 95);// click on the left tower popup
        Thread.sleep(1000);
        touchDown(83, 63.56f);// click on (invisible) second tower
        Thread.sleep(1000);
        // update the player to be lvl 2 and have enough money
        leftPlayer.addXP(40000);
        leftPlayer.updateCoins(175 - leftPlayer.getCoins());
        touchDown(70, 95);// click on left popup
        Thread.sleep(1000);
        touchDown(83, 63.56f);// click on visible second tower
        Thread.sleep(1000);
        touchDown(164, 95);// click accept (shouldn't work)
        assertEquals(2, leftPlayer.getTowers().size());
        Thread.sleep(1000);
        touchDown(102, 75.73f);// start to drag
        for (int i = 1; i < 48; i++)// drag the tower (slowly) off the first one
        {
            moveTouch(102 + i, 76);
        }
        Thread.sleep(1000);
        touchDown(102, 79);// try to scroll below halfway with tower out (can't)
        for (int i = -1; i > -50; i--)
        {
            moveTouch(102 + i, 80);
        }
        // make sure it hasn't scrolled
        assertEquals(initX, leftPlayer.getBaseTower().targetableX(), .01);
        touchDown(102, 10);
        for (int i = -1; i > -50; i--)// scroll above halfway with tower out
        {
            moveTouch(102 + i, 10);
        }
        assertEquals(initX - 49, leftPlayer.getBaseTower().targetableX(), .01);
        touchDown(102, 10);
        moveTouch(151, 10);// move back to initial scroll (all the way left)
        touchDown(164, 95);// now place the tower
        assertEquals(3, leftPlayer.getTowers().size());
        // now update it for the second minion
        leftPlayer.updateCoins(100);
        touchDown(120, 95);// click left minion popup
        Thread.sleep(1000);
        touchDown(131, 63.56f);// click left second minion
        Thread.sleep(1000);
        assertEquals(3, leftPlayer.getMinions().size());
    }


    /**
     * Calls the onTouchDown method in GameScreen to test touching the screen
     */
    private void touchDown(final float x, final float y)
    {
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run()
            {
                getScreen().onTouchDown(x, y);
            }
        });
    }


    /**
     * Calls the onTouchMove method in GameScreen to test moving the finger on
     * the screen
     */
    private void moveTouch(final float x, final float y)
    {
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run()
            {
                getScreen().onTouchMove(x, y);
            }
        });
    }
}
