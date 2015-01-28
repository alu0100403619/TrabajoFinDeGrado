package com.example.gonzalo.noteappexternalstorage;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {

    private List<Note> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().show();
        setContentView(R.layout.activity_main);

        posts = new ArrayList<Note>();
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this, R.layout.list_item_layout, posts);
        setListAdapter(adapter);

        //Cargar notas desde archivo
        refreshNoteList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_new: {
                Intent intent = new Intent (this, edit_note.class);
                startActivity(intent);
            }case R.id.action_refresh: {
                refreshNoteList();
            }/*case R.id.action_edit: {
                Intent intent = new Intent (this, edit_note.class);
                //put Extras
                startActivity(intent);
            }*/ case R.id.action_settings: {
                return true;
            }
        }//switch

        return super.onOptionsItemSelected(item);
    }

    public void onListItemClick(ListView list, View view, int position, long id) {
        Note note = posts.get(position);
        Intent intent = new Intent(this, edit_note.class);
        //intent.putExtra("noteId", note.getId());
        intent.putExtra("noteTitle", note.getTitle());
        intent.putExtra("noteContent", note.getContent());
        startActivity(intent);
    }

    public void refreshList(View view) {
        refreshNoteList();
    }

    public void launchNewNote(View view) {
        Intent intent = new Intent (this, edit_note.class);
        startActivity(intent);
        this.finish();
    }

    private void refreshNoteList() {
        int i = 0;
        String aDataRow = "";
        String id = "";
        String title = "";
        String content = "";
        Note note;

        try {
            File file = new File("/sdcard/" + R.string.file_name);
            if (file.exists()) {
                Log.d("refreshNote", "File exist");
                FileInputStream inputStream = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream));
                while (aDataRow != null){
                    aDataRow = bufferedReader.readLine();
                    if (i == 0) { id = aDataRow; i++; }//if 0
                    else if (i == 1) { title = aDataRow; i++; }//else if 1
                    else if (i == 2) { content = aDataRow; i++; }//else if 2
                    else {
                        i = 0;
                        id = "";
                        title = "";
                        content = "";
                    }//else
                    //ERROR-------Title SI -- content SI -- Id NO -- aDataRow NO--------------------
                    if ((!id.isEmpty()) && (!title.isEmpty()) && (!content.isEmpty())) {
                        note = new Note(id, title, content);
                        posts.add(note);
                    }//if && &&---------------------------------------------------------------------
                }//while
                bufferedReader.close();
                ((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged();
            } else {
                file.createNewFile();
                Log.d("refreshNote", "File Created Succefully");
            }//else
        }//try
        catch (IOException e) {
            e.printStackTrace();
        }//catch
    }

}//class
