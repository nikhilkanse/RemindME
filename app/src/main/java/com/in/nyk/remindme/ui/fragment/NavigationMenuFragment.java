package com.in.nyk.remindme.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.in.nyk.remindme.R;

/**
 * Created by nikhilkanse on 27/11/17.
 */

public class NavigationMenuFragment extends BaseFragment {
    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public String getDrawerTitle() {
        return null;
    }
}
