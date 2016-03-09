package com.parse.weaver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LocationCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.utilities.GMailSenderAsync;

import java.text.DateFormat;
import java.util.TimeZone;

public class NeedTakeoverActivity extends AppCompatActivity
{

    EditText takeoverOrigin;
    EditText takeoverGoal;
    EditText takeoverTime;
    EditText takeoverDetail;

    DateFormat df = DateFormat.getTimeInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_takeover);

        // Set Timezone
        df.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        // Set up the login form.
        takeoverOrigin = (EditText) findViewById(R.id.originDetail);
        takeoverGoal = (EditText) findViewById(R.id.goalDetail);
        takeoverTime = (EditText) findViewById(R.id.timeStimate);
        takeoverDetail = (EditText) findViewById(R.id.takeoverDetail);

        // Set up the submit button click handler
        final Button actionButton = (Button) findViewById(R.id.askTakeover);
        actionButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                putTakeoverRequest();
            }

        });

        // Set up the submit button click handler
        ImageView imageOrigin = (ImageView) findViewById(R.id.imageOrigin);
        imageOrigin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                takeoverOrigin.requestFocus();
                imm.showSoftInput(takeoverOrigin, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // Set up the submit button click handler
        ImageView imageGoal = (ImageView) findViewById(R.id.imageGoal);
        imageGoal.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                takeoverGoal.requestFocus();
                imm.showSoftInput(takeoverGoal, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // Set up the submit button click handler
        ImageView imageTime = (ImageView) findViewById(R.id.imageTime);
        imageTime.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                takeoverTime.requestFocus();
                imm.showSoftInput(takeoverTime, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // Set up the submit button click handler
        ImageView imageDetail = (ImageView) findViewById(R.id.imageVan);
        imageDetail.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                takeoverDetail.requestFocus();
                imm.showSoftInput(takeoverDetail, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // Set up the submit button click handler
        ImageView imageTakeover = (ImageView) findViewById(R.id.imageTakeover);
        imageTakeover.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                putTakeoverRequest();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_need_takeover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        this.finish();
        return true;
    }

    private void putTakeoverRequest()
    {
        final String origin = takeoverOrigin.getText().toString();
        final String goal = takeoverGoal.getText().toString();
        final String time = takeoverTime.getText().toString();
        final String detail = takeoverDetail.getText().toString();

        // Validate the log in data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

        if (origin.length() == 0)
        {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_origin));
        }

        if (goal.length() == 0)
        {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_goal));
        }

        /*
        *   Detail is not mandatory
        *

        if (time.length() == 0)
        {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_time));
        }

        if (detail.length() == 0)
        {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_TO_detail));
        }
        */

        validationErrorMessage.append(getString(R.string.error_end));

        // If there is a validation error, display the error
        if (validationError)
        {
            Toast.makeText(NeedTakeoverActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(NeedTakeoverActivity.this);
        dialog.setMessage(getString(R.string.progress_requesting));
        dialog.show();



        //Set marker here
        ParseGeoPoint currentLocation = new ParseGeoPoint();

        final ParseObject takeoverRequestObject = new ParseObject("TORequests");
        takeoverRequestObject.put("username", ParseUser.getCurrentUser());
        takeoverRequestObject.put("origin", origin);
        takeoverRequestObject.put("destination", goal);
        takeoverRequestObject.put("time_estimation", time);
        takeoverRequestObject.put("comments", detail);

        takeoverRequestObject.put("request_date", df.getCalendar().getTime());

        currentLocation.getCurrentLocationInBackground(30000, new LocationCallback() {
            @Override
            public void done(ParseGeoPoint geoPoint, ParseException e) {
                if (geoPoint != null) {

                    takeoverRequestObject.put("loc_lat", String.valueOf(geoPoint.getLatitude()));
                    takeoverRequestObject.put("loc_lng", String.valueOf(geoPoint.getLongitude()));

                    takeoverRequestObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                dialog.dismiss();
                                Toast.makeText(NeedTakeoverActivity.this, getString(R.string.label_petition_stored), Toast.LENGTH_LONG).show();

                                String userName = ParseUser.getCurrentUser().get("fullname").toString();

                                String messageBody = "";
                                messageBody =   "Nueva solicitud para usuario: " + ParseUser.getCurrentUser().get("fullname").toString() +"\n"+
                                                "Hora de solicitud: " + df.getCalendar().getTime().toString() +"\n"+
                                                "Celular: " + ParseUser.getCurrentUser().get("cellphone").toString() +"\n"+
                                                "Tiempo estimado: " + takeoverTime.getText() +"\n"+
                                                "Indicación :" + takeoverDetail.getText();

                                GMailSenderAsync asyncMailSenderNicolas = new GMailSenderAsync();
                                GMailSenderAsync asyncMailSenderWilmar = new GMailSenderAsync();
                                GMailSenderAsync asyncMailSenderDavid = new GMailSenderAsync();
                                GMailSenderAsync asyncMailSenderWeaver = new GMailSenderAsync();

                                asyncMailSenderNicolas.execute(userName, messageBody, "nicolas@weavercol.com");
                                asyncMailSenderDavid.execute(userName, messageBody, "david@weavercol.com");
                                asyncMailSenderWilmar.execute(userName, messageBody, "wilmar@weavercol.com");
                                asyncMailSenderWeaver.execute(userName, messageBody, "contacto.weaver@gmail.com");

                                // Start and intent
                                startActivity(new Intent(NeedTakeoverActivity.this, MainActivity.class));
                            } else {
                                // Show the error message
                                Toast.makeText(NeedTakeoverActivity.this, getString(R.string.error_wrong_petition), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        public void done(com.parse.ParseException e) {
                            // TODO Auto-generated method stub
                            if (e != null) {
                                Log.v("Loc Updated!", "True");
                            } else {
                                Log.v("Loc Updated!", "False");
                            }
                        }
                    });

                } else {
                    Log.v("Loc. wrong", e.getMessage());
                }

            }
        });

    }

}
