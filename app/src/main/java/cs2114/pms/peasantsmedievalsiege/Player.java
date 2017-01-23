package cs2114.pms.peasantsmedievalsiege;

import java.util.LinkedList;
import java.util.List;
import sofia.graphics.Color;
import sofia.graphics.OvalShape;
import sofia.graphics.RectangleShape;
import sofia.graphics.TextShape;


/**
 * // -------------------------------------------------------------------------
 * /** Abstract class for the player object. Sets everything that doesn't depend
 * on location, since those are a function of whether the player is left or
 * right.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public abstract class Player
{

    /**
     * The BaseTower for the player.
     */
    private Tower               baseTower;
    /**
     * The player's experience
     */
    private EXPBar              playerEXP;
    /**
     * the text showing the player's level
     */
    private TextShape           playerLevel;
    /**
     * Allows the player to add towers on their turn.
     */
    private RectangleShape      addTowerButton;
    /**
     * Allows the player to spawn minions on their turn.
     */
    private RectangleShape      addMinionButton;
    /**
     * The list of all the towers the player has created.
     */
    private LinkedList<Tower>   towers;
    /**
     * The list of all the minions the player has created.
     */
    private LinkedList<Minion>  minions;
    /**
     * LEFT if this is the left player, or RIGHT if this is the right player
     */
    private PlayerType          type;
    /**
     * the number of coins a player has
     */
    private int                 coins;
    /**
     * the graphic of the coin amount
     */
    private OvalShape           coinSymbol;
    /**
     * the graphic representation of the number of coins
     */
    private TextShape           coinAmount;
    /**
     * the profile which stores the user's level and xp accumulated
     */
    private Profile             profile;
    /**
     * a menu which is a list of all the towers this profile can purchase
     */
    private PopupMenu           towerMenu;
    /**
     * a menu which is a list of all the minions this profile can purchase
     */
    private PopupMenu           minionMenu;
    /**
     * list of the available towers the profile can build according to level
     */
    private List<Tower>         availableTowers;
    /**
     * list of the available minions the profile can build according to level
     */
    private List<Minion>        availableMinions;
    /**
     * all towers that are in the game
     */
    private final Tower[]       allTowers  = { new BasicTower(),
        new SecondTower()                 };
    /**
     * all minions that are in the game
     */
    private final Minion[]      allMinions = { new BasicMinion(),
        new SecondMinion()                };
    /**
     * colors for the towers which are in the menu
     */
    private LinkedList<Integer> towerColors;
    /**
     * colors for the minions which are in the menu
     */
    private LinkedList<Integer> minionColors;


    /**
     * Sets the screen, width (really only used for right player), and whether
     * the player is left or right.
     *
     * @param screen
     *            the GameScreen that the player exists on
     * @param bt
     *            the base tower for the player
     * @param eb
     *            the experience bar for the player
     * @param pl
     *            the textshape representing the player's level
     * @param atb
     *            the add tower button for this player
     * @param amb
     *            the add minion button for this player
     * @param cs
     *            the coin symbol for this player
     * @param ca
     *            the textshape for the coins for this player
     * @param type
     *            LEFT for left player and RIGHT for right player
     * @param profile
     *            the profile which contains info about the player
     */
    protected Player(
        GameScreen screen,
        BaseTower bt,
        EXPBar eb,
        TextShape pl,
        RectangleShape atb,
        RectangleShape amb,
        OvalShape cs,
        TextShape ca,
        PlayerType type,
        Profile profile)
    {
        // takes all of the Shapes that are specific to each players based on
        // location and stores them in the local variables
        baseTower = bt;
        screen.add(baseTower.getTowerRect());
        screen.add(baseTower.getHpbar());
        baseTower.addObserver(screen);

        playerEXP = eb;
        screen.add(playerEXP);

        playerLevel = pl;
        playerLevel.setColor(Color.white);
        playerLevel.setTypeSize(.75f);
        playerLevel.setZIndex(1000);
        screen.add(playerLevel);

        addTowerButton = atb;
        addTowerButton.setImage("addtower");
        screen.add(addTowerButton);

        addMinionButton = amb;
        addMinionButton.setImage("addminion");
        screen.add(addMinionButton);

        coinSymbol = cs;
        coinSymbol.setFillColor(Color.yellow);
        screen.add(coinSymbol);

        coinAmount = ca;
        coinAmount.setColor(Color.black);
        coinAmount.setTypeSize(1.5f);
        screen.add(coinAmount);

        towers = new LinkedList<Tower>();
        addTower(baseTower);
        minions = new LinkedList<Minion>();
        availableTowers = new LinkedList<Tower>();
        availableMinions = new LinkedList<Minion>();
        this.type = type;
        coins = 200;
        this.profile = profile;

        // add all the towers that the player has enough experience for
        for (Tower t : allTowers)
        {
            if (t.getLevelNeeded() <= profile.getLevel())
            {
                availableTowers.add(t);
            }
        }
        // add all the minions that the player has enough experience for
        for (Minion m : allMinions)
        {
            if (m.getLevelNeeded() <= profile.getLevel())
            {
                availableMinions.add(m);
            }
        }
        // add the colors of the different towers and minions for display
        // in the popup menus
        towerColors = new LinkedList<Integer>();
        for (Tower t : availableTowers)
        {
            towerColors.add(t.getColor());
        }
        minionColors = new LinkedList<Integer>();
        for (Minion m : availableMinions)
        {
            minionColors.add(m.getColor());
        }

        towerMenu = new PopupMenu(addTowerButton, towerColors, screen);
        minionMenu = new PopupMenu(addMinionButton, minionColors, screen);
    }


    // ----------------------------------------------------------
    /**
     * @return the towers
     */
    public LinkedList<Tower> getTowers()
    {
        return towers;
    }


    // ----------------------------------------------------------
    /**
     * @return the minions
     */
    public LinkedList<Minion> getMinions()
    {
        return minions;
    }


    // ----------------------------------------------------------
    /**
     * @return the type
     */
    public PlayerType getType()
    {
        return type;
    }


    // ----------------------------------------------------------
    /**
     * @return the profile
     */
    public Profile getProfile()
    {
        return profile;
    }


    // ----------------------------------------------------------
    /**
     * @return the towerMenu
     */
    public PopupMenu getTowerMenu()
    {
        return towerMenu;
    }


    // ----------------------------------------------------------
    /**
     * @return the minionMenu
     */
    public PopupMenu getMinionMenu()
    {
        return minionMenu;
    }


    // ----------------------------------------------------------
    /**
     * @return the availableTowers
     */
    public List<Tower> getAvailableTowers()
    {
        return availableTowers;
    }


    // ----------------------------------------------------------
    /**
     * @return the availableMinions
     */
    public List<Minion> getAvailableMinions()
    {
        return availableMinions;
    }


    // ----------------------------------------------------------
    /**
     * Adds a tower to the list of towers for the player.
     *
     * @param t
     *            the tower to be added
     */
    public void addTower(Tower t)
    {
        towers.add(t);
    }


    // ----------------------------------------------------------
    /**
     * Adds a minion to the list of minions for the player.
     *
     * @param m
     *            the minion to be added
     */
    public void addMinion(Minion m)
    {
        minions.add(m);
    }


    // ----------------------------------------------------------
    /**
     * Updates the players coins and the textshape representing it
     *
     * @param change
     *            negative for subtraction, positive for addition
     */
    public void updateCoins(int change)
    {
        this.coins += change;
        this.coinAmount.setText("" + this.coins);
    }


    /**
     * @return this player's coins
     */
    public int getCoins()
    {
        return this.coins;
    }


    // ----------------------------------------------------------
    /**
     * @return the addTowerButton
     */
    public RectangleShape getAddTowerButton()
    {
        return addTowerButton;
    }


    // ----------------------------------------------------------
    /**
     * @return the addMinionButton
     */
    public RectangleShape getAddMinionButton()
    {
        return addMinionButton;
    }


    // ----------------------------------------------------------
    /**
     * @return the baseTower
     */
    public Tower getBaseTower()
    {
        return baseTower;
    }


    // ----------------------------------------------------------
    /**
     * Adds the xp to the user's profile and updates changes in the player's xp
     * bar
     *
     * @param xpToAdd
     *            the xp to add
     */
    public void addXP(int xpToAdd)
    {
        // add the xp to the bar
        int initialXp = this.playerEXP.getCurrentValue();
        this.playerEXP.add(xpToAdd);
        int curLevel = this.profile.getLevel();
        int xpLeft = xpToAdd;
        // if the bar is full, then increase the level and find out how much
        // experience is left
        if (this.playerEXP.isFull())
        {
            curLevel++;
            this.playerEXP.empty();
            xpLeft = xpLeft - (this.playerEXP.getMaxValue() - initialXp);
            this.playerEXP.setMaxValue(curLevel * 10);
            this.playerEXP.add(xpLeft);
        }
        // if the user has enough experience to advance more than one level
        // then keep adding levels until all of it is used up
        while (this.playerEXP.isFull())
        {
            curLevel++;
            this.playerEXP.empty();
            xpLeft = xpLeft - this.playerEXP.getMaxValue();
            this.playerEXP.setMaxValue(curLevel * 10);
            this.playerEXP.add(xpLeft);
        }

        // add new towers and minions based on level
        if (curLevel > this.profile.getLevel())
        {
            for (Tower t : allTowers)
            {
                if (t.getLevelNeeded() <= curLevel
                    && !availableTowers.contains(t))
                {
                    availableTowers.add(t);
                    towerMenu.addMenuItem(t.getColor());
                }
            }

            for (Minion m : allMinions)
            {
                if (m.getLevelNeeded() <= curLevel
                    && !availableMinions.contains(m))
                {
                    availableMinions.add(m);
                    minionMenu.addMenuItem(m.getColor());
                }
            }
        }
        // set the final level and current experience and save it in the profile
        this.profile.setLevel(curLevel);
        this.playerLevel.setText("Level:  " + curLevel);
        this.profile.setXP(this.playerEXP.getCurrentValue());
    }

}
