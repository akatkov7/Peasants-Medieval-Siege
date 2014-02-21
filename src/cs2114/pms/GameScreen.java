package cs2114.pms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.widget.Toast;
import java.util.Iterator;
import java.util.LinkedList;
import sofia.app.ShapeScreen;
import sofia.graphics.Color;
import sofia.graphics.RectangleShape;
import sofia.graphics.Shape;
import sofia.graphics.ShapeField;

// -------------------------------------------------------------------------
/**
 * The GameScreen class displays the game background along with game components
 * such as towers and minions. Most of the game takes place on this screen.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class GameScreen
    extends ShapeScreen
{
    private RectangleShape     background;
    private final String       prefFile       = "ProfileStorage";
    private final int          width           = 192;
    private final int          height          = 108;
    private final int          passiveGoldGain = 25;
    private PointF             lastTouchDown;
    private Player             leftPlayer;
    private Player             rightPlayer;
    private Tower              towerBeingPlaced;
    private RectangleShape     leftCheck;
    private RectangleShape     rightCheck;
    private RectangleShape     leftCancel;
    private RectangleShape     rightCancel;
    private RectangleShape     finishTurn;
    private float              realLeft;
    private boolean            inGame          = true;
    private boolean            isAnimating     = false;
    private PlayerType         currentTurn     = PlayerType.LEFT;
    private Toast              turnNotifier;
    private LinkedList<Minion> deadMinions     = new LinkedList<Minion>();
    private LinkedList<Tower>  deadTowers      = new LinkedList<Tower>();


    // ----------------------------------------------------------
    /**
     * Sets up the background and instantiates the various game components such
     * as the check and cancel buttons, the finish button, as well as set up the
     * players who will add lots of components in the Player class.
     */
    public void initialize()
    {
        // resizes coordinate system
        getCoordinateSystem().width(width).height(height);
        // stores last point where user touched
        lastTouchDown = new PointF(0.0f, 0.0f);
        // sets up background
        background = new RectangleShape(0, 0, 2 * width, height);
        background.setActive(false);
        background.setImage("gamebackground");
        background.setZIndex(-100000);
        this.add(background);
        // stores the "real" left side of the screen so that when the user
        // scrolls, we know where the object should be placed relative
        // to the full size of the canvas
        realLeft = 0;
        // sets up the finish turn button
        finishTurn = new RectangleShape(155, 86, 192, 104);
        finishTurn.setImage("finish");
        this.add(finishTurn);
        // sets up the left check button
        leftCheck =
            new RectangleShape(
                (int)(155 - realLeft),
                86,
                (int)(173 - realLeft),
                104);
        leftCheck.setFillColor(Color.green);
        leftCheck.setVisible(false);
        this.add(leftCheck);
        // sets up the left cancel button
        leftCancel =
            new RectangleShape(
                (int)(175 - realLeft),
                86,
                (int)(192 - realLeft),
                104);
        leftCancel.setFillColor(Color.red);
        leftCancel.setVisible(false);
        this.add(leftCancel);
        // sets up the right check button
        rightCheck =
            new RectangleShape(
                (int)(19 - realLeft + width),
                86,
                (int)(37 - realLeft + width),
                104);
        rightCheck.setFillColor(Color.green);
        rightCheck.setVisible(false);
        this.add(rightCheck);
        // sets up the right cancel button
        rightCancel =
            new RectangleShape(
                (int)(-realLeft + width),
                86,
                (int)(17 - realLeft + width),
                104);
        rightCancel.setFillColor(Color.red);
        rightCancel.setVisible(false);
        this.add(rightCancel);

        // instantiates the player based on the profiles selected on the
        // previous screen
        // the intent is null in testing so this is the reason for this check
        if (getIntent() == null
            || getIntent().getStringExtra("leftPlayer") == null)
        {
            leftPlayer =
                new LeftPlayer(GameScreen.this, new Profile("default 1 0"));
            rightPlayer =
                new RightPlayer(GameScreen.this, width, new Profile(
                    "default 1 0"));
        }
        else
        {
            leftPlayer =
                new LeftPlayer(GameScreen.this, new Profile(getIntent()
                    .getStringExtra("leftPlayer")));
            rightPlayer =
                new RightPlayer(GameScreen.this, width, new Profile(getIntent()
                    .getStringExtra("rightPlayer")));
        }
        // lets the players know who goes first
        turnNotifier =
            Toast.makeText(
                this,
                leftPlayer.getProfile().getName() + "'s Turn!",
                Toast.LENGTH_SHORT);
        turnNotifier.show();
    }


    @Override
    public void onBackPressed()
    {
        // if the user tries to back out, open the pause screen
        inGame = false;
        Intent i = new Intent(this, PauseScreen.class);
        startActivityForResult(i, 2);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // the user has decided to exit the game from the pause screen
        // so the pause screen sends a request code of 2 in order to tell
        // the game activity to finish
        Log.d("onActivityResult", "Called in GameScreen with resultCode of "
            + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2)
        {
            finish();
        }
    }


    @Override
    public void onPause()
    {
        // if the user opens another application or anything that would cause
        // onPause to be called, then if the player is currently in game,
        // send them to the pause screen
        if (inGame)
        {
            onBackPressed();
        }
        super.onPause();
    }


    @Override
    public void onResume()
    {
        // once the user returns, inGame is back to true
        inGame = true;
        super.onResume();
    }


    /**
     * Processes all of the user touches on the screen.
     *
     * @param x
     *            the x-coordinate of the touch
     * @param y
     *            the y-coordinate of the touch
     */
    public void onTouchDown(float x, float y)
    {
        Log.d("touched at", x + ", " + y);
        // don't do anything if a fight is being animated
        if (!isAnimating)
        {
            // store the point touched down at
            lastTouchDown.set(x, y);
            Log.d("TOUCHDOWN", Float.toString(x) + ", " + Float.toString(y));
            if (currentTurn == PlayerType.LEFT)
            {
                // if a tower is being placed, then check if the touch
                // was inside the check or cancel buttons
                if (towerBeingPlaced != null)
                {
                    if (leftCheck.isVisible() && leftCancel.isVisible())
                    {
                        if (leftCheck.contains(x, y))
                        {
                            processLeftCheck();
                        }
                        if (leftCancel.contains(x, y))
                        {
                            processLeftCancel();
                        }
                    }
                }
                // if no tower is being placed, consider all other options
                else
                {
                    // if the user clicks on the tower menu, then go check
                    // to see if they selected a tower
                    if (leftPlayer.getTowerMenu().isShowing()
                        && leftPlayer.getTowerMenu().getBackground()
                            .contains(x, y))
                    {
                        processLeftTowerMenuSelection(x, y);
                    }
                    // open or close the tower menu based on the current state
                    if (leftPlayer.getAddTowerButton().contains(x, y))
                    {
                        if (leftPlayer.getTowerMenu().isShowing())
                        {
                            leftPlayer.getTowerMenu().collapse();
                        }
                        else
                        {
                            leftPlayer.getTowerMenu().show();
                        }
                    }
                    // if the user clicks on the minion menu, then go check
                    // to see if they selected a minion
                    if (leftPlayer.getMinionMenu().isShowing()
                        && leftPlayer.getMinionMenu().getBackground()
                            .contains(x, y))
                    {
                        processLeftMinionMenuSelection(x, y);
                    }
                    // open or close the minion menu based on current state
                    if (leftPlayer.getAddMinionButton().contains(x, y))
                    {
                        if (leftPlayer.getMinionMenu().isShowing())
                        {
                            leftPlayer.getMinionMenu().collapse();
                        }
                        else
                        {
                            leftPlayer.getMinionMenu().show();
                        }
                    }
                    // finish the player's turn
                    if (finishTurn.contains(x, y))
                    {
                        processLeftFinished();
                    }
                }
            }
            // if it's the right player's turn
            else
            {
                // if a tower is being placed, then check if the touch
                // was inside the check or cancel buttons
                if (towerBeingPlaced != null)
                {
                    if (rightCheck.isVisible() && rightCancel.isVisible())
                    {
                        if (rightCheck.contains(x, y))
                        {
                            processRightCheck();
                        }
                        else if (rightCancel.contains(x, y))
                        {
                            processRightCancel();
                        }
                    }
                }
                // if no tower is being placed, consider all other options
                else
                {
                    // if the user clicks on the tower menu, then go check
                    // to see if they selected a tower
                    if (rightPlayer.getTowerMenu().isShowing()
                        && rightPlayer.getTowerMenu().getBackground()
                            .contains(x, y))
                    {
                        processRightTowerMenuSelection(x, y);
                    }
                    // open or close the tower menu based on the current state
                    if (rightPlayer.getAddTowerButton().contains(x, y))
                    {
                        if (rightPlayer.getTowerMenu().isShowing())
                        {
                            rightPlayer.getTowerMenu().collapse();
                        }
                        else
                        {
                            rightPlayer.getTowerMenu().show();
                        }
                    }
                    // if the user clicks on the minion menu, then go check
                    // to see if they selected a minion
                    if (rightPlayer.getMinionMenu().isShowing()
                        && rightPlayer.getMinionMenu().getBackground()
                            .contains(x, y))
                    {
                        processRightMinionMenuSelection(x, y);
                    }
                    // open or close the minion menu based on the current state
                    if (rightPlayer.getAddMinionButton().contains(x, y))
                    {
                        if (rightPlayer.getMinionMenu().isShowing())
                        {
                            rightPlayer.getMinionMenu().collapse();
                        }
                        else
                        {
                            rightPlayer.getMinionMenu().show();
                        }
                    }
                    // finish the player's turn
                    if (finishTurn.contains(x, y))
                    {
                        processRightFinished();
                    }
                }
            }
        }
    }


    /**
     * Animates the fight by moving all of the minions on the board, then
     * checking all of the dead units, and resetting the tower attacks
     */
    private void animateFight()
    {
        isAnimating = true;
        // descending iterator allows for the minions to be in order
        // of the most forward minion to the back
        Iterator<Minion> itr = leftPlayer.getMinions().descendingIterator();
        while (itr.hasNext())
        {
            itr.next().move(
                rightPlayer.getMinions(),
                rightPlayer.getTowers(),
                leftPlayer.getMinions());
        }
        itr = rightPlayer.getMinions().descendingIterator();
        while (itr.hasNext())
        {
            itr.next().move(
                leftPlayer.getMinions(),
                leftPlayer.getTowers(),
                rightPlayer.getMinions());
        }
        // removes dead things
        processDeath();

        // lets the towers attack next turn
        for (Tower eachTower : leftPlayer.getTowers())
        {
            eachTower.resetAttacks();
        }
        for (Tower eachTower : rightPlayer.getTowers())
        {
            eachTower.resetAttacks();
        }
    }


    // ----------------------------------------------------------
    /**
     * Called when a minion dies.
     *
     * @param m
     *            the minion that died
     */
    public void changeWasObserved(Minion m)
    {
        Log.d("changeWasObserved", "Minion died");
        deadMinions.add(m);
    }


    // ----------------------------------------------------------
    /**
     * Called when a tower dies.
     *
     * @param t
     *            the tower that died
     */
    public void changeWasObserved(Tower t)
    {
        Log.d("changeWasObserved", "Tower died");
        deadTowers.add(t);
    }


    /**
     * Processes the death of units. Goes through all of the dead minions and
     * towers. Award money and XP to the other user based on the deaths. Then,
     * clear the list of dead minions and towers.
     */
    private void processDeath()
    {
        for (Minion m : deadMinions)
        {
            this.remove(m.getGraphics());
            if (m.getPlayer() == PlayerType.LEFT)
            {
                if (leftPlayer.getMinions().remove(m))
                {
                    rightPlayer.updateCoins(m.getCost() / 2);
                    rightPlayer.addXP(m.getXpGiven());
                }
            }
            if (m.getPlayer() == PlayerType.RIGHT)
            {
                if (rightPlayer.getMinions().remove(m))
                {
                    leftPlayer.updateCoins(m.getCost() / 2);
                    leftPlayer.addXP(m.getXpGiven());
                }
            }
        }
        deadMinions.clear();
        for (Tower t : deadTowers)
        {
            this.remove(t.getGraphics());
            if (t.getPlayer() == PlayerType.LEFT)
            {
                if (leftPlayer.getTowers().remove(t))
                {
                    if (t instanceof BaseTower)
                    {
                        endGame(rightPlayer);
                    }
                    rightPlayer.updateCoins(t.getCost() / 2);
                    rightPlayer.addXP(t.getXpGiven());
                }
            }
            if (t.getPlayer() == PlayerType.RIGHT)
            {
                if (rightPlayer.getTowers().remove(t))
                {
                    if (t instanceof BaseTower)
                    {
                        endGame(leftPlayer);
                    }
                    leftPlayer.updateCoins(t.getCost() / 2);
                    leftPlayer.addXP(t.getXpGiven());
                }
            }
        }
        deadTowers.clear();
    }


    /**
     * If the right player has finished his turn, close both the menus. Then,
     * animate the fight. After the fight, move the finish button to the left
     * player's side, add passive gold, and update the user on whose turn it is.
     */
    private void processRightFinished()
    {
        rightPlayer.getTowerMenu().collapse();
        rightPlayer.getMinionMenu().collapse();
        animateFight();
        currentTurn = PlayerType.LEFT;
        finishTurn.setBounds(new RectF(
            (int)(155 - realLeft),
            86,
            (int)(192 - realLeft),
            104));
        rightPlayer.updateCoins(passiveGoldGain);
        turnNotifier.setText(leftPlayer.getProfile().getName() + "'s Turn!");
        turnNotifier.show();
        isAnimating = false;
    }


    /**
     * If the user doesn't want to place the tower, then remove the tower and
     * make the finish button visible.
     */
    private void processRightCancel()
    {
        this.remove(towerBeingPlaced.getGraphics());
        towerBeingPlaced = null;
        rightCheck.setVisible(false);
        rightCancel.setVisible(false);
        finishTurn.setVisible(true);
    }


    /**
     * If the user wants to place the tower, then make sure it doesn't intersect
     * other towers, then subtract the coins and make the finish button appear.
     */
    private void processRightCheck()
    {
        for (Tower t : rightPlayer.getTowers())
        {
            if (t.getTowerRect().intersects(towerBeingPlaced.getTowerRect()))
            {
                Toast.makeText(
                    this,
                    "You can't place a tower on top of another tower.",
                    Toast.LENGTH_SHORT).show();
                return;
            }
        }
        rightPlayer.updateCoins(-towerBeingPlaced.getCost());
        towerBeingPlaced.getTowerRect().setAlpha(255);
        // this.remove(towerBeingPlaced.rangeDetector);
        rightPlayer.addTower(towerBeingPlaced);
        towerBeingPlaced = null;
        rightCheck.setVisible(false);
        rightCancel.setVisible(false);
        finishTurn.setVisible(true);
    }


    /**
     * If the user is finished, then close the menus, award passive gold, and
     * move the finish button.
     */
    private void processLeftFinished()
    {
        currentTurn = PlayerType.RIGHT;
        finishTurn.setBounds(new RectF(
            (int)(-realLeft + width),
            86,
            (int)(37 - realLeft + width),
            104));
        leftPlayer.getTowerMenu().collapse();
        leftPlayer.getMinionMenu().collapse();
        leftPlayer.updateCoins(passiveGoldGain);
        turnNotifier.setText(rightPlayer.getProfile().getName() + "'s Turn!");
        turnNotifier.show();
    }


    /**
     * If the user doesn't want to place the tower, then remove the tower and
     * make the finish button visible.
     */
    private void processLeftCancel()
    {
        this.remove(towerBeingPlaced.getGraphics());
        towerBeingPlaced = null;
        leftCheck.setVisible(false);
        leftCancel.setVisible(false);
        finishTurn.setVisible(true);
    }


    /**
     * If the user wants to place the tower, then make sure it doesn't intersect
     * other towers, then subtract the coins and make the finish button appear.
     */
    private void processLeftCheck()
    {
        for (Tower t : leftPlayer.getTowers())
        {
            if (t.getTowerRect().intersects(towerBeingPlaced.getTowerRect()))
            {
                Toast.makeText(
                    this,
                    "You can't place a tower on top of another tower.",
                    Toast.LENGTH_SHORT).show();
                return;
            }
        }
        leftPlayer.updateCoins(-towerBeingPlaced.getCost());
        towerBeingPlaced.getTowerRect().setAlpha(255);
        // this.remove(towerBeingPlaced.rangeDetector);
        leftPlayer.addTower(towerBeingPlaced);
        towerBeingPlaced = null;
        leftCheck.setVisible(false);
        leftCancel.setVisible(false);
        finishTurn.setVisible(true);
    }


    /**
     * if the user has clicked on the tower menu, then go through all of the
     * options in the menu to see if one of them was clicked. Match the tower
     * class to the button to decide which tower to spawn.
     */
    private void processRightTowerMenuSelection(float x, float y)
    {
        rightPlayer.getTowerMenu().collapse();
        for (Shape s : rightPlayer.getTowerMenu().getMenuOptions())
        {
            if (s.contains(x, y))
            {
                if (rightPlayer.getAvailableTowers().get(
                    rightPlayer.getTowerMenu().getMenuOptions().indexOf(s)) instanceof BasicTower)
                {
                    towerBeingPlaced =
                        new BasicTower(
                            (int)(81 - realLeft + width),
                            64,
                            (int)(101 - realLeft + width),
                            84,
                            PlayerType.RIGHT,
                            GameScreen.this);
                }
                else if (rightPlayer.getAvailableTowers().get(
                    rightPlayer.getTowerMenu().getMenuOptions().indexOf(s)) instanceof SecondTower)
                {
                    towerBeingPlaced =
                        new SecondTower(
                            (int)(81 - realLeft + width),
                            64,
                            (int)(101 - realLeft + width),
                            84,
                            PlayerType.RIGHT,
                            GameScreen.this);
                }
                if (towerBeingPlaced.getCost() > rightPlayer.getCoins())
                {
                    Toast.makeText(
                        this,
                        "You can't afford that tower. It costs "
                            + towerBeingPlaced.getCost() + " gold.",
                        Toast.LENGTH_SHORT).show();
                    towerBeingPlaced = null;
                }
                else
                {
                    towerBeingPlaced.addObserver(this);
                    this.add(towerBeingPlaced.getGraphics());
                    finishTurn.setVisible(false);
                    rightCheck.setVisible(true);
                    rightCancel.setVisible(true);
                }
                return;
            }
        }
    }


    /**
     * If the user clicks on the minion menu, then check which minion button was
     * clicked in instantiate the appropriate one.
     */
    private void processRightMinionMenuSelection(float x, float y)
    {
        Minion temp = null;
        for (Shape s : rightPlayer.getMinionMenu().getMenuOptions())
        {
            if (s.contains(x, y))
            {
                // index of 0 means it is a BasicMinion
                if (rightPlayer.getAvailableMinions().get(
                    rightPlayer.getMinionMenu().getMenuOptions().indexOf(s)) instanceof BasicMinion)
                {
                    temp =
                        new BasicMinion(
                            (int)(rightPlayer.getBaseTower().getTowerRect()
                                .getBounds().left - 5),
                            79,
                            (int)(rightPlayer.getBaseTower().getTowerRect()
                                .getBounds().left),
                            84,
                            PlayerType.RIGHT,
                            5);
                }
                else if (rightPlayer.getAvailableMinions().get(
                    rightPlayer.getMinionMenu().getMenuOptions().indexOf(s)) instanceof SecondMinion)
                {
                    temp =
                        new SecondMinion(
                            (int)(rightPlayer.getBaseTower().getTowerRect()
                                .getBounds().left - 5),
                            79,
                            (int)(rightPlayer.getBaseTower().getTowerRect()
                                .getBounds().left),
                            84,
                            PlayerType.RIGHT,
                            8);
                }
                if (temp != null && temp.getCost() > rightPlayer.getCoins())
                {
                    Toast.makeText(
                        this,
                        "You can't afford that minion. It costs "
                            + temp.getCost() + " gold.",
                        Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // shift the minion to the front of the minions so that
                    // they don't overlap
                    for (Minion m : rightPlayer.getMinions())
                    {
                        if (temp.getMinionRect().intersects(m.getMinionRect()))
                        {
                            temp.moveBy(temp.getMinionRect().getBounds().left
                                - m.getMinionRect().getBounds().right);
                        }
                    }
                    temp.addObserver(this);
                    rightPlayer.updateCoins(-temp.getCost());
                    rightPlayer.addMinion(temp);
                    this.add(temp.getGraphics());
                }
                return;
            }
        }
    }


    /**
     * If the user clicks on the minion menu, then check which minion button was
     * clicked in instantiate the appropriate one.
     */
    private void processLeftMinionMenuSelection(float x, float y)
    {
        Minion temp = null;
        for (Shape s : leftPlayer.getMinionMenu().getMenuOptions())
        {
            if (s.contains(x, y))
            {
                if (leftPlayer.getAvailableMinions().get(
                    leftPlayer.getMinionMenu().getMenuOptions().indexOf(s)) instanceof BasicMinion)
                {
                    temp =
                        new BasicMinion(
                            (int)(leftPlayer.getBaseTower().getTowerRect()
                                .getBounds().right),
                            79,
                            (int)(leftPlayer.getBaseTower().getTowerRect()
                                .getBounds().right + 5),
                            84,
                            PlayerType.LEFT,
                            5);
                }
                else if (leftPlayer.getAvailableMinions().get(
                    leftPlayer.getMinionMenu().getMenuOptions().indexOf(s)) instanceof SecondMinion)
                {
                    temp =
                        new SecondMinion(
                            (int)(leftPlayer.getBaseTower().getTowerRect()
                                .getBounds().right),
                            79,
                            (int)(leftPlayer.getBaseTower().getTowerRect()
                                .getBounds().right + 5),
                            84,
                            PlayerType.LEFT,
                            8);
                }
                if (temp != null && temp.getCost() > leftPlayer.getCoins())
                {
                    Toast.makeText(
                        this,
                        "You can't afford that minion. It costs "
                            + temp.getCost() + " gold.",
                        Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // shift the minion to the front of the minions so that
                    // they don't overlap
                    for (Minion m : leftPlayer.getMinions())
                    {
                        if (temp.getMinionRect().intersects(m.getMinionRect()))
                        {
                            temp.moveBy(m.getMinionRect().getBounds().right
                                - temp.getMinionRect().getBounds().left);
                        }
                    }
                    temp.addObserver(this);
                    leftPlayer.updateCoins(-temp.getCost());
                    leftPlayer.addMinion(temp);
                    this.add(temp.getGraphics());
                }

                return;
            }
        }
    }


    /**
     * if the user has clicked on the tower menu, then go through all of the
     * options in the menu to see if one of them was clicked. Match the tower
     * class to the button to decide which tower to spawn.
     */
    private void processLeftTowerMenuSelection(float x, float y)
    {
        leftPlayer.getTowerMenu().collapse();
        Log.d("NUM TOWERS", ""
            + leftPlayer.getTowerMenu().getMenuOptions().size());
        for (Shape s : leftPlayer.getTowerMenu().getMenuOptions())
        {
            if (s.contains(x, y))
            {
                // index of 0 means it is a BasicTower
                if (leftPlayer.getAvailableTowers().get(
                    leftPlayer.getTowerMenu().getMenuOptions().indexOf(s)) instanceof BasicTower)
                {
                    towerBeingPlaced =
                        new BasicTower(
                            (int)(91 - realLeft),
                            64,
                            (int)(111 - realLeft),
                            84,
                            PlayerType.LEFT,
                            GameScreen.this);
                }
                else if (leftPlayer.getAvailableTowers().get(
                    leftPlayer.getTowerMenu().getMenuOptions().indexOf(s)) instanceof SecondTower)
                {
                    towerBeingPlaced =
                        new SecondTower(
                            (int)(91 - realLeft),
                            64,
                            (int)(111 - realLeft),
                            84,
                            PlayerType.LEFT,
                            GameScreen.this);
                }
                if (towerBeingPlaced.getCost() > leftPlayer.getCoins())
                {
                    Toast.makeText(
                        this,
                        "You can't afford that tower. It costs "
                            + towerBeingPlaced.getCost() + " gold.",
                        Toast.LENGTH_SHORT).show();
                    towerBeingPlaced = null;
                }
                else
                {
                    towerBeingPlaced.addObserver(this);
                    this.add(towerBeingPlaced.getGraphics());
                    finishTurn.setVisible(false);
                    leftCheck.setVisible(true);
                    leftCancel.setVisible(true);
                }
                return;
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Deletes all the shapes within a ShapeField
     *
     * @param field
     *            the ShapeField to delete
     */
    public void remove(ShapeField field)
    {
        for (Shape s : field)
        {
            this.remove(s);
        }
    }


    // ----------------------------------------------------------
    /**
     * Responds to user finger drag by scrolling the screen and its elements as
     * well as recording where it was touched
     *
     * @param x
     *            where the user clicked
     * @param y
     *            where the user clicked
     */
    public void onTouchMove(float x, float y)
    {
        if (!isAnimating)
        {
            // if the user is placing a tower and has clicked on the tower
            // then move the tower
            if (towerBeingPlaced != null
                && towerBeingPlaced.getTowerRect().contains(x, y))
            {
                RectF curBounds = towerBeingPlaced.getTowerRect().getBounds();
                Log.d("SCROLL", Float.toString(x - lastTouchDown.x));
                float left = curBounds.left + (x - lastTouchDown.x);
                float right = left + (curBounds.right - curBounds.left);
                if (towerBeingPlaced.getPlayer() == PlayerType.LEFT)
                {
                    if (left >= leftPlayer.getBaseTower().getTowerRect()
                        .getBounds().right
                        && right <= -realLeft + width)
                    {
                        for (Shape s : towerBeingPlaced.getGraphics())
                        {
                            s.moveBy(x - lastTouchDown.x, 0);
                        }
                    }
                }
                else
                {
                    if (left >= -realLeft + width
                        && right <= rightPlayer.getBaseTower().getTowerRect()
                            .getBounds().left)
                    {
                        for (Shape s : towerBeingPlaced.getGraphics())
                        {
                            s.moveBy(x - lastTouchDown.x, 0);
                        }
                    }
                }
            }
            // if a tower is being placed, then only let the user scroll
            // above the tower to make placement easier, otherwise you can
            // scroll anywhere
            else if (towerBeingPlaced != null && y < 64
                || towerBeingPlaced == null)
            {
                scrollAll(x, y);
            }
            lastTouchDown.set(x, y);
        }
    }


    // ----------------------------------------------------------
    /**
     * Scrolls the background and elements if the scrolling would not move the
     * background off of the screen. All elements on screen are moved if the
     * background is moved except for the background and the ground.
     *
     * @param x
     *            where the user clicked
     * @param y
     *            where the user clicked
     */
    public void scrollAll(final float x, float y)
    {
        RectF curBounds = background.getBounds();
        float left = curBounds.left + (x - lastTouchDown.x);
        float right = left + 2 * width;
        // only scroll the background if the user would remain on the
        // background, the left edge should always be 0 or negative because the
        // left edge of the background should always be off screen so that
        // no black screen is seen, same for the right
        if (left <= 0 && right >= width)
        {
            realLeft -= (x - lastTouchDown.x);
            background.moveBy(x - lastTouchDown.x, 0);
            Shape[] arr = this.getShapes().all().toArray();
            for (int i = 0; i < arr.length; i++)
            {
                if (!arr[i].equals(background))
                {
                    curBounds = arr[i].getBounds();
                    left = curBounds.left + (x - lastTouchDown.x);
                    right = left + (curBounds.right - curBounds.left);
                    arr[i].moveBy(x - lastTouchDown.x, 0);
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Handles the end game for when one player wins
     *
     * @param victor
     *            the winner of the game
     */
    public void endGame(Player victor)
    {
        victor.addXP(50);
        SharedPreferences savedProfiles =
            getSharedPreferences(prefFile, MODE_PRIVATE);
        Editor editor = savedProfiles.edit();
        editor.remove(rightPlayer.getProfile().getName());
        editor.remove(leftPlayer.getProfile().getName());
        editor.putString(rightPlayer.getProfile().getName(), rightPlayer
            .getProfile().toString());
        editor.putString(leftPlayer.getProfile().getName(), leftPlayer
            .getProfile().toString());
        editor.commit();
        inGame = false;
        Intent i = new Intent(this, MainMenu.class);
        startActivity(i);
        finish();
    }


    // ----------------------------------------------------------
    /**
     * Adds all the shapes within a ShapeField
     *
     * @param field
     *            the ShapeField to add
     */
    public void add(ShapeField field)
    {
        for (Shape s : field)
        {
            this.add(s);
        }
    }


    /**
     * Gets the player of the said type
     *
     * @param type
     *            the type of player
     * @return the player
     */
    public Player getPlayer(PlayerType type)
    {
        return type == PlayerType.LEFT ? leftPlayer : rightPlayer;
    }
}
