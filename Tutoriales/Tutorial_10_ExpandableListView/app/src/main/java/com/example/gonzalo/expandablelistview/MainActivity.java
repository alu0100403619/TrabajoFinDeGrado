package com.example.gonzalo.expandablelistview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
public class MainActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtener el elemento xml
        expListView = (ExpandableListView) findViewById(R.id.expListView);

        //Preparar los datos
        prepareListData();

        //Adaptador
        listAdapter = new ExpandableListAdapter (this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        //Listeners
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });//OnChildClickListener

        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });//OnGroupExpandListener

        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });//OnGroupCollapseListener
    }

    //Preparando lista de datos
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //Adding Header Data
        listDataHeader.add("Top 10");
        listDataHeader.add("Siguiendo Ahora");
        listDataHeader.add("Próximamente...");

        //Adding Child Data
        List<String> top10 = new ArrayList<String>();
        top10.add("Queer as Folk");
        top10.add("Dante's Cove");
        top10.add("Avatar");
        top10.add("Game of Thrones");
        top10.add("Los Simpsons");
        top10.add("La que se Avecina");
        top10.add("Junjou Romantica");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Grey's Anatomy");
        nowShowing.add("2 Broke Girl");
        nowShowing.add("Once Upon a Time");
        nowShowing.add("Looking");
        nowShowing.add("Lost Girl");
        nowShowing.add("The Lair");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Dr. Who");
        comingSoon.add("Alles Was Zahlt");
        comingSoon.add("As the World Turns");
        comingSoon.add("Hollyoaks");
        comingSoon.add("Verbotene Liebe");

        // Header, Child data
        listDataChild.put(listDataHeader.get(0), top10);
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }//function

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
