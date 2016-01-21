package com.parse.weaver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NeedTakeoverActivity extends AppCompatActivity
{

    EditText takeoverDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_takeover);

        // Set up the login form.
        takeoverDetail = (EditText) findViewById(R.id.takeoverDetail);

        // Set up the submit button click handler
        Button actionButton = (Button) findViewById(R.id.askTakeover);

        actionButton.setOnClickListener(new View.OnClickListener()
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
        String detail = takeoverDetail.getText().toString();

        // Validate the log in data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

        if (detail.length() == 0)
        {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_detail));
        }

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

        ParseObject takeoverRequestObject = new ParseObject("TakeoverRequests");
        takeoverRequestObject.put("username", ParseUser.getCurrentUser().getUsername());
        takeoverRequestObject.put("description",detail);
        takeoverRequestObject.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    dialog.dismiss();
                    Toast.makeText(NeedTakeoverActivity.this, getString(R.string.label_petition_stored), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(NeedTakeoverActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else
                {
                    // Show the error message
                    Toast.makeText(NeedTakeoverActivity.this, getString(R.string.error_wrong_petition), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
