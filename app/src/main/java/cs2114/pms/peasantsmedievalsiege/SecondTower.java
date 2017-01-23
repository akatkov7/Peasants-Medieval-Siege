package cs2114.pms.peasantsmedievalsiege;

import sofia.graphics.Color;
import sofia.graphics.LineShape;

// -------------------------------------------------------------------------
/**
 * The second most basic tower unit.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.12.05
 */
public class SecondTower
    extends Tower
{

    private GameScreen screen;


    // ----------------------------------------------------------
    /**
     * Create a new SecondTower object. Set the cost, levelNeeded, attacks per
     * turn and the color for this tower, and call super() constructor.
     */
    public SecondTower()
    {
        super();
        setCost(175);
        setLevelNeeded(2);
        setAttacksPerTurn(2);
        setColor(Color.darkGreen.toRawColor());
    }


    // ----------------------------------------------------------
    /**
     * Create a new SecondTower object.
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
    public SecondTower(
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
            Color.darkGreen,
            type,
            8,
            40,
            175,
            2,
            6,
            20);
        getTowerRect().setAlpha(50);
        this.screen = screen;
        setAttacksPerTurn(2);
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
        LineShape line =
            new LineShape(
                targetableX(),
                targetableY(),
                m.targetableX(),
                m.targetableY());
        line.setColor(Color.red);
        screen.add(line);
        line.animate(1000).strokeWidth(2.0)
            .removeWhenComplete().play();
        try
        {
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        m.takeDamage(this.getDamage());
        // attacked one more time
        this.setAmountOfAttacks(this.getAmountOfAttacks() + 1);
        this.getAttackedOnThisTurn().add(m);
    }
}
