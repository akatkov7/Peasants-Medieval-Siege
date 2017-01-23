package cs2114.pms.peasantsmedievalsiege;

import android.graphics.RectF;
import sofia.graphics.Color;
import sofia.graphics.RectangleShape;
import sofia.graphics.ShapeField;

// -------------------------------------------------------------------------
/**
 * The Bar class is an class that is extended by various bars to be used in the
 * game. For example, a health bar, action bar, or ammo bar could be extended
 * from this class.
 *
 * @author Andriy Katkov (akatkov)
 * @author Luke Mazzu (mluke94)
 * @author Tony Reiter (treiter)
 * @version 2013.11.15
 */
public class Bar
    extends ShapeField
{
    private int            currentValue;
    private int            maxValue;
    private RectangleShape background;
    private RectangleShape inner;
    private ShapeField     wholeBar;


    // ----------------------------------------------------------
    /**
     * Create a new Bar object.
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
     *            the start value of the bar
     * @param maxVal
     *            the maximum value that the bar can represent
     * @param col
     *            the color of the bar
     */
    public Bar(
        float left,
        float top,
        float right,
        float bottom,
        int startVal,
        int maxVal,
        Color col)
    {
        currentValue = startVal;
        maxValue = maxVal;
        background = new RectangleShape(new RectF(left, top, right, bottom));
        background.setFillColor(Color.gray);
        this.add(background);
        inner =
            new RectangleShape(left, top, left + (((float)startVal) / maxVal)
                * (right - left), bottom);
        inner.setZIndex(1000);
        inner.setFillColor(col);
        this.add(inner);
        wholeBar = new ShapeField();
        wholeBar.add(background);
        wholeBar.add(inner);
    }


    // ----------------------------------------------------------
    /**
     * @return all the graphics for the bar
     */
    public ShapeField getWholeBar()
    {
        return wholeBar;
    }


    // ----------------------------------------------------------
    /**
     * @return the currentValue of the bar
     */
    public int getCurrentValue()
    {
        return currentValue;
    }


    // ----------------------------------------------------------
    /**
     * @return the maxValue of the bar
     */
    public int getMaxValue()
    {
        return maxValue;
    }


    /**
     * Changes the current value
     *
     * @param newCurrent
     *            the new current value to be updated
     */
    public void setCurrentValue(int newCurrent)
    {
        currentValue = newCurrent;
    }


    /**
     * Changes the maximum value
     *
     * @param newMax
     *            the new maximum value
     */
    public void setMaxValue(int newMax)
    {
        maxValue = newMax;
    }


    // ----------------------------------------------------------
    /**
     * Removes amountToBeRemoved from the currentValue and then should call
     * redraw to reflect these changes graphically.
     *
     * @param amountToBeRemoved
     *            the amount to be removed from currentValue
     */
    public void remove(int amountToBeRemoved)
    {
        currentValue = Math.max(0, currentValue - amountToBeRemoved);
        redraw();
    }


    // ----------------------------------------------------------
    /**
     * Adds amountToBeAdded to the currentValue and then should call redraw to
     * reflect these changes graphically.
     *
     * @param amountToBeAdded
     *            the amount to be added to currentValue
     */
    public void add(int amountToBeAdded)
    {
        currentValue = Math.min(maxValue, currentValue + amountToBeAdded);
        redraw();
    }


    // ----------------------------------------------------------
    /**
     * Resets the bar to empty (currentValue = 0) and then should call redraw to
     * reflect these changes graphically.
     */
    public void empty()
    {
        currentValue = 0;
        redraw();
    }


    // ----------------------------------------------------------
    /**
     * @return true if the bar is full, false if not
     */
    public boolean isFull()
    {
        return currentValue == maxValue;
    }


    // ----------------------------------------------------------
    /**
     * Redraws the bar's contents to reflect changes in the currentValue. Should
     * be called after all changes in remove, add, refill, or empty.
     */
    public void redraw()
    {
        RectF b = inner.getBounds();
        inner.setBounds(new RectF(
            b.left,
            b.top,
            b.left + (((float)currentValue) / maxValue)
                * (background.getBounds().right - background.getBounds().left),
            b.bottom));
    }


    /**
     * Only to be used when first instantiating a minion or tower.
     *
     * @param left
     *            the left coordinate
     * @param top
     *            the top coordinate
     * @param right
     *            the right coordinate if the bar is completely full
     * @param bottom
     *            the bottom coordinate
     */
    public void moveTo(float left, float top, float right, float bottom)
    {
        background.setBounds(new RectF(left, top, right, bottom));
        inner.setBounds(new RectF(left, top, left + ((float)currentValue)
            / maxValue * (right - left), bottom));
    }

}
