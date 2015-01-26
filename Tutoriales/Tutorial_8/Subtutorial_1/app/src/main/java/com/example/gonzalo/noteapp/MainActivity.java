package com.example.gonzalo.noteapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Tutuorial:
//http://www.sitepoint.com/creating-cloud-backend-android-app-using-parse/
public class MainActivity extends ListActivity {

    public static final String APPLICATION_ID = "hLnkclA8KI9Vbgfcgf3TLPfQOUsK4VCy7FvidT8f";
    public static final String CLIENT_KEY = "Hme8qIm4TBXXTe3q5lyGiLJS71DfOjMxUmSI0yga";
    private List<Note> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }

        posts = new ArrayList<Note>();
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this, R.layout.list_item_layout, posts);
        setListAdapter(adapter);

        refreshPostList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_refresh: {
             //refreshPostList();
                break;
            }case R.id.action_new: {
                Intent intent = new Intent(this, EditNoteActivity.class);
                startActivity(intent);
                break;
            }case R.id.action_logout: {
                ParseUser.logOut();
                loadLoginView();
                break;
            }case R.id.action_settings: {
                //return true;
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //Creada por que no se ve el nombre de la aplicación ni el menù
    public void launchNewNote(View view) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        startActivity(intent);
    }

    //Creada por que no se ve el nombre de la aplicación ni el menù
    public void launchLogout(View view) {
        ParseUser.logOut();
        loadLoginView();
    }

    //Creada por que no se ve el nombre de la aplicación ni el menù
    public void refreshView(View view) {
        refreshPostList();
    }

    private void refreshPostList() { //Original del tutorial
        Object[] authors = {null, ParseUser.getCurrentUser()};
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        //query.whereEqualTo("author", ParseUser.getCurrentUser()); //Usuario actual
        //query.whereEqualTo("author", null); //Notas sin autor
        query.whereContainedIn("author", Arrays.asList(authors));
        setProgressBarIndeterminateVisibility(true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    posts.clear();
                    for (ParseObject post : postList) {
                        Note note = new Note(post.getObjectId(),
                                post.getString("title"), post.getString("content"));
                        posts.add(note);
                    }//for
                    ((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged();
                }//if
                else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }//else
            }
        });//query.findInBackground
    }

    //(ListView l, View v, int position, long id)
    public void onListItemClick(ListView list, View view, int position, long id) {
        Note note = posts.get(position);
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("noteId", note.getId());
        intent.putExtra("noteTitle", note.getTitle());
        intent.putExtra("noteContent", note.getContent());
        startActivity(intent);
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, activity_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}//class
