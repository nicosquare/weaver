package com.parse.weaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.parse.LocationCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Activity which starts an intent for either the logged in (MainActivity) or logged out
 * (SignUpOrLoginActivity) activity.
 */
public class DispatchActivity extends Activity
{

    // Handler for getting location
    Handler locationHandler = new Handler();

    public DispatchActivity()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Check if GPS is Enabled
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            // Show the error message
            Toast.makeText(DispatchActivity.this, getString(R.string.error_no_gps), Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        // Check if there is current user info
        if (ParseUser.getCurrentUser().getUsername() != null)
        {
            // Fire Handler for location
            locationHandler.postDelayed(updateLocationThread, 0);
            // Start an intent for the logged in activity
            startActivity(new Intent(this, MainActivity.class));
        }
        else
        {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }

    private Runnable updateLocationThread = new Runnable()
    {

        public void run()
        {

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
                        ParseUser.getCurrentUser().put                                                                                                  ("loc_lng", String.valueOf(geoPoint.getLongitude()));

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
