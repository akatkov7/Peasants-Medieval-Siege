package cs2114.pms.peasantsmedievalsiege;

import android.util.Log;
import sofia.graphics.Color;
import java.util.ArrayList;
import sofia.graphics.RectangleShape;
import android.graphics.PointF;
import sofia.graphics.LineShape;
import sofia.graphics.ShapeField;
import sofia.graphics.Shape;
import java.util.List;

// -------------------------------------------------------------------------
/**
 * A small menu that pops up when the user attempts to add a tower or minion.
 * Displays the different options by using the color of the minion or tower.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class PopupMenu
    extends ShapeField
{

    private LineShape      l1;
    private LineShape      l2;
    private RectangleShape background;
    private List<Shape>    menu;
    private List<Integer>  menuOptions;
    private Shape          trigger;
    private GameScreen     screen;
    private boolean        showing = false;


    // ----------------------------------------------------------
    /**
     * Create a new PopupMenu object.
     *
     * @param trigger
     *            the shape that this menu is attached to
     * @param menuOptions
     *            the list of options the menu presents
     * @param self
     *            the screen to add all the components on, easier to handle in
     *            this class with multiple components
     */
    public PopupMenu(Shape trigger, List<Integer> menuOptions, GameScreen self)
    {
        screen = self;
        this.menuOptions = menuOptions;
        this.trigger = trigger;
        PointF p = trigger.getPosition();
        // lines pointing to the edges of the menu
        l1 = new LineShape(p.x, p.y, p.x - 19, p.y - 2);
        l2 = new LineShape(p.x, p.y, p.x + 19, p.y - 2);
        // the background for the menu
        background = new RectangleShape(p.x - 19, p.y - 40, p.x + 19, p.y - 2);
        background.setFillColor(Color.gray);
        background.setZIndex(1500);
        menu = new ArrayList<Shape>();
        int len = menuOptions.size();
        // add the various options to the menu
        for (int r = 0; r <= (len - 1) / 3; r++)
        {
            for (int c = 0; c < Math.min(3, len - 3 * r); c++)
            {
                RectangleShape temp =
                    new RectangleShape(p.x - 19 + 2 * (c + 1) + 10 * c, p.y - 2
                        - 2 * (3 - r) - 10 * (3 - r), p.x - 19 + 2 * (c + 1)
                        + 10 * c + 10, p.y - 2 - 2 * (3 - r) - 10 * (3 - r)
                        + 10);
                Log.d("MATH", 3 * r * c + "");
                temp.setFillColor(Color.fromRawColor(menuOptions.get(3 * r + c)));
                // temp.setImage(menuOptions.get(3 * r + c));
                temp.setZIndex(5000);
                menu.add(temp);
            }
        }
        // make the menues invisible until the user clicks on them
        l1.setVisible(false);
        l2.setVisible(false);
        background.setVisible(false);
        for (Shape s : menu)
        {
            s.setVisible(false);
            screen.add(s);
        }
        screen.add(l1);
        screen.add(l2);
        screen.add(background);
        for (Shape s : menu)
        {
            screen.add(s);
        }

    }


    // ----------------------------------------------------------
    /**
     * Shows the options to the user
     */
    public void show()
    {
        l1.setVisible(true);
        l2.setVisible(true);
        background.setVisible(true);
        for (Shape s : menu)
        {
            s.setVisible(true);
        }
        showing = true;
    }


    // ----------------------------------------------------------
    /**
     * Collapses the menu
     */
    public void collapse()
    {
        l1.setVisible(false);
        l2.setVisible(false);
        background.setVisible(false);
        for (Shape s : menu)
        {
            s.setVisible(false);
        }
        showing = false;
    }


    // ----------------------------------------------------------
    /**
     * @return true if showing, false if not
     */
    public boolean isShowing()
    {
        return showing;
    }


    // ----------------------------------------------------------
    /**
     * @return the background for this menu
     */
    public RectangleShape getBackground()
    {
        return background;
    }


    // ----------------------------------------------------------
    /**
     * @return the list containing the shapes that represent the options in the
     *         menu
     */
    public List<Shape> getMenuOptions()
    {
        return menu;
    }


    // ----------------------------------------------------------
    /**
     * Add an item to the menu
     *
     * @param color
     *            the color of the item to add
     */
    public void addMenuItem(int color)
    {
        menuOptions.add(color);
        for (Shape s : menu)
        {
            s.remove();
        }
        // redraw the menu options with the added menu item
        menu = new ArrayList<Shape>();
        int len = menuOptions.size();
        PointF p = trigger.getPosition();
        for (int r = 0; r <= (len - 1) / 3; r++)
        {
            for (int c = 0; c < Math.min(3, len - 3 * r); c++)
            {
                RectangleShape temp =
                    new RectangleShape(p.x - 19 + 2 * (c + 1) + 10 * c, p.y - 2
                        - 2 * (3 - r) - 10 * (3 - r), p.x - 19 + 2 * (c + 1)
                        + 10 * c + 10, p.y - 2 - 2 * (3 - r) - 10 * (3 - r)
                        + 10);
                Log.d("MATH", 3 * r * c + "");
                temp.setFillColor(Color.fromRawColor(menuOptions.get(3 * r + c)));
                // temp.setImage(menuOptions.get(3 * r + c));
                temp.setZIndex(5000);
                menu.add(temp);
            }
        }
        for (Shape s : menu)
        {
            s.setVisible(false);
            screen.add(s);
        }
    }
}
