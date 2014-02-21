package cs2114.pms;

import sofia.graphics.Color;
import sofia.graphics.OvalShape;

// -------------------------------------------------------------------------
/**
 * The Projectile class represents a ball that is shot by the BasicTower.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class Projectile
    extends OvalShape
{
    // ----------------------------------------------------------
    /**
     * Create a new Projectile object.
     *
     * @param x
     *            the x start point
     * @param y
     *            the y start point
     * @param size
     *            the size of the ball
     */
    public Projectile(float x, float y, int size)
    {
        super(x, y, size);
        setFillColor(Color.red);
    }

}
