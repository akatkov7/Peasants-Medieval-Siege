package cs2114.pms.peasantsmedievalsiege;

import sofia.graphics.Color;

// -------------------------------------------------------------------------
/**
 * The BaseTower class represents the player's base tower. It cannot attack
 * and when it dies, the game will end.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class BaseTower
    extends Tower
{
    // ----------------------------------------------------------
    /**
     * Create a new BaseTower object.
     *
     * @param left
     *            the left edge of the tower
     * @param right
     *            the right edge of the tower
     * @param bottom
     *            the bottom edge of the tower
     * @param top
     *            the top edge of the tower
     * @param type
     *            the type of player it is (LEFT or RIGHT)
     * @param healthLeft
     *            the left coordinate of the hpbar
     * @param healthTop
     *            the top coordinate of the hpbar
     * @param healthRight
     *            the right coordinate of the hpbar
     * @param healthBottom
     *            the bottom coordinate of the hpbar
     */
    public BaseTower(
        int left,
        int top,
        int right,
        int bottom,
        PlayerType type,
        int healthLeft,
        int healthTop,
        int healthRight,
        int healthBottom)
    {
        super(left, top, right, bottom, Color.black, type, 10, 0, 0, 0, 0, 20);
        // most towers have their HP above them, the base tower has it below
        changeLocationOfHPBar(healthLeft, healthTop, healthRight, healthBottom);
    }


    @Override
    public void attack(Minion m)
    {
        // do nothing since base tower cannot attack
    }
}
