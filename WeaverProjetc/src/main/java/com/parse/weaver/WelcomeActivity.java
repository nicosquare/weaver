package com.parse.weaver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Activity which displays a registration screen to the user.
 */
public class WelcomeActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Check if GPS is Enabled
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        // Log in button click handler
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {

                if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                    builder.setMessage("El GPS esta apagado, Weaver funcionara mejor si lo activas. ¿Deseas activarlo?")
                            .setPositiveButton("Si", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
                else
                {
                    // Starts an intent of the log in activity
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }

            }
        });

        // Sign up button click handler
        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                // Starts an intent for the sign up activity
                startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
            }
        });

        // Mail button click handler
        FloatingActionButton mailButton = (FloatingActionButton) findViewById(R.id.fab);
        mailButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                /* Create the Intent */
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"contacto@weavercol.com"});

                /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case DialogInterface.BUTTON_POSITIVE:
                    // Show the error message
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Starts an intent of the log in activity
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    break;
            }
        }
    };
}
