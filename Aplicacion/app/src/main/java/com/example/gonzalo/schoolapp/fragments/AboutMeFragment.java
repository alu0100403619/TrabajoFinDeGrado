package com.example.gonzalo.schoolapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gonzalo.schoolapp.R;

/**
 * Created by Gonzalo on 23/04/2015.
 */
public class AboutMeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_me, container, false);
    }
}
