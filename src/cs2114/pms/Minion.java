package cs2114.pms;

import android.util.Log;
import java.util.List;
import sofia.graphics.Color;
import sofia.graphics.RectangleShape;
import sofia.graphics.Shape;
import sofia.graphics.ShapeField;

/**
 * // -------------------------------------------------------------------------
 * /** The minion class that all actual minions will extend.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public abstract class Minion
    extends sofia.util.Observable
    implements Damageable
{
    /**
     * health of this minion
     */
    private int            health;
    /**
     * the color of the minion
     */
    private int            color;
    /**
     * the graphical representation of the hp bar
     */
    private HPBar          hpbar;
    /**
     * the graphical representation of the minion
     */
    private RectangleShape minionRect;
    /**
     * contains hpbar and minionRect for easy removal and addition to screen
     */
    private ShapeField     graphics;
    /**
     * the cost of the minion
     */
    private int            cost;
    /**
     * the level needed to be able to build this minion
     */
    private int            levelNeeded;
    /**
     * the move distance of the minion
     */
    private int            moveDistance;
    /**
     * the damage this minion deals
     */
    private int            damage;
    /**
     * the player to whom this minion belongs to
     */
    private PlayerType     player;
    /**
     * the xp given to the enemy player when killed
     */
    private int            xpGiven;
    private boolean        canMove = true;

    // should always start as alive
    private boolean        isDead  = false;


    // ----------------------------------------------------------
    /**
     * Create a new Minion object. Cost and levelNeeded should be set in
     * subclasses. Only use to keep track of which minions are available for
     * building.
     */
    protected Minion()
    {
        // intentionally empty
    }


    /**
     * Protected constructor so that abstract class cannot be instantiated
     *
     * @param left
     *            the left edge
     * @param top
     *            the top edge
     * @param right
     *            the right
     * @param bottom
     *            the bottom
     * @param c
     *            the color of the minion
     * @param leftOrRight
     *            LEFT for left player and RIGHT for right player
     * @param health
     *            the health of the minion
     * @param cost
     *            the cost of the minion
     * @param levelNeeded
     *            the level needed to build this minion
     * @param moveDistance
     *            the distance this minion can move
     * @param damage
     *            the damage this minion does
     * @param xpGiven
     *            the xp this minion gives on death
     */
    protected Minion(
        int left,
        int top,
        int right,
        int bottom,
        Color c,
        PlayerType leftOrRight,
        int health,
        int cost,
        int levelNeeded,
        int moveDistance,
        int damage,
        int xpGiven)
    {
        minionRect = new RectangleShape(left, top, right, bottom);
        minionRect.setZIndex(1000);
        minionRect.setFillColor(c);

        player = leftOrRight;

        this.health = health;
        this.cost = cost;
        this.levelNeeded = levelNeeded;
        this.moveDistance = moveDistance;
        this.damage = damage;
        this.xpGiven = xpGiven;
        this.color = c.toRawColor();
        hpbar = new HPBar(left, top - 4, right, top - 1, health, health);

        graphics = new ShapeField();
        graphics.add(minionRect);
        graphics.addAll(hpbar.getWholeBar());

        // makes it possible for the player to tell apart each other's minions
        RectangleShape playerIdentifier =
            new RectangleShape(right - 2, bottom - 2, right, bottom);
        playerIdentifier.setZIndex(1000);
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
     * @return the hpbar
     */
    public HPBar getHpbar()
    {
        return hpbar;
    }


    // ----------------------------------------------------------
    /**
     * @return the minionRect
     */
    public RectangleShape getMinionRect()
    {
        return minionRect;
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
     * @return the color
     */
    public int getColor()
    {
        return color;
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
     * @return the player
     */
    public PlayerType getPlayer()
    {
        return player;
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
     * @return the levelNeeded
     */
    public int getLevelNeeded()
    {
        return levelNeeded;
    }


    @Override
    public void takeDamage(int amount)
    {
        health -= amount;
        hpbar.remove(amount);
        Log.d("health", health + "");
        if (health <= 0.1)
        {
            Log.d("DEATH", "OCCURING");
            notifyObservers();
            isDead = true;
        }

    }


    // ----------------------------------------------------------
    /**
     * Move the minion and the hp bar by amount
     *
     * @param amount
     *            the amount to move
     */
    public void moveBy(float amount)
    {
        for (Shape s : graphics)
        {
            s.moveBy(amount, 0);
        }
    }


    // ----------------------------------------------------------
    /**
     * Moves the minion as much as possible according to fights and distance
     * allowed to move
     *
     * @param enemyMinions
     *            the enemy minions
     * @param enemyTowers
     *            the enemy towers
     * @param friendlyMinions
     *            the friendly minions on your team
     */
    public void move(
        List<Minion> enemyMinions,
        List<Tower> enemyTowers,
        List<Minion> friendlyMinions)
    {
        this.canMove = true;
        int i = 0;
        while (i < moveDistance && canMove)
        {
            // if at any point the minion dies during its move, stop moving
            if (isDead)
            {
                return;
            }
            if (player == PlayerType.LEFT)
            {
                this.moveBy(1);
                // after every step, check if there have been collisions
                // enemy minion: attack each other and back up
                // enemy tower: attack the tower and back up
                // friendly minion: stop moving
                // if this minion is in range of a tower during its move,
                // it is attacked
                for (Minion m : enemyMinions)
                {
                    if (this.minionRect.intersects(m.minionRect) && !m.isDead())
                    {
                        Log.d("MinionCollision", "Enemies colliding");
                        m.takeDamage(this.damage);
                        this.takeDamage(m.damage);
                        // if the minion killed this, stop
                        if (this.isDead)
                        {
                            return;
                        }
                        this.canMove = false;
                        this.moveBy(m.getMinionRect().getBounds().left
                            - this.minionRect.getBounds().right);
                    }
                }
                for (Tower t : enemyTowers)
                {
                    if (this.minionRect.intersects(t.getTowerRect()))
                    {
                        Log.d("TowerCollision", "Enemies colliding");
                        t.takeDamage(this.damage);
                        this.canMove = false;
                        this.moveBy(t.getTowerRect().getBounds().left
                            - this.minionRect.getBounds().right);
                    }
                    if (t.getRangeDetector().contains(
                        this.targetableX(),
                        this.targetableY()))
                    {
                        Log.d("TowerAttack", "Tower attacking");
                        t.attack(this);
                        // if the tower killed this, stop moving right away
                        if (isDead)
                        {
                            return;
                        }
                    }
                }
                for (Minion m : friendlyMinions)
                {
                    if (this.minionRect.intersects(m.minionRect)
                        && !this.equals(m) && !m.isDead())
                    {
                        Log.d("MinionCollision", "Friendlys colliding");
                        this.canMove = false;
                        this.moveBy(m.getMinionRect().getBounds().left
                            - this.minionRect.getBounds().right);
                    }
                }
            }
            else
            {
                // after every step, check if there have been collisions
                // enemy minion: attack each other and back up
                // enemy tower: attack the tower and back up
                // friendly minion: stop moving
                // if this minion is in range of a tower during its move,
                // it is attacked
                this.moveBy(-1);
                for (Minion m : enemyMinions)
                {
                    if (this.minionRect.intersects(m.minionRect) && !m.isDead())
                    {
                        Log.d("MinionCollision", "Enemies colliding");
                        m.takeDamage(this.damage);
                        this.takeDamage(m.damage);
                        // if the minion killed this, stop
                        if (this.isDead)
                        {
                            return;
                        }
                        this.canMove = false;
                        this.moveBy(m.getMinionRect().getBounds().right
                            - this.minionRect.getBounds().left);
                    }
                }
                for (Tower t : enemyTowers)
                {
                    if (this.minionRect.intersects(t.getTowerRect()))
                    {
                        Log.d("TowerCollision", "Enemies colliding");
                        t.takeDamage(this.damage);
                        this.canMove = false;
                        this.moveBy(t.getTowerRect().getBounds().right
                            - this.minionRect.getBounds().left);
                    }
                    if (t.getRangeDetector().contains(
                        this.targetableX(),
                        this.targetableY()))
                    {
                        Log.d("TowerAttack", "Tower attacking");
                        t.attack(this);
                        // if the tower killed this, stop moving immediately
                        if (this.isDead)
                        {
                            return;
                        }
                    }
                }
                for (Minion m : friendlyMinions)
                {
                    if (this.minionRect.intersects(m.minionRect)
                        && !this.equals(m) && !m.isDead())
                    {
                        Log.d("MinionCollision", "Friendlys colliding");
                        this.canMove = false;
                        this.moveBy(m.getMinionRect().getBounds().right
                            - this.minionRect.getBounds().left);
                    }
                }
            }
            i++;
        }
    }


    @Override
    public float targetableX()
    {
        return minionRect.getPosition().x;
    }


    @Override
    public float targetableY()
    {
        return minionRect.getPosition().y;
    }


    @Override
    public float getHealth()
    {
        return health;
    }


    /**
     * Returns whether or not the minion is dead
     *
     * @return true for dead, false for alive
     */
    public boolean isDead()
    {
        return isDead;
    }

}
