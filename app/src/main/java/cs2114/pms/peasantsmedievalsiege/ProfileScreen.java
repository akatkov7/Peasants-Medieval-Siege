package cs2114.pms.peasantsmedievalsiege;

import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import sofia.app.Screen;
import sofia.widget.Spinner;

// -------------------------------------------------------------------------
/**
 * This is the screen which allows for the users to select which profile will
 * represent each user. This is used to maintain xp values across game sessions,
 * allowing for frequent users to unlock more content.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class ProfileScreen
    extends Screen
{
    private final String         prefFile = "ProfileStorage";
    private Map<String, Profile> profiles;
    private SharedPreferences    savedProfiles;
    private Editor               editor;
    private Spinner<String>      selector;
    private Profile              leftPlayer;
    private Profile              rightPlayer;
    private TextView             playerSelecting;
    private EditText             nameField;


    // ----------------------------------------------------------
    /**
     * Initialize the profiles stored and the editor used to modify them, store
     * the profiles in a HashMap and then add the current profiles to the
     * spinner for user selection
     */
    public void initialize()
    {
        profiles = new HashMap<String, Profile>();
        savedProfiles = getSharedPreferences(prefFile, MODE_PRIVATE);
        // savedProfiles = getPreferences(MODE_PRIVATE);
        editor = savedProfiles.edit();
        //editor.putString("Gooby", "Gooby 1 0").commit();
        //editor.putString("Dumbo", "Dumbo 1 0").commit();
        for (String key : savedProfiles.getAll().keySet())
        {
            Profile temp = new Profile(savedProfiles.getString(key, "null"));
            profiles.put(temp.getName(), temp);
        }
        for (String name : profiles.keySet())
        {
            selector.add(name);
        }
    }


    // ----------------------------------------------------------
    /**
     * Adds the profile the user requests to the HashMap of profiles (local) and
     * the SharedPreferences (persistent)
     *
     * @param name
     *            the name of the profile
     */
    public void addProfile(String name)
    {
        editor.putString(name, name + " 1 0").commit();
        profiles.put(name, new Profile(name + " 1 0"));
    }


    // ----------------------------------------------------------
    /**
     * Remove the profile being displayed on the spinner from the HashMap
     * (local), the spinner (local), and the SharedPreferences (persistent)
     */
    public void deleteProfileClicked()
    {
        profiles.remove(selector.getSelectedItem());
        editor.remove(selector.getSelectedItem()).commit();
        selector.remove(selector.getSelectedItem());
    }


    // ----------------------------------------------------------
    /**
     * When the user finishes typing a name, add it to the HashMap, spinner, and
     * SharedPreferences. Finally, clear the edittext object
     */
    public void nameFieldEditingDone()
    {
        String name = nameField.getText().toString().trim();
        selector.add(name);
        addProfile(name);
        nameField.setText("");
    }


    // ----------------------------------------------------------
    /**
     * If Player 1 presses next, then allow for Player 2 to select a profile, If
     * Player 2 presses next, then make sure the selected profile is different
     * from Player 1's, then send the selected profiles to the GameScreen class
     * so it knows who is playing
     */
    public void nextClicked()
    {
        if (playerSelecting.getText().toString().equals("Player 1"))
        {
            leftPlayer = profiles.get(selector.getSelectedItem());
            playerSelecting.setText("Player 2");
        }
        else
        {
            if (!selector.getSelectedItem().equals(leftPlayer.getName()))
            {
                rightPlayer = profiles.get(selector.getSelectedItem());
                Intent i = new Intent(this, GameScreen.class);
                i.putExtra("leftPlayer", leftPlayer.toString());
                i.putExtra("rightPlayer", rightPlayer.toString());
                setResult(1);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(
                    this,
                    "Please select a different profile from Player 1!",
                    Toast.LENGTH_SHORT).show();
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * If Player 1 presses back, then return to the main menu, If Player 2
     * presses back, then let Player 1 select a profile
     */
    public void backClicked()
    {
        if (playerSelecting.getText().toString().equals("Player 2"))
        {
            playerSelecting.setText("Player 1");
        }
        else
        {
            super.onBackPressed();
        }
    }
}
