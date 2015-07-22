package com.example.gonzalo.schoolapp.utilities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.gonzalo.schoolapp.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Gonzalo on 23/04/2015.
 */
public class TabsViewPagerAdapter extends FragmentPagerAdapter {

    public static final int welcome = 0;
    public static final int help = 1;
    public static final int aboutMe = 2;

    static String welcome_tab = "Bienvenido";
    static String help_tab = "Ayuda";
    static String aboutMe_tab = "Sobre Mi";

    private ArrayList<Fragment> fragments;

    public TabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);

        String localeString = Locale.getDefault().toString();
        if (localeString.equals("es")) {
            welcome_tab = "Bienvenido";
            help_tab = "Ayuda";
            aboutMe_tab = "Sobre Mi";
        } else if (localeString.equals("en")) {
            welcome_tab = "Welcome";
            help_tab = "Help";
            aboutMe_tab = "About Me";
        } else if (localeString.equals("fr")) {
            welcome_tab = "Bienvenue";
            help_tab = "Aidez-Moi";
            aboutMe_tab = "Sur Moi";
        } else {
            welcome_tab = "Bienvenido";
            help_tab = "Ayuda";
            aboutMe_tab = "Sobre Mi";
        }

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
