package com.in.nyk.remindme.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/**
 * Created by nikhilkanse on 27/11/17.
 *
 * Overriding the onDetachFromWindow for the default android ViewFlipper to resolve a bug in the ViewFlipper.
 * Refs -
 * http://stackoverflow.com/questions/8050730/viewflipper-receiver-not-registered
 * http://daniel-codes.blogspot.in/2010/05/viewflipper-receiver-not-registered.html
 *
 */

public class RemindMEViewFlipper extends ViewFlipper
{
    public RemindMEViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (IllegalArgumentException e) {
            stopFlipping();
        }
    }

}
