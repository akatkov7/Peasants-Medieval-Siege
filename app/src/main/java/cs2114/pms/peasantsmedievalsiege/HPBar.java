package cs2114.pms.peasantsmedievalsiege;

import sofia.graphics.Color;

// -------------------------------------------------------------------------
/**
 * The HPBar extends Bar and displays the HP remaining for a tower or minion.
 *
 * @author akatkov
 * @author mluke94
 * @author treiter
 * @version 2013.11.15
 */
public class HPBar
    extends Bar
{
    // ----------------------------------------------------------
    /**
     * Create a new HPBar object.
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
    public HPBar(
        float left,
        float top,
        float right,
        float bottom,
        int startVal,
        int maxVal)
    {
        super(left, top, right, bottom, startVal, maxVal, Color.red);
    }
}
