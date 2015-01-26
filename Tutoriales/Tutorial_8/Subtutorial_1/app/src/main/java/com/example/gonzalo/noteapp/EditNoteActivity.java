package com.example.gonzalo.noteapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class EditNoteActivity extends ActionBarActivity {

    private Note note;
    private EditText titleEditText;
    private EditText contentEditText;
    private String postTitle;
    private String postContent;
    private Button saveNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_note);

        Intent intent = this.getIntent();
        titleEditText = (EditText) findViewById(R.id.noteTitle);
        contentEditText = (EditText) findViewById(R.id.noteContent);
        if (intent.getExtras() != null){
            note = new Note(intent.getStringExtra("noteId"),
                    intent.getStringExtra("noteTitle"), intent.getStringExtra("noteContent"));
            titleEditText.setText(note.getTitle());
            contentEditText.setText(note.getContent());
        }//if

        saveNoteButton = (Button) findViewById(R.id.saveNote);
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });//setOnClickListener
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
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

    private void saveNote() {
        postTitle = titleEditText.getText().toString();
        postContent = contentEditText.getText().toString();
        postTitle = postTitle.trim();
        postContent = postContent.trim();
        if (!postTitle.isEmpty()) {
            if (note == null) {
                //Create new post
                final ParseObject post = new ParseObject("Post");
                post.put("title", postTitle);
                post.put("content", postContent);
                post.put("author", ParseUser.getCurrentUser());
                setProgressBarIndeterminateVisibility(true);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        setProgressBarIndeterminateVisibility(false);
                        if (e == null) {
                            note = new Note(post.getObjectId(), postTitle, postContent);
                            Toast.makeText(getApplicationContext(), "Saved",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Save",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "User update error: " + e);
                        }//else
                    }
                });//saveInBackground
            }//if null
            else {
                //Update Post
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
                //Retrieve the object by id
                query.getInBackground(note.getId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject post, ParseException e) {
                        if (e == null) {
                            //Now let's update it with some new data
                            post.put("title", postTitle);
                            post.put("content", postContent);
                            //setProgressBarIndeterminateVisibility(true);
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                   // setProgressBarIndeterminateVisibility(false);
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "Saved",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                                        Log.d(getClass().getSimpleName(), "User update error: " + e);
                                    }
                                }
                            });//saveInBackground
                        }//if e == null
                    }
                });//query
            }
        }//if isEmpty
        else if (postTitle.isEmpty() && !postContent.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
            builder.setMessage(R.string.edit_error_message)
                    .setTitle(R.string.edit_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }//else if
    }//function

}//class
