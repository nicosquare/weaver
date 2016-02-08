package com.parse.weaver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.utilities.GMailSenderAsync;

public class PasswordForgottenActivity extends AppCompatActivity
{
    EditText forgottenUserElement;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgotten);

        forgottenUserElement = (EditText) findViewById(R.id.for_user_element);

        // Set up the submit button click handler
        Button passResetButton = (Button) findViewById(R.id.for_user_button);
        passResetButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                resetPassword();
            }
        });

    }

    private void resetPassword()
    {
        final String forgottenUser = forgottenUserElement.getText().toString().trim();

        // Validate the log in data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

        if (forgottenUser.length() == 0)
        {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_email_cellphone_document));
        }

        validationErrorMessage.append(getString(R.string.error_end));

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(PasswordForgottenActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(PasswordForgottenActivity.this);
        dialog.setMessage(getString(R.string.progress_login));
        dialog.show();

        // Get username by email
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereEqualTo("email", forgottenUser);
        query.getFirstInBackground(new GetCallback<ParseUser>()
        {
            public void done(ParseUser object, ParseException e)
            {
                if (object == null)
                {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();

                    query.whereEqualTo("cellphone", forgottenUser);
                    query.getFirstInBackground(new GetCallback<ParseUser>()
                    {
                        public void done(ParseUser object, ParseException e)
                        {
                            if (object == null)
                            {
                                ParseQuery<ParseUser> query = ParseUser.getQuery();

                                query.whereEqualTo("document", forgottenUser);
                                query.getFirstInBackground(new GetCallback<ParseUser>()
                                {
                                    public void done(ParseUser object, ParseException e)
                                    {
                                        if (object == null)
                                        {
                                            dialog.dismiss();
                                            Toast.makeText(PasswordForgottenActivity.this, getString(R.string.error_wrong_loginel), Toast.LENGTH_LONG).show();
                                        } else
                                        {
                                            dialog.dismiss();
                                            String email = object.getEmail();
                                            String username = object.get("username").toString();
                                            GMailSenderAsync asyncMailSender = new GMailSenderAsync();

                                            asyncMailSender.execute(username, email, "contacto.weaver@gmail.com");

                                            // Notification
                                            Toast.makeText(PasswordForgottenActivity.this, getString(R.string.alert_pass_recovery), Toast.LENGTH_LONG).show();
                                            // Start an intent for the dispatch activity
                                            startActivity(new Intent(PasswordForgottenActivity.this, DispatchActivity.class));
                                        }
                                    }
                                });

                            }
                            else
                            {
                                dialog.dismiss();
                                String email = object.getEmail();
                                String username = object.get("username").toString();
                                GMailSenderAsync asyncMailSender = new GMailSenderAsync();

                                asyncMailSender.execute(username, email, "contacto.weaver@gmail.com");

                                // Notification
                                Toast.makeText(PasswordForgottenActivity.this, getString(R.string.alert_pass_recovery), Toast.LENGTH_LONG).show();
                                // Start an intent for the dispatch activity
                                startActivity(new Intent(PasswordForgottenActivity.this, DispatchActivity.class));

                            }
                        }
                    });

                }
                else
                {
                    dialog.dismiss();
                    String email = object.getEmail();
                    String username = object.get("username").toString();
                    GMailSenderAsync asyncMailSender = new GMailSenderAsync();

                    asyncMailSender.execute(username, email, "contacto.weaver@gmail.com");

                    // Notification
                    Toast.makeText(PasswordForgottenActivity.this, getString(R.string.alert_pass_recovery), Toast.LENGTH_LONG).show();
                    // Start an intent for the dispatch activity
                    startActivity(new Intent(PasswordForgottenActivity.this, DispatchActivity.class));
                }
            }
        });

    }

}
