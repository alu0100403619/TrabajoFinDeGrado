package com.example.gonzalo.schoolapp.utilities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Gonzalo on 23/04/2015.
 */
public class TabsViewPagerAdapter extends FragmentPagerAdapter {

    public static final int welcome = 0;
    public static final int help = 1;
    public static final int aboutMe = 2;

    final static String welcome_tab = "Bienvenido";
    final static String help_tab = "Ayuda";
    final static String aboutMe_tab = "Sobre Mi";

    private ArrayList<Fragment> fragments;

    public TabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public String getPageTitle(int position) {
        switch (position) {
            case welcome:
                return welcome_tab;
            case help:
                return help_tab;
            case aboutMe:
                return aboutMe_tab;
        }//switch
        return null;
    }
}
