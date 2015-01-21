package com.example.gonzalo.androidtestingfun.lesson3;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.gonzalo.androidtestingfun.R;

/**
 * Created by Gonzalo on 21/01/2015.
 *
 * Tests for ClickFunActivity. Introduces touch mode, test size annotations and TouchUtils.
 */
public class ClickFunActivityTest extends ActivityInstrumentationTestCase2<ClickFunActivity> {

    private ClickFunActivity mClickFunActivity;
    private Button mClickMeButton;
    private TextView mInfoTextView;

    public ClickFunActivityTest() {
        super(ClickFunActivity.class);
    }

    /**
     * Sets up the test fixture for this test case. This method is always called before every test
     * run.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        //Sets the initial touch mode for the Activity under test. This must be called before
        //getActivity()
        setActivityInitialTouchMode(true);

        //Get a reference to the Activity under test, starting it if necessary.
        mClickFunActivity = getActivity();

        //Get references to all views
        mClickMeButton = (Button) mClickFunActivity.findViewById(R.id.launch_next_activity_button);
        mInfoTextView = (TextView) mClickFunActivity.findViewById(R.id.info_text_view);
    }

    /**
     * Tests the preconditions of this test fixture.
     */
    @MediumTest
    public void testPreconditions() {
        assertNotNull("mClickFunActivity is null", mClickFunActivity);
        assertNotNull("mClickMeButton is null", mClickMeButton);
        assertNotNull("mInfoTextView is null", mInfoTextView);
    }

    @MediumTest
    public void testClickMeButton_layout() {
        //Retrieve the top-level window decor view
        final View decorView = mClickFunActivity.getWindow().getDecorView();

        //Verify that the mClickMeButton is on screen
        ViewAsserts.assertOnScreen(decorView, mClickMeButton);

        //Verify width and heights
        final ViewGroup.LayoutParams layoutParams = mClickMeButton.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @MediumTest
    public void testClickMeButton_labelText() {
        //Verify that mClickMeButton uses the correct string resource
        final String expectedNextButtonText = mClickFunActivity.getString(R.string.label_click_me);
        final String actualNextButtonText = mClickMeButton.getText().toString();
        assertEquals(expectedNextButtonText, actualNextButtonText);
    }

    @MediumTest
    public void testInfoTextView_layout() {
        //Retrieve the top-level window decor view
        final View decorView = mClickFunActivity.getWindow().getDecorView();

        //Verify that the mInfoTextView is on screen and is not visible
        ViewAsserts.assertOnScreen(decorView, mInfoTextView);
        assertTrue(View.GONE == mInfoTextView.getVisibility());
    }

    @MediumTest
    public void testInfoTextViewText_isEmpty() {
        //Verify that the mInfoTextView is initialized with the correct default value
        assertEquals("", mInfoTextView.getText());
    }

    @MediumTest
    public void testClickMeButton_clickButtonAndExpectInfoText() {
        String expectedInfoText = mClickFunActivity.getString(R.string.info_text);
        //Perform a click on mClickMeButton
        TouchUtils.clickView(this, mClickMeButton);
        //Verify the that mClickMeButton was clicked. mInfoTextView is visible and contains
        //the correct text.
        assertTrue(View.VISIBLE == mInfoTextView.getVisibility());
        assertEquals(expectedInfoText, mInfoTextView.getText());
    }
}
