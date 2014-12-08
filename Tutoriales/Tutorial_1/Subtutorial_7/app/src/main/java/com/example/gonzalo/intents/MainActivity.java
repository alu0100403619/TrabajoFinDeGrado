package com.example.gonzalo.intents;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;

import java.util.List;

public class MainActivity extends Activity {

    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


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

    public void call (View view) {
        Uri number = null;
        TextView textView = (TextView) findViewById(R.id.phone_field);
        if (textView.getText() != null) {
            number = Uri.parse("tel:" + textView.getText());
        } else {
            number = Uri.parse("tel:922127777");
        }
        //Intent callIntent = new Intent(Intent.ACTION_DIAL, number); //Mustra el Dialer
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);//Hace la llamada

        //Verificamos si se resuelve el Intent
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(callIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            startActivity(callIntent);
        }
    }

    public void mapLocation (View view) {
        Uri location = Uri.parse("geo:0,0?q=Buenavista+del+Norte,+Santa+Cruz+de+Tenerife,+Canarias");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

        //Verificamos si se resuelve el Intent
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            startActivity(mapIntent);
        }
    }

    public void webPage (View view) {
        Uri webPage = Uri.parse("http://www.google.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webPage);

        //Verificamos si se resuelve el Intent
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            startActivity(webIntent);
        }
    }

    public void appChooser (View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        String title = getResources().getString(R.string.chooser_title);
        Intent chooser = Intent.createChooser(intent, title);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
    public void pickContact(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String [] projection = {Phone.NUMBER, Phone.DISPLAY_NAME};

                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                int column = cursor.getColumnIndex(Phone.NUMBER);
                String number = cursor.getString(column);

                column = cursor.getColumnIndex(Phone.DISPLAY_NAME);
                String name = cursor.getString(column);

                //Hacer algo con el numero
                TextView phonefield = (TextView) findViewById(R.id.phone_field);
                phonefield.setText(number);
                TextView nameText = (TextView) findViewById(R.id.name_textView);
                nameText.setText(name);
            }
        }
    }
}
