package com.example.gonzalo.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        Toast.makeText (MainActivity.this, "onCreate...", Toast.LENGTH_SHORT).show();
        setContentView (R.layout.activity_main);
    }

    @Override
    protected void onStart () {
        super.onStart ();
        Toast.makeText (MainActivity.this, "onStart...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume () {
        super.onResume ();
        Toast.makeText (MainActivity.this, "onResume...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause () {
        super.onPause ();
        Toast.makeText (MainActivity.this, "onPause...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop () {
        super.onStop();
        Toast.makeText (MainActivity.this, "onStop...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart () {
        super.onRestart ();
        Toast.makeText (MainActivity.this, "onRestart...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        Toast.makeText(MainActivity.this, "onDestroy...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void lanzarAcercaDe (View view) {
        Intent i = new Intent (this, AcercaDe.class);
        startActivity(i);
    }

    public void lanzarAuthor (View view) {
        Intent i = new Intent (this, Autor.class);
        startActivity (i);
    }

    public void lanzarConfiguracion (View view) {
        Intent i = new Intent (this, Configuracion.class);
        startActivity(i);
    }

    public void lanzarJugar (View view) {
        Intent i = new Intent (this, Jugar.class);
        startActivity(i);
    }
}