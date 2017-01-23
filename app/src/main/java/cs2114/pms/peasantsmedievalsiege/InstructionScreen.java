package cs2114.pms.peasantsmedievalsiege;

import android.widget.Button;
import android.widget.TextView;
import sofia.app.Screen;

// -------------------------------------------------------------------------
/**
 * The InstructionScreen class is displayed upon user request. It provides the
 * user with instructions to play the game.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */

public class InstructionScreen
    extends Screen
{
    private int      pageCounter = 1;
    private TextView line1;
    private TextView line2;
    private Button   next;


    /**
     * Sets the instructions for the first page of instructions
     */
    public void initialize()
    {
        line1.setText("Welcome to Peasants Medieval Siege!");
        line2.setText("Two vengeful rulers have turned "
            + "their peasants into colored shapes of doom and prepare to do "
            + "war against one another!");
    }


    // ----------------------------------------------------------
    /**
     * Updates the instructions based on which page the user is on
     */
    public void nextClicked()
    {
        if (pageCounter == 1)
        {
            line1.setText("The left player will be the blue team, denoted "
                + "by the small square in the bottom right of each unit. "
                + "The right player will be the red team. Each player will "
                + "choose their name based off the profile they select. "
                + "These are important because they'll store your experience!");
            line2.setText("");
            pageCounter++;
        }
        else if (pageCounter == 2)
        {
            line1.setText("Under your base tower (yes, the big black rect"
                + "angle), you will find your health and your experience bar. "
                + "When you run out of health, you lose! So, try to not let "
                + "that happen. You gain experience from killing units "
                + "and winning. Experience unlocks new towers and minions and "
                + "is carried over across games!");
            pageCounter++;
        }
        else if (pageCounter == 3)
        {
            line1.setText("Minions and towers cost gold. You get gold by "
                + "killing units and passively throughout the game. Use it to "
                + "good effect to defeat your opponent! The available units "
                + "can be seen by clicking on the Towers and Minions buttons.");
            pageCounter++;
        }
        else if (pageCounter == 4)
        {
            line1.setText("The orange minion is the basic minion and the "
                + "purple tower is the basic tower. Don't expect too much from "
                + "these guys, but they'll get you started. As you gain "
                + "experience, new minions and towers will show up in your "
                + "menus.");
            pageCounter++;
        }
        else if (pageCounter == 5)
        {
            line1.setText("This game is turn-based. When both players have "
                + "completed their turns, the minions and towers will fight "
                + "each other! Strengthen your army each turn to continue "
                + "the siege. Good luck on the battlefield!");
            pageCounter++;
            next.setEnabled(false);
        }
    }


    // ----------------------------------------------------------
    /**
     * Make the back button on screen do the same as the back button on the
     * device which is returning to the previous activity.
     */
    public void backClicked()
    {
        if (pageCounter == 1)
        {
            super.onBackPressed();
        }
        else if (pageCounter == 2)
        {
            line1.setText("Welcome to Peasants Medieval Siege!");
            line2.setText("Two vengeful rulers have turned "
                + "their peasants into colored shapes of doom and prepare "
                + "to do war against one another!");
            pageCounter--;
        }
        else if (pageCounter == 3)
        {
            line1.setText("The left player will be the blue team, denoted "
                + "by the small square in the bottom right of each unit. "
                + "The right player will be the red team. Each player will "
                + "choose their name based off the profile they select. "
                + "These are important because they'll store your experience!");
            pageCounter--;
        }
        else if (pageCounter == 4)
        {
            line1.setText("Under your base tower (yes, the big black rect"
                + "angle), you will find your health and your experience bar. "
                + "When you run out of health, you lose! So, try to not let "
                + "that happen. You gain experience from killing units "
                + "and winning. Experience unlocks new towers and minions and "
                + "is carried over across games!");
            pageCounter--;
        }
        else if (pageCounter == 5)
        {
            line1.setText("Minions and towers cost gold. You get gold by "
                + "killing units and passively throughout the game. Use it to "
                + "good effect to defeat your opponent! The available units "
                + "can be seen by clicking on the Towers and Minions buttons.");
            pageCounter--;
        }
        else if (pageCounter == 6)
        {
            line1.setText("The orange minion is the basic minion and the "
                + "purple tower is the basic tower. Don't expect too much from "
                + "these guys, but they'll get you started. As you gain "
                + "experience, new minions and towers will show up in your "
                + "menus.");
            pageCounter--;
            next.setEnabled(true);
        }
    }
}
