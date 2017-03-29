package com.alequinonboard.notes;

//31/10/16

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Alequin on 13/08/2016.
 * updated 19.12.16
 *
 * had issues with this code while working with landscape keyboards that go full screen
 */
public class SoftInputVisibilityController {

    private SoftInputVisibilityController(){}

    public static void showSoftInput(Activity activity){

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        // This line makes keyboard pop up
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        imm.showSoftInput(view, 0);

    }

    public static void hideAndResetSoftInput(Activity activity){

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        //line resets the InputMethodManager so that when showSoftInput is called it starts from its
        //Original state
        //show soft input will not work (after hideAndResetSoftInput has been called) if this line is not included
        imm.restartInput(view);
    }
}
