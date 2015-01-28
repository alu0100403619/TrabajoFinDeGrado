package com.example.gonzalo.noteappexternalstorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;


public class edit_note extends ActionBarActivity {

    private Note note;
    private String id;
    private EditText titleEditText;
    private EditText contentEditText;
    private Button saveNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().show();
        setContentView(R.layout.activity_edit_note);

        Intent intent = this.getIntent();
        titleEditText = (EditText) findViewById(R.id.noteTitle);
        contentEditText = (EditText) findViewById(R.id.noteContent);
        if (intent.getExtras() != null){
            note = new Note(intent.getStringExtra("noteId"),
                    intent.getStringExtra("noteTitle"), intent.getStringExtra("noteContent"));
            id = note.getId();
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

    public void saveNote() {
        if (id == null) { id = randomUUID();}
        try {
            File file = new File("/sdcard/" + R.string.file_name);
            if (!file.exists()){
                file.createNewFile();
                Log.d("Save Note", "File Created Succefully");
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(outputStream));
            bufferedWriter.append(note.getId()); //------------ERROR-----------------------
            bufferedWriter.append(note.getTitle());
            bufferedWriter.append(note.getContent());//------------------------------------
            bufferedWriter.close();
        }//try
        catch (IOException e) {
            e.printStackTrace();
        }//catch

        //Cargar la vista principal
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private String randomUUID() {
        return UUID.randomUUID().toString();
    }

}//class
