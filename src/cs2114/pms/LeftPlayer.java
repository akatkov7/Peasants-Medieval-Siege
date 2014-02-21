package cs2114.pms;

import sofia.graphics.Color;
import sofia.graphics.OvalShape;
import sofia.graphics.RectangleShape;
import sofia.graphics.TextShape;
import android.graphics.RectF;

/**
 * // -------------------------------------------------------------------------
 * /** The left player in the game.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class LeftPlayer
    extends Player
{
    /**
     * Creates objects in the appropriate place for the left player.
     *
     * @param screen
     *            the screen the player exists on
     * @param p
     *            the profile of the player
     */
    public LeftPlayer(GameScreen screen, Profile p)
    {
        super(
            screen,
            new BaseTower(12, 9, 57, 84, PlayerType.LEFT, 12, 86, 57, 90),
            new EXPBar(12, 92, 57, 96, p.getXP(), 10 * p.getLevel()),
            new TextShape("Level  " + p.getLevel(), 13, 91),
            new RectangleShape(60, 86, 105, 104),
            new RectangleShape(110, 86, 150, 104),
            new OvalShape(new RectF(49, 98, 57, 106)),
            new TextShape("" + 200, 12, 97),
            PlayerType.LEFT,
            p);
        // displays the player's name about the left base tower
        TextShape nameTag = new TextShape(p.getName(), 12, 0);
        nameTag.setColor(Color.black);
        nameTag.setTypeSize(1.0f);
        screen.add(nameTag);
    }
}
