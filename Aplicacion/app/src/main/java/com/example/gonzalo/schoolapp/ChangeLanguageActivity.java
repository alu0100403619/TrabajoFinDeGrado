package com.example.gonzalo.schoolapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.gonzalo.schoolapp.utilities.Utilities;

import java.util.Locale;


public class ChangeLanguageActivity extends Activity {

    RadioButton spanish, english, french;
    Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utilities.loadLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);

        spanish = (RadioButton) findViewById(R.id.spanish);
        english = (RadioButton) findViewById(R.id.english);
        french = (RadioButton) findViewById(R.id.french);

        String localeString = Locale.getDefault().toString();
        Log.i("CLA", "Locale: "+localeString);
        if (localeString.equals(getString(R.string.es))) {
            spanish.setChecked(true);
        } else if (localeString.equals(getString(R.string.en))) {
            english.setChecked(true);
        } else if (localeString.equals(getString(R.string.fr))) {
            french.setChecked(true);
        } else {
            spanish.setChecked(true);
        }
        locale = new Locale(localeString);
        changeLanguage(locale);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_language, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectLanguage(View view) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.selectLanguaje);
        int selectedLanguage = radioGroup.getCheckedRadioButtonId();
        switch (selectedLanguage) {
            case R.id.spanish:
                locale = new Locale(getString(R.string.es));
                break;
            case R.id.english:
                locale = new Locale(getString(R.string.en));
                break;
            case R.id.french:
                locale = new Locale(getString(R.string.fr));
                break;
            default:
                locale = new Locale(getString(R.string.es));
                break;
        }//switch
        changeLanguage(locale);
    }

    public void back(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed () {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void changeLanguage(Locale locale) {
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().
                getResources().getDisplayMetrics());
        saveLanguage(locale);
        //Locale.setDefault(locale);
    }

    public void saveLanguage(Locale locale) {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String languaje = locale.toString();
        Log.i("CLA", "Language save: " + languaje);
        editor.putString(getString(R.string.language), languaje);
        editor.commit();
    }
}
