/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.weaver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.parse.weaver.classes.SharedPreferenceFreeTime;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Layout buttons
        Button needTakeover = (Button) findViewById(R.id.needTakeover);
        final Button canTakeover = (Button) findViewById(R.id.canTakeover);
        // Layout logo images
        ImageView waLogo = (ImageView) findViewById(R.id.imageWA);
        ImageView phoneLogo = (ImageView) findViewById(R.id.imagePhone);

        Button info = (Button) findViewById(R.id.info);

        //TextView state = (TextView) findViewById(R.id.textViewState);

        /*
         * Commented temporally
         *
        if(ParseUser.getCurrentUser().get("available") == "true")
        {
            state.setTextColor(Color.rgb(51, 180, 102));
            state.setText(getString(R.string.label_available));
        }
        else
        {
            state.setTextColor(Color.RED);
            state.setText(getString(R.string.label_not_available));
        }
        */

        needTakeover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NeedTakeoverActivity.class));
            }

        });

        canTakeover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FreeTimeActivity.class));
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
            }
        });

        waLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set up the log out button click handler
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("¿Quieres comunicarte vía Whatsapp?")
                        .setPositiveButton("Si", dialogClickListenerWA)
                        .setNegativeButton("No", dialogClickListenerWA).show();

            }
        });

        phoneLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set up the log out button click handler
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("¿Quieres comunicarte vía telefónica?")
                        .setPositiveButton("Si", dialogClickListenerPhone)
                        .setNegativeButton("No", dialogClickListenerPhone).show();
            }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
        });
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

    // Other methods

    DialogInterface.OnClickListener dialogClickListenerWA = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case DialogInterface.BUTTON_POSITIVE:

                    // Launch Whatsapp with the chat

                    PackageManager pm=getPackageManager();

                    try
                    {
                        PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

                        Uri mUri = Uri.parse("smsto:+3192920369");
                        Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                        mIntent.setPackage("com.whatsapp");
                        mIntent.putExtra("sms_body", "¡Hola Relevos App!, necesito un relevo");
                        mIntent.putExtra("chat", true);
                        startActivity(mIntent);

                    }
                    catch (PackageManager.NameNotFoundException e)
                    {
                        Toast.makeText(MainActivity.this, "WhatsApp no está instalado en tu equipo", Toast.LENGTH_SHORT)
                                .show();
                    }

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Starts an intent of the log in activity
                    dialog.dismiss();
                    break;
            }
        }
    };

    DialogInterface.OnClickListener dialogClickListenerPhone = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case DialogInterface.BUTTON_POSITIVE:

                    // Launch phone with number
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:3192920369"));
                    startActivity(intent);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Starts an intent of the log in activity
                    dialog.dismiss();
                    break;
            }
        }
    };

}
