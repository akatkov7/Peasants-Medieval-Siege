package cs2114.pms.peasantsmedievalsiege;

import sofia.graphics.Color;

// -------------------------------------------------------------------------
/**
 * The most basic tower unit.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class BasicTower
    extends Tower
{

    private GameScreen screen;


    // ----------------------------------------------------------
    /**
     * Create a new BasicTower object. Set the cost, levelNeeded, attacks per
     * turn and the color for this tower, and call super() constructor.
     */
    public BasicTower()
    {
        super();
        setCost(100);
        setLevelNeeded(1);
        setAttacksPerTurn(1);
        setColor(Color.purple.toRawColor());
    }


    // ----------------------------------------------------------
    /**
     * Create a new BasicTower object.
     *
     * @param left
     *            the left edge of the tower
     * @param top
     *            the top edge of the tower
     * @param right
     *            the right edge of the tower
     * @param bottom
     *            the bottom edge of the tower
     * @param type
     *            either LEFT or RIGHT according to which player built the tower
     * @param screen
     *            the game screen so that it can add projectiles to the screen
     */
    public BasicTower(
        int left,
        int top,
        int right,
        int bottom,
        PlayerType type,
        GameScreen screen)
    {
        super(
            left,
            top,
            right,
            bottom,
            Color.purple,
            type,
            5,
            40,
            100,
            1,
            4,
            10);
        getTowerRect().setAlpha(50);
        this.screen = screen;
        setAttacksPerTurn(1);
    }


    @Override
    public void attack(Minion m)
    {
        // Only attack if it still has "attacks" left and it hasn't attacked
// this minion yet
        if (getAmountOfAttacks() >= getAttacksPerTurn()
            || this.getAttackedOnThisTurn().contains(m))
        {
            return;
        }
        // shoot projectile from center of tower to center of minion
        Projectile p = new Projectile(targetableX(), targetableY(), 2);
        screen.add(p);
        p.animate(1000).position(m.targetableX(), m.targetableY())
            .removeWhenComplete().play();
        // wait while the projectile finishes animating
        try
        {
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // damage the minion
        m.takeDamage(this.getDamage());
        // attacked one more time
        this.setAmountOfAttacks(this.getAmountOfAttacks() + 1);
        this.getAttackedOnThisTurn().add(m);
    }
}
