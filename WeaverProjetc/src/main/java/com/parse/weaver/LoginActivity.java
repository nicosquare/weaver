package com.parse.weaver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
public class LoginActivity extends Activity
{
    // UI references.
    private EditText loginElementEditText;
    private EditText passwordEditText;
    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        loginPrefs = this.getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_login);

        final FloatingActionButton mailButton = (FloatingActionButton) findViewById(R.id.fab);
        Button actionButton = (Button) findViewById(R.id.action_button);
        Button passResetButton = (Button) findViewById(R.id.forgotten_pass_button);

        // Set up the login form.
        loginElementEditText = (EditText) findViewById(R.id.login_element);
        passwordEditText = (EditText) findViewById(R.id.password);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == R.id.edittext_action_login || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
                {
                    login();
                    return true;
                }
                return false;
            }
        });

        // Set up the submit button click handler
        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                login();
            }
        });

        // Set up the submit button click handler
        passResetButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("¿Olvidaste tu contraseña?")
                        .setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        // Mail button click handler
        mailButton.setOnClickListener(new View.OnClickListener() {
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

        loginElementEditText.setText(loginPrefs.getString("loginElement", ""));
        passwordEditText.setText(loginPrefs.getString("password", ""));

    }

    private void login()
    {
        final String loginElement = loginElementEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        String column = "";

        // Validate the log in data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

        if (loginElement.length() == 0)
        {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_email));
        }

        if (password.length() == 0)
        {
            if (validationError)
            {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getString(R.string.progress_login));
        dialog.show();

        // Get username by email
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereEqualTo("email", loginElement);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                if (object == null) {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();

                    query.whereEqualTo("cellphone", loginElement);
                    query.getFirstInBackground(new GetCallback<ParseUser>() {
                        public void done(ParseUser object, ParseException e) {
                            if (object == null) {
                                ParseQuery<ParseUser> query = ParseUser.getQuery();

                                query.whereEqualTo("document", loginElement);
                                query.getFirstInBackground(new GetCallback<ParseUser>() {
                                    public void done(ParseUser object, ParseException e) {
                                        if (object == null) {
                                            dialog.dismiss();
                                            Toast.makeText(LoginActivity.this, getString(R.string.error_wrong_loginel), Toast.LENGTH_LONG).show();
                                        } else {
                                            String username = object.get("username").toString();

                                            ParseUser.logInInBackground(username, password, new LogInCallback() {
                                                @Override
                                                public void done(ParseUser user, ParseException e) {
                                                    dialog.dismiss();
                                                    if (e != null) {
                                                        // Show the error message
                                                        Toast.makeText(LoginActivity.this, getString(R.string.error_wrong_login), Toast.LENGTH_LONG).show();
                                                    } else {
                                                        loginPrefsEditor = loginPrefs.edit();

                                                        loginPrefsEditor.putString("loginElement", loginElement);
                                                        loginPrefsEditor.putString("password", password);
                                                        loginPrefsEditor.commit();

                                                        // Start an intent for the dispatch activity
                                                        Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            } else {
                                String username = object.get("username").toString();

                                ParseUser.logInInBackground(username, password, new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        dialog.dismiss();
                                        if (e != null) {
                                            // Show the error message
                                            Toast.makeText(LoginActivity.this, getString(R.string.error_wrong_login), Toast.LENGTH_LONG).show();
                                        } else {
                                            loginPrefsEditor = loginPrefs.edit();

                                            loginPrefsEditor.putString("loginElement", loginElement);
                                            loginPrefsEditor.putString("password", password);
                                            loginPrefsEditor.commit();

                                            // Start an intent for the dispatch activity
                                            Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }
                    });

                } else {
                    String username = object.get("username").toString();

                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            dialog.dismiss();
                            if (e != null) {
                                // Show the error message
                                Toast.makeText(LoginActivity.this, getString(R.string.error_wrong_login), Toast.LENGTH_LONG).show();
                            } else {
                                loginPrefsEditor = loginPrefs.edit();

                                loginPrefsEditor.putString("loginElement", loginElement);
                                loginPrefsEditor.putString("password", password);
                                loginPrefsEditor.commit();

                                // Start an intent for the dispatch activity
                                Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }
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
                    startActivity(new Intent(LoginActivity.this, PasswordForgottenActivity.class));
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    startActivity(new Intent(LoginActivity.this, DispatchActivity.class));
                    break;
            }
        }
    };

}