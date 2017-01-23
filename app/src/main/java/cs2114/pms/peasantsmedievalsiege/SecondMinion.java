package cs2114.pms.peasantsmedievalsiege;

import sofia.graphics.Color;

// -------------------------------------------------------------------------
/**
 * The second most basic minion class.
 *
 * @author Andriy
 * @version Dec 7, 2013
 */
public class SecondMinion
    extends Minion
{

    // ----------------------------------------------------------
    /**
     * Create a new SecondMinion object. Set the cost, levelNeeded, attacks per
     * turn and the color for this minion, and call super() constructor.
     */
    public SecondMinion()
    {
        super();
        setCost(100);
        setLevelNeeded(4);
        setColor(Color.tan.toRawColor());
    }


    // ----------------------------------------------------------
    /**
     * Create a new SecondMinion object.
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
    public SecondMinion(
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
            Color.tan,
            leftOrRight,
            health,
            100,
            4,
            40,
            3,
            10);
    }

}
