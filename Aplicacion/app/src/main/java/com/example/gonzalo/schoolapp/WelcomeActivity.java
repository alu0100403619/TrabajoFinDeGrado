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
import android.widget.Toast;

import com.example.gonzalo.schoolapp.fragments.AboutFragment;
import com.example.gonzalo.schoolapp.fragments.AboutMeFragment;
import com.example.gonzalo.schoolapp.fragments.WelcomeFragment;
import com.example.gonzalo.schoolapp.utilities.TabsViewPagerAdapter;
import com.example.gonzalo.schoolapp.utilities.Utilities;
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
        fragments.add(new AboutFragment());
        fragments.add(new AboutMeFragment());

        // use FragmentPagerAdapter to bind the slidingTabLayout (tabs with different titles)
        // and ViewPager (different pages of fragment) together.
        tabsViewPageAdapter =new TabsViewPagerAdapter(getFragmentManager(),
                fragments);
        viewPager.setAdapter(tabsViewPageAdapter);

        //Aseguremonos de que las tabs esten bien espaciadas.
        slidingTabLayout.setDistributeEvenly(true);
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
        else if (id == R.id.action_change_language) {
            Intent intent = new Intent(this, ChangeLanguageActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchLogin(View view) {
        if (!Utilities.haveInternet(this)) {
            //startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            Toast.makeText(WelcomeActivity.this,
                    getString(R.string.wifi_data_error),
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    public void launchRegister(View view) {
        if (!Utilities.haveInternet(this)) {
            //startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            Toast.makeText(WelcomeActivity.this,
                    getString(R.string.wifi_data_error),
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

}
