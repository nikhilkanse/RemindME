package com.in.nyk.remindme.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.common.api.ApiException;
import com.in.nyk.remindme.R;
import com.in.nyk.remindme.activity.BaseActivity;
import com.in.nyk.remindme.core.RemindMEApplication;
import com.in.nyk.remindme.util.ErrorHandler;
import com.in.nyk.remindme.util.FontManager;
import com.in.nyk.remindme.util.NetworkUtil;
import com.in.nyk.remindme.util.RemindMEException;

/**
 * Created by nikhilkanse on 27/11/17.
 */

public abstract class BaseFragment extends Fragment {

    public static final String INVALIDATE_DRAWER_ITEM_SELECTION = "invalid drawer item";
    private ViewFlipper mViewFlipper;
    private IFloatingActionCallBackListener mCallbackListener;
    private boolean mShouldOverrideTitle = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Do not override this method unless and until required.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewFlipper = (ViewFlipper) view.findViewById(R.id.base_fragment_view_flipper);
        if (((ProgressBar) mViewFlipper.getChildAt(0).findViewById(R.id.loading_view)).getIndeterminateDrawable() != null) {
            ((ProgressBar) mViewFlipper.getChildAt(0).findViewById(R.id.loading_view)).getIndeterminateDrawable().setColorFilter(RemindMEApplication.getAppContext().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
        FrameLayout contentViewContainer = (FrameLayout) mViewFlipper.getChildAt(2);
        View contentView = getContentView(LayoutInflater.from(getActivity()), null);
        if (contentView != null)
            contentViewContainer.addView(contentView);
        mViewFlipper.setDisplayedChild(VIEW_MODES.CONTENT_VIEW.ordinal());
    }

    public void setmShouldOverrideTitle(boolean mShouldOverrideTitle) {
        this.mShouldOverrideTitle = mShouldOverrideTitle;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCallbackListener != null) {
            if (shouldShowFAB()) {
                mCallbackListener.showFloatingAction();
            } else {
                mCallbackListener.hideFloatingAction();
            }
            if (mShouldOverrideTitle) {
                mCallbackListener.setToolbarTitle(getTitle());
            }
        }
    }

    protected void setToolbarTitle(String title) {
        if (mCallbackListener != null)
            mCallbackListener.setToolbarTitle(title);
    }

    protected boolean shouldShowFAB() {
        return true;
    }

    protected boolean showView(VIEW_MODES mode) {
        boolean retVal = false;
        if (this.mViewFlipper != null && this.mViewFlipper.getDisplayedChild() != mode.ordinal()) {
            mViewFlipper.setDisplayedChild(mode.ordinal());
            retVal = true;
        }
        return retVal;
    }


    protected boolean showMessage(String text, int imageId, View.OnClickListener tryAgainClickListener) {
        boolean retVal = false;
        if (this.mViewFlipper != null) {
            RelativeLayout relativeLayout = (RelativeLayout) mViewFlipper.getChildAt(1);
            ((ImageView) relativeLayout.findViewById(R.id.error_image)).setImageResource(imageId);
            ((TextView) relativeLayout.findViewById(R.id.error_text)).setText(text);
            TextView refreshButton = (TextView) relativeLayout.findViewById(R.id.refreshIcon);
            refreshButton.setTypeface(FontManager.getTypeface(getCurrentActivity(),"&#xf021;"));

            if (tryAgainClickListener == null)
                refreshButton.setVisibility(View.GONE);
            else refreshButton.setOnClickListener(tryAgainClickListener);
            mViewFlipper.setDisplayedChild(1);

            retVal = true;
        }
        return retVal;
    }

    protected boolean showMessage(String textMsg, String errorMsg, int imageId, final boolean shouldLaunchHome) {
        boolean retVal = false;
        if (this.mViewFlipper != null) {
            RelativeLayout relativeLayout = (RelativeLayout) mViewFlipper.getChildAt(1);
            ((ImageView) relativeLayout.findViewById(R.id.error_image)).setImageResource(imageId);
            ((TextView) relativeLayout.findViewById(R.id.error_text)).setText(textMsg);
            TextView boldText = (TextView) relativeLayout.findViewById(R.id.display_msg);
            (relativeLayout.findViewById(R.id.refreshIcon)).setVisibility(View.GONE);
            boldText.setText(errorMsg);
            mViewFlipper.setDisplayedChild(1);
            retVal = true;
        }
        return retVal;
    }

    public void setupToolBar() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).showToolBarWithTitle(getTitle());
    }

    protected String getTitle() {
        return null;
    }

    protected void showMessage(String text) {
        showMessage(text, R.mipmap.sad_cloud, null);
    }

    protected void showMessage(String text, View.OnClickListener tryAgainClickListener) {
        showMessage(text, R.mipmap.sad_cloud, tryAgainClickListener);
    }

    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

    protected void handleError(RemindMEException exception, View.OnClickListener tryAgainClickListener) {
        //TODO Error handler for clearing user details and all.
        ErrorHandler.handleError(getActivity(), exception);
        showMessage(NetworkUtil.getErrorMessage(exception), tryAgainClickListener);
    }

    //TODO : Specific to error handling for api.
    protected void handleError(ApiException e) {

    }

    public boolean shouldGoBack() {
        return true;
    }

    //use getCurrentActivity the current activity context.
    //When we change the orientation, the child fragments getActivity() returns the old activity reference, to which it was attached.
    public FragmentActivity getCurrentActivity() {
        if (this.getParentFragment() != null) {
            return this.getParentFragment().getActivity();
        } else {
            return getActivity();
        }
    }

    protected enum VIEW_MODES {
        LOADING_VIEW, EMPTY_VIEW, CONTENT_VIEW;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public abstract String getDrawerTitle();

    protected void sendScreenAnalytics() {
        //TODO: this needs to be more user readable for every fragment
        if (this.getParentFragment() == null)// make sure this is not nested fragment and is directly attached to the parent activity
        {
            String screenName = ((getDrawerTitle() != null) && (!getDrawerTitle().trim().isEmpty()) && (getDrawerTitle() != BaseFragment.INVALIDATE_DRAWER_ITEM_SELECTION)) ? this.getDrawerTitle().trim() : this.getClass().getSimpleName();
//            AnalyticsServiceManager.getInstance().send(AnalyticsDataCreator.createScreenViewHit(screenName));
        }
    }

    public interface IFloatingActionCallBackListener {
        public void showFloatingAction();

        public void hideFloatingAction();

        void setToolbarTitle(String title);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFloatingActionCallBackListener) {
            mCallbackListener = (IFloatingActionCallBackListener) context;
        }
    }
}
