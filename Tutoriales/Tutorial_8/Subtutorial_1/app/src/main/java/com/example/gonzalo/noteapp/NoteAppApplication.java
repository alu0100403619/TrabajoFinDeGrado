package com.example.gonzalo.noteapp;

import android.app.Activity;
import android.os.Bundle;

import com.parse.Parse;

/**
 * Created by Gonzalo on 22/01/2015.
 */
public class NoteAppApplication extends Activity {

    public static final String APPLICATION_ID = "hLnkclA8KI9Vbgfcgf3TLPfQOUsK4VCy7FvidT8f";
    public static final String CLIENT_KEY = "Hme8qIm4TBXXTe3q5lyGiLJS71DfOjMxUmSI0yga";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        /*ParseObject testObject = new ParseObject("Test Object");
        testObject.put("foo", "bar");
        testObject.saveInBackground();//*/
    }
}
