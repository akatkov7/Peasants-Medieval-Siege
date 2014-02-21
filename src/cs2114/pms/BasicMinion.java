package cs2114.pms;

import sofia.graphics.Color;

// -------------------------------------------------------------------------
/**
 * The most basic minion.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class BasicMinion
    extends Minion
{

    // ----------------------------------------------------------
    /**
     * Create a new BasicMinion object. Set the cost, levelNeeded, attacks per
     * turn and the color for this minion, and call super() constructor.
     */
    public BasicMinion()
    {
        super();
        setCost(50);
        setLevelNeeded(1);
        setColor(Color.orange.toRawColor());
    }


    // ----------------------------------------------------------
    /**
     * Create a new BasicMinion object.
     *
     * @param left
     *            the left edge
     * @param top
     *            the top edge
     * @param right
     *            the right
     * @param bottom
     *            the bottom
     * @param leftOrRight
     *            LEFT for left player and RIGHT for right player
     * @param health
     *            the health of the minion
     */
    public BasicMinion(
        int left,
        int top,
        int right,
        int bottom,
        PlayerType leftOrRight,
        int health)
    {
        super(
            left,
            top,
            right,
            bottom,
            Color.orange,
            leftOrRight,
            health,
            50,
            1,
            45,
            1,
            5);
    }

}
