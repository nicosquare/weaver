/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.weaver;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button needTakeover = (Button) findViewById(R.id.needTakeover);
    final Button canTakeover = (Button) findViewById(R.id.canTakeover);
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

    needTakeover.setOnClickListener(new View.OnClickListener()
    {
        public void onClick(View view)
        {
            startActivity(new Intent(MainActivity.this, NeedTakeoverActivity.class));
        }

    });

    canTakeover.setOnClickListener(new View.OnClickListener()
    {
        public void onClick(View view)
        {
            startActivity(new Intent(MainActivity.this, FreeTimeActivity.class));
        }
    });


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);

    menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
    {
      public boolean onMenuItemClick(MenuItem item)
      {
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
}
