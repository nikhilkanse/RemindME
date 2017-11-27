package com.in.nyk.remindme.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.in.nyk.remindme.R;
import com.in.nyk.remindme.core.RemindMEApplication;
import com.in.nyk.remindme.util.UIHelper;

/**
 * Created by nikhilkanse on 27/11/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;
    protected boolean mIsResumed = false; //flag used to know if activity is in resumed state. This flag is used to perform fragment transaction on TagUploadCompleteStatusDialog.

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        setUpToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mToolbar != null) {
            UIHelper.hideSoftKeyboard(mToolbar.getFocusedChild());
        }
        UIHelper.hideSoftKeyboard(getCurrentFocus());
        mIsResumed = false;
    }

    protected void hideSoftKeyboard(EditText editText) {
        if (editText != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    protected void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbar.setBackgroundColor(RemindMEApplication.getAppContext().getResources().getColor(R.color.colorPrimary));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected abstract int getLayoutResource();

    /**
     * Clears fragment manager's back stack immediately
     */

    public void flushBackStackCompletely() {
        //On Navigation drawer item clicked, starting with all new backstack.
        if (!isFinishing())
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected Fragment findFragmentByTag(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            return fragmentManager.findFragmentByTag(tag);
        }
        return null;
    }

    protected void replaceFragment(Fragment fragment, boolean doAddToBackstack, String transactionName, String fragmentTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragmentTag);
        if (doAddToBackstack) {
            transaction.addToBackStack(transactionName);
        }
        transaction.commit();
    }

    protected void replaceFragment(Fragment fragment, boolean doAddToBackstack, String transactionName, int container, String fragmentTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(container, fragment, fragmentTag);
        if (doAddToBackstack) {
            transaction.addToBackStack(transactionName);
        }
        transaction.commit();
    }


    /**
     * Convenience method for showing Toast. Derived classes may override this method for customisation of Toast.
     *
     * @param message
     * @param duration
     */

    protected void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    /**
     * Convenience method for showing Progress Dialog.
     *
     * @param message
     * @return
     */
    protected ProgressDialog showProgressDialog(String message) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setMax(100);
        dialog.show();
        return dialog;
    }

    /**
     * This will show toolbar with title and no image.
     *
     * @param title Title to be shown on toolbar
     */
    public void showToolBarWithTitle(String title) {
        if (mToolbar != null && title != null && !title.isEmpty()) {
            {
                mToolbar.setLogo(null);
                mToolbar.setTitle(title);
            }
        }
    }

    /**
     * This will show toolbar with image and no title.
     */
    public void showToolBarWithImage() {
        // for now this is not required
        // whenever required need to implement as per requirement
        throw new UnsupportedOperationException("This is currently not supported");
    }
}
