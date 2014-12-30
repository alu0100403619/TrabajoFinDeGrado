package com.example.gonzalo.addinganimations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Gonzalo on 30/12/2014.
 */
public class CrossfadeActivity extends Activity {

    private boolean mContentLoaded; //Contenido cargado??
    private View mContentView; //Vista con el contenido
    private View mLoadingView; //vista con Spinner
    private int mShortAnimationDuration; //Duración de la animación en ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfade);
        mContentView = findViewById(R.id.content);
        mLoadingView = findViewById(R.id.loading_spinner);
        mContentView.setVisibility(View.GONE); //Conetnido escondido al principio

        // Recuperar y almacenar en caché por defecto "corto" tiempo de animación del sistema.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_crossfade, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_toggle:
                mContentLoaded = !mContentLoaded;
                showContentOrLoadingIndicator(mContentLoaded);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showContentOrLoadingIndicator(boolean contentLoaded) {

        final View showView = contentLoaded ? mContentView : mLoadingView;
        final View hideView = contentLoaded ? mLoadingView : mContentView;

        // Ajuste el "show" vista a 0% de opacidad, pero visible, de modo que sea visible
        // (pero totalmente transparente) durante la animación.
        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        showView.animate() //Animamos la vista a mostrar
                .alpha(1f) //100% de opacidad
                .setDuration(mShortAnimationDuration) //Animación de corta duración
                .setListener(null); //Borrar todos los listener

        hideView.animate() //Animamos la vista a ocultar
                .alpha(0f) //0% de opacidad
                .setDuration(mShortAnimationDuration) //Animación de corta duración
                .setListener(new AnimatorListenerAdapter() { //Nuevo listener
                    @Override
                    public void onAnimationEnd(Animator animation) { //Al finalizar la animación
                        hideView.setVisibility(View.GONE); //Visibilidad=IDO(parte de optimización)
                    }
                });
    }
}