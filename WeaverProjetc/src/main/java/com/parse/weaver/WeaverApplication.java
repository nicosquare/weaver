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
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

import com.parse.LocationCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;


public class WeaverApplication extends Application
{

    // Handler for getting location
    Handler locationHandler = new Handler();

    @Override
    public void onCreate()
    {
        super.onCreate();

        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buildAlertMessageNoGps();
        }

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "7ropjlhhnO4J9PgSpYjCGWvpr4jgjQsinptycJdX", "KUYF34tj0Mj25ptjbGPeEDIcyUH2r0ewkckimqJE");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        // Fire Handler for location
        locationHandler.postDelayed(updateLocationThread, 0);
    }

    private void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.error_no_gps)).
                setCancelable(false).
                setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                    dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    private Runnable updateLocationThread = new Runnable()
    {

        public void run()
        {
            Log.v("Location","Update Location enter");

            //Set marker here
            ParseGeoPoint currentLocation = new ParseGeoPoint();

            currentLocation.getCurrentLocationInBackground(30000, new LocationCallback()
            {
                @Override
                public void done(ParseGeoPoint geoPoint, ParseException e)
                {
                    if(geoPoint != null)
                    {
                        Log.v("Location", "Update Location");

                        ParseUser.getCurrentUser().put("loc_lat", String.valueOf(geoPoint.getLongitude()));
                        ParseUser.getCurrentUser().put("loc_lng", String.valueOf(geoPoint.getLongitude()));

                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback()
                        {
                            public void done(com.parse.ParseException e)
                            {
                                // TODO Auto-generated method stub
                                if (e != null)
                                {
                                    Log.v("Loc Updated!","True");
                                } else
                                {
                                    Log.v("Loc Updated!","False");
                                }
                            }
                        });

                    }
                    else
                    {
                        Log.v("Loc. wrong",e.getMessage());
                    }

                }
            });

            // Update location each 5 minutes
            locationHandler.postDelayed(this, 1000*60*5);
        }
    };

}
