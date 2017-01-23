package cs2114.pms.peasantsmedievalsiege;

import sofia.graphics.ShapeField;
import java.util.LinkedList;
import sofia.graphics.Color;
import sofia.graphics.OvalShape;
import sofia.graphics.RectangleShape;

/**
 * // -------------------------------------------------------------------------
 * /** The abstract base for the tower class. Contains the taking damage
 * methods, but not the attacking methods.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public abstract class Tower
    extends sofia.util.Observable
    implements Damageable
{
    /**
     * the health of the tower
     */
    private float              health;
    /**
     * The graphics for the tower class, stored in a RectangleShape
     */
    private RectangleShape     towerRect;
    /**
     * The graphic representation for the hp bar for the tower
     */
    private HPBar              hpbar;
    /**
     * contains hpbar, towerRect, and rangeindicator for easy removal and
     * addition to screen
     */
    private ShapeField         graphics;
    /**
     * the color of the tower
     */
    private int                color;
    /**
     * The damage of the tower, protected so that subclasses (different types of
     * towers) can access it without getting and setting.
     */
    private int                damage;
    /**
     * the cost of the tower
     */
    private int                cost;
    /**
     * the range of the tower
     */
    private int                range;
    /**
     * the ovalshape used to determine if another entity enters the range of the
     * tower
     */
    private OvalShape          rangeDetector;
    /**
     * the level needed to be able to build this tower
     */
    private int                levelNeeded;
    /**
     * the xp given to the enemy player when this tower dies
     */
    private int                xpGiven;
    /**
     * that player to whom this tower belongs
     */
    private PlayerType         player;

    /**
     * Amount of attacks per turn
     */
    private int                attacksPerTurn;

    /**
     * How many times it has attack this turn already
     */
    private int                amountOfAttacks;

    /**
     * A list of which minions it's attacked so that it doesn't attack the same
     * one twice
     */
    private LinkedList<Minion> attackedOnThisTurn = new LinkedList<Minion>();


    // ----------------------------------------------------------
    /**
     * @return the attacksPerTurn
     */
    public int getAttacksPerTurn()
    {
        return attacksPerTurn;
    }


    // ----------------------------------------------------------
    /**
     * @param attacksPerTurn
     *            the attacksPerTurn to set
     */
    public void setAttacksPerTurn(int attacksPerTurn)
    {
        this.attacksPerTurn = attacksPerTurn;
    }


    // ----------------------------------------------------------
    /**
     * Create a new Tower object. Cost and levelNeeded should be set in
     * subclasses. Only use to keep track of which towers are available for
     * building.
     */
    protected Tower()
    {
        // intentionally empty
    }


    /**
     * Constructor for the tower class. Takes into account location, creates
     * object, and assigns variables accordingly. Protected to ensure that
     * abstract class cannot be instantiated.
     *
     * @param left
     *            the left edge of the tower
     * @param top
     *            the top edge of the tower
     * @param right
     *            the right edge of the tower
     * @param bottom
     *            the bottom edge of the tower
     * @param c
     *            the color of the tower
     * @param type
     *            either LEFT or RIGHT according to which player built the tower
     * @param health
     *            the starting and maximum health of the tower
     * @param range
     *            the range of the tower
     * @param cost
     *            the cost of the tower
     * @param levelNeeded
     *            the level needed to build this tower
     * @param damage
     *            the damage of this tower
     * @param xpGiven
     *            the xp given to the enemy player when it dies
     */
    protected Tower(
        int left,
        int top,
        int right,
        int bottom,
        Color c,
        PlayerType type,
        int health,
        int range,
        int cost,
        int levelNeeded,
        int damage,
        int xpGiven)
    {
        towerRect = new RectangleShape(left, top, right, bottom);
        towerRect.setFillColor(c);
        towerRect.setZIndex(-500);
        this.color = c.toRawColor();

        player = type;
        this.health = health;
        this.range = range;
        this.cost = cost;
        this.levelNeeded = levelNeeded;
        this.damage = damage;
        this.xpGiven = xpGiven;

        hpbar = new HPBar(left, top - 8, right, top - 2, health, health);

        rangeDetector = new OvalShape(targetableX(), targetableY(), this.range);
        rangeDetector.setColor(Color.red);
        this.attacksPerTurn = 0;

        graphics = new ShapeField();
        graphics.add(towerRect);
        graphics.addAll(hpbar.getWholeBar());
        graphics.add(rangeDetector);

        // makes it possible for the player to tell apart each other's towers
        RectangleShape playerIdentifier =
            new RectangleShape(right - 4, bottom - 4, right, bottom);
        if (player == PlayerType.LEFT)
        {
            playerIdentifier.setFillColor(Color.blue);
        }
        else
        {
            playerIdentifier.setFillColor(Color.red);
        }
        graphics.add(playerIdentifier);
    }


    // ----------------------------------------------------------
    /**
     * @param m
     *            the minion to attack
     */
    public abstract void attack(Minion m);


    @Override
    public void takeDamage(int amount)
    {
        health -= amount;
        hpbar.remove(amount);
        if (health <= 0.1)
        {
            notifyObservers();
        }
    }


    // ----------------------------------------------------------
    /**
     * @return the amountOfAttacks
     */
    public int getAmountOfAttacks()
    {
        return amountOfAttacks;
    }


    // ----------------------------------------------------------
    /**
     * @param amountOfAttacks
     *            the amountOfAttacks to set
     */
    public void setAmountOfAttacks(int amountOfAttacks)
    {
        this.amountOfAttacks = amountOfAttacks;
    }


    // ----------------------------------------------------------
    /**
     * @return the attackedOnThisTurn
     */
    public LinkedList<Minion> getAttackedOnThisTurn()
    {
        return attackedOnThisTurn;
    }


    // ----------------------------------------------------------
    /**
     * @param attackedOnThisTurn
     *            the attackedOnThisTurn to set
     */
    public void setAttackedOnThisTurn(LinkedList<Minion> attackedOnThisTurn)
    {
        this.attackedOnThisTurn = attackedOnThisTurn;
    }


    @Override
    public float getHealth()
    {
        return health;
    }


    @Override
    public float targetableX()
    {
        return towerRect.getPosition().x;
    }


    @Override
    public float targetableY()
    {
        return towerRect.getPosition().y;
    }


    /**
     * Allows subclasses to have different hpbar locations
     *
     * @param left
     *            the left edge
     * @param top
     *            the top edge
     * @param right
     *            the right edge
     * @param bottom
     *            the bottom edge
     */
    protected void changeLocationOfHPBar(
        float left,
        float top,
        float right,
        float bottom)
    {
        hpbar.moveTo(left, top, right, bottom);
    }


    // ----------------------------------------------------------
    /**
     * Returns the graphics for the Tower.
     *
     * @return the rectangle shape which contains the tower
     */
    public RectangleShape getTowerRect()
    {
        return towerRect;
    }


    // ----------------------------------------------------------
    /**
     * Returns which player this tower belongs to.
     *
     * @return LEFT for left player, RIGHT for right player
     */
    public PlayerType getPlayer()
    {
        return player;
    }


    // ----------------------------------------------------------
    /**
     * @return the cost
     */
    public int getCost()
    {
        return cost;
    }


    // ----------------------------------------------------------
    /**
     * @param cost
     *            the cost to set
     */
    public void setCost(int cost)
    {
        this.cost = cost;
    }


    // ----------------------------------------------------------
    /**
     * @return the levelNeeded
     */
    public int getLevelNeeded()
    {
        return levelNeeded;
    }


    // ----------------------------------------------------------
    /**
     * @param levelNeeded
     *            the levelNeeded to set
     */
    public void setLevelNeeded(int levelNeeded)
    {
        this.levelNeeded = levelNeeded;
    }


    // ----------------------------------------------------------
    /**
     * @return the hpbar
     */
    public HPBar getHpbar()
    {
        return hpbar;
    }


    // ----------------------------------------------------------
    /**
     * @return the damage
     */
    public int getDamage()
    {
        return damage;
    }


    // ----------------------------------------------------------
    /**
     * @return the rangeDetector
     */
    public OvalShape getRangeDetector()
    {
        return rangeDetector;
    }


    // ----------------------------------------------------------
    /**
     * @return the graphics
     */
    public ShapeField getGraphics()
    {
        return graphics;
    }


    // ----------------------------------------------------------
    /**
     * @param color
     *            the color to set
     */
    public void setColor(int color)
    {
        this.color = color;
    }


    // ----------------------------------------------------------
    /**
     * @return the xpGiven
     */
    public int getXpGiven()
    {
        return xpGiven;
    }


    // ----------------------------------------------------------
    /**
     * @return the color
     */
    public int getColor()
    {
        return color;
    }


    /**
     * Allows the tower to restart all of it's attacks (sets current attacks to
     * 0)
     */
    public void resetAttacks()
    {
        amountOfAttacks = 0;
        attackedOnThisTurn.clear();
    }
}
