package cs2114.pms;

import android.graphics.RectF;
import sofia.graphics.OvalShape;
import sofia.graphics.Color;
import sofia.graphics.TextShape;
// import java.util.LinkedList;
import sofia.graphics.RectangleShape;

/**
 * // -------------------------------------------------------------------------
 * /** The right player for the game.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class RightPlayer
    extends Player
{
    /**
     * Puts all the objects in the proper spot for the right player, as well as
     * instantiates variables in the Player class.
     *
     * @param screen
     *            the GameScreen the player belongs to
     * @param w
     *            the width of the entire screen
     * @param p
     *            the profile of the player
     */
    public RightPlayer(GameScreen screen, int w, Profile p)
    {
        super(
            screen,
            new BaseTower(
                2 * w - 12 - 15 * 3,
                9,
                2 * w - 12,
                84,
                PlayerType.RIGHT,
                2 * w - 57,
                86,
                2 * w - 12,
                90),
            new EXPBar(
                2 * w - 57,
                92,
                2 * w - 12,
                96,
                p.getXP(),
                10 * p.getLevel()),
            new TextShape("Level  " + p.getLevel(), 2 * w - 56, 91),
            new RectangleShape(2 * w - 105, 86, 2 * w - 60, 104),
            new RectangleShape(2 * w - 150, 86, 2 * w - 110, 104),
            new OvalShape(new RectF(2 * w - 20, 98, 2 * w - 12, 106)),
            new TextShape("" + 200, 2 * w - 57, 97),
            PlayerType.RIGHT,
            p);
        // displays the player's name about the right base tower
        TextShape nameTag = new TextShape(p.getName(), 2 * w - 12 - 15 * 3, 0);
        nameTag.setColor(Color.black);
        nameTag.setTypeSize(1.0f);
        screen.add(nameTag);
    }
}
