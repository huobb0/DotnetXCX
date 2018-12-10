package com.example.zsombor.dotnetxcx;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

        private static final int PERMISSIONS_REQUEST_RECEIVE_SMS = 0;

        private void requestPermissions(String permission, int requestCode) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(this, "Granting permission is necessary!", Toast.LENGTH_LONG).show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{permission},
                            requestCode);

                    // requestCode is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Request the permission immediately here for the first time run
        requestPermissions(Manifest.permission.RECEIVE_SMS, PERMISSIONS_REQUEST_RECEIVE_SMS);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

                if (cursor.moveToFirst()) { // must check the result to prevent exception
                    do {
                        String msgData = "";
                        for(int idx=0;idx<cursor.getColumnCount();idx++)
                        {
                            msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                        }
                        Log.d("XMX",msgData);
                    } while (cursor.moveToNext());
                } else {
                    // empty box, no SMS
                    Log.d("XMX","No SMS - yet.");
                }
            }
        });
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
}
