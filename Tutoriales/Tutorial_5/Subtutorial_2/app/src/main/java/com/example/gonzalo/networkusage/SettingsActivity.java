package com.example.gonzalo.networkusage;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Gonzalo on 09/01/2015.
 */
public class SettingsActivity extends PreferenceActivity
        implements
        OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Carga el Archivo XML de preferencias.
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Registra una llamada al ser invocado cuando un usuario cambia las preferencias.
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregisters the listener set in onResume().
        // It's best practice to unregister listeners when your app isn't using them to cut down on
        // unnecessary system overhead. You do this in onPause().
        getPreferenceScreen()
                .getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    // Se dispara cuando el usuario cambia las preferencias.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Sets refreshDisplay a true cuando el usuario vuelve a la actividad
        // principal, la pantalla se refresca con los nuevos ajustes.
        NetworkActivity.refreshDisplay = true;
    }
}
