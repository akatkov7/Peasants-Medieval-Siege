package cs2114.pms;

import android.widget.Button;
import android.widget.TextView;
import student.AndroidTestCase;

// -------------------------------------------------------------------------
/**
 * Tests the instruction screen class.
 *
 * @author Andriy
 * @version Dec 7, 2013
 */
public class InstructionScreenTest
    extends AndroidTestCase<InstructionScreen>
{
    private Button   next;
    private Button   back;
    private TextView line1;


    /**
     * Constructor for InstuctionScreen
     */
    public InstructionScreenTest()
    {
        super(InstructionScreen.class);
    }

    /**
     * The set up method, do nothing in this method
     */
    public void setUp()
    {
        // do nothing
    }


    // ----------------------------------------------------------
    /**
     * Tests the next button in InstructionClass
     */
    public void testNext()
    {
        String temp = line1.getText().toString();
        for (int i = 0; i < 6; i++)
        {
            click(next);
            // assert that the text is changing
            assertFalse(temp.equals(line1.getText().toString()));
            temp = line1.getText().toString();
        }
        assertFalse(next.isEnabled());
    }


    // ----------------------------------------------------------
    /**
     * Tests the back button in InstructionClass
     */
    public void testBack()
    {
        String temp = line1.getText().toString();
        for (int i = 0; i < 6; i++)
        {
            click(next);
            // assert that the text is changing
            assertFalse(temp.equals(line1.getText().toString()));
            temp = line1.getText().toString();
        }
        assertFalse(next.isEnabled());
        click(back);
        assertTrue(next.isEnabled());
        for (int i = 0; i < 5; i++)
        {
            click(back);
            // assert that the text is changing
            assertFalse(temp.equals(line1.getText().toString()));
            temp = line1.getText().toString();
        }
    }
}
