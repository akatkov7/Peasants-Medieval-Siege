package cs2114.pms.peasantsmedievalsiege;

import sofia.graphics.Color;

// -------------------------------------------------------------------------
/**
 * The EXPBar extends Bar and displays the experience of the user.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class EXPBar
    extends Bar
{
    // ----------------------------------------------------------
    /**
     * Create a new EXPBar object.
     *
     * @param left
     *            the left edge of the bar
     * @param bottom
     *            the bottom edge of the bar
     * @param right
     *            the right edge of the bar
     * @param top
     *            the top edge of the bar
     * @param startVal
     *            the number of actions a user has
     * @param maxVal
     *            the maximum number of actions a user has
     */
    public EXPBar(
        float left,
        float top,
        float right,
        float bottom,
        int startVal,
        int maxVal)
    {
        super(left, top, right, bottom, startVal, maxVal, Color.blue);
    }
}
