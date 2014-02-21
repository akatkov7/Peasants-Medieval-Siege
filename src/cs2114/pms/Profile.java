package cs2114.pms;

// -------------------------------------------------------------------------
/**
 * A simple class which is used to store profiles for the users. It contains the
 * user's name, what level the user is, and the current experience on the
 * current level.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class Profile
{
    private String name;
    private int    level;
    private int    xp;


    // ----------------------------------------------------------
    /**
     * Create a new Profile object.
     *
     * @param s
     *            a string representation of the profile such as "Name Level XP"
     *            i.e. "George 2 17"
     */
    public Profile(String s)
    {
        name = s.substring(0, s.indexOf(" "));
        level =
            Integer
                .parseInt(s.substring(s.indexOf(" ") + 1, s.lastIndexOf(" ")));
        xp = Integer.parseInt(s.substring(s.lastIndexOf(" ") + 1));
    }


    // ----------------------------------------------------------
    /**
     * @return the name associated with this profile
     */
    public String getName()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * @return the level of this profile
     */
    public int getLevel()
    {
        return level;
    }


    // ----------------------------------------------------------
    /**
     * Set the level of this profile to i
     *
     * @param i
     *            the new level of the profile
     */
    public void setLevel(int i)
    {
        level = i;
    }


    // ----------------------------------------------------------
    /**
     * @return the xp of this profile
     */
    public int getXP()
    {
        return xp;
    }


    // ----------------------------------------------------------
    /**
     * Set the xp of this profile to i
     *
     * @param i
     *            the new xp
     */
    public void setXP(int i)
    {
        xp = i;
    }


    @Override
    public String toString()
    {
        return name + " " + level + " " + xp;
    }
}
