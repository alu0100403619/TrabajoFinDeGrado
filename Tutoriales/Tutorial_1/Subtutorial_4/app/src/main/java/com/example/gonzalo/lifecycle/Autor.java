package com.example.gonzalo.lifecycle;

import android.app.Activity;
import android.os.Bundle;


public class Autor extends Activity {

	/** Called when the activity is first created. */
	@Override
  public void onCreate (Bundle savedInstanceState) {
	  super.onCreate (savedInstanceState);
	  setContentView (R.layout.autor);
	}

}
