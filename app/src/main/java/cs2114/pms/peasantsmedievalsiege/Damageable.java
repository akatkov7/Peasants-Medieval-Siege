package cs2114.pms.peasantsmedievalsiege;

/**
 * // -------------------------------------------------------------------------
 * /** Interface for any class that can take damage during the game. The methods
 * included are for targeting purposes, and then damage taking purposes.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public interface Damageable
{
    /**
     * this should take damage in the amount of the parameter, and then update
     * for whether or not it is destroyed. Pre-condition: amount >= 0
     *
     * @param amount
     *            the amount of damage taken
     */
    public abstract void takeDamage(int amount);


    /**
     * Returns the location of where a projectile should be aimed towards. This
     * can be different than the object's actual x value, so a targetedX value
     * is used instead (usually the middle of an object)
     *
     * @return the x-coordinate that should be aimed at
     */
    public abstract float targetableX();


    /**
     * Returns the location of where a projectile should be aimed towards. This
     * can be different than the object's actual y value, so a targetedY value
     * is used instead (usually the middle of an object)
     *
     * @return the y-coordinate that should be aimed at
     */
    public abstract float targetableY();


    /**
     * Should return the amount of health remaining.
     *
     * @return the amount of health remaining
     */
    public abstract float getHealth();
}
