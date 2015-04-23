package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.gonzalo.schoolapp.fragments.AboutMeFragment;
import com.example.gonzalo.schoolapp.fragments.HelpFragment;
import com.example.gonzalo.schoolapp.fragments.WelcomeFragment;
import com.example.gonzalo.schoolapp.utilities.TabsViewPagerAdapter;
import com.example.gonzalo.schoolapp.view.SlidingTabLayout;

import java.util.ArrayList;


public class WelcomeActivity extends Activity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private TabsViewPagerAdapter tabsViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Define SlidingTabLayout (Se muestra arriba)
        // y ViewPager (Se muestra abajo) en la vista.
        // obtenemos sus instancias.
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Creamos una lista de fragmentos en orden.
        fragments = new ArrayList<>();
        fragments.add(new WelcomeFragment());
        fragments.add(new HelpFragment());
        fragments.add(new AboutMeFragment());

        // use FragmentPagerAdapter to bind the slidingTabLayout (tabs with different titles)
        // and ViewPager (different pages of fragment) together.
        tabsViewPageAdapter =new TabsViewPagerAdapter(getFragmentManager(),
                fragments);
        viewPager.setAdapter(tabsViewPageAdapter);

        //Asegur&eacute;monos de que las tabs est&aacute;n bien espaciadas.
        //slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setDistributeEvenly(false);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_welcome, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void launchRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
