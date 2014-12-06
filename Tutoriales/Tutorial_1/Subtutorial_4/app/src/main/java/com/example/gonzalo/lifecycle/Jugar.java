package com.example.gonzalo.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


public class Jugar extends Activity {

    static final String STATE_SCORE = "playerScore";
    static final String STATE_LEVEL = "playerLevel";

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int score = 50;
    public int level = 7;

    /** Called when the activity is first created. */
	@Override
  public void onCreate (Bundle savedInstanceState) {
      //Toast.makeText(this, "Level: " + getLevel(), Toast.LENGTH_LONG);
      //Toast.makeText(this, "Score: " + getScore(), Toast.LENGTH_LONG);
	  super.onCreate (savedInstanceState);
	  setContentView (R.layout.jugar);
	}

  public void onSaveInstanceState (Bundle savedInstanceState) {
      setScore(getScore() + getLevel());
      setLevel(getLevel() + 1);
      savedInstanceState.putInt(STATE_SCORE, getScore());
      savedInstanceState.putInt(STATE_LEVEL, getLevel());
      super.onSaveInstanceState(savedInstanceState);
  }

  public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      score = savedInstanceState.getInt(STATE_SCORE);
      level = savedInstanceState.getInt(STATE_LEVEL);
  }

}
