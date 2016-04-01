package com.parse.weaver;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Activity that displays the settings screen.
 */
public class SettingsActivity extends AppCompatActivity
{
    private EditText fullnameEditText;
    private EditText documentEditText;
    private EditText emailEditText;
    private EditText cellphoneEditText;
    private EditText brandEditText;
    private EditText lineEditText;
    private EditText modelEditText;
    private EditText capacityEditText;

    private Boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        final ParseUser user = ParseUser.getCurrentUser();
        isEditing = false;

        fullnameEditText = (EditText) findViewById(R.id.fullname_edit_text);
        documentEditText = (EditText) findViewById(R.id.document_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        cellphoneEditText = (EditText) findViewById(R.id.cellphone_edit_text);
        brandEditText = (EditText) findViewById(R.id.veh_bran_edit_text);
        lineEditText = (EditText) findViewById(R.id.veh_line_edit_text);
        modelEditText = (EditText) findViewById(R.id.veh_model_edit_text);
        capacityEditText = (EditText) findViewById(R.id.veh_capacity_edit_text);

        fullnameEditText.setText(user.getString("fullname"));
        documentEditText.setText(user.getString("document"));
        emailEditText.setText(user.getEmail());
        cellphoneEditText.setText(user.getString("cellphone"));
        brandEditText.setText(user.getString("veh_brand"));
        lineEditText.setText(user.getString("veh_line"));
        modelEditText.setText(String.valueOf(user.getInt("veh_model")));
        capacityEditText.setText(String.valueOf(user.getInt("veh_cap")));

        // Set up the edit/save button click handler
        final Button editSaveButton = (Button) findViewById(R.id.edit_save_button);
        editSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!isEditing)
                {
                    fullnameEditText.setEnabled(true);
                    documentEditText.setEnabled(true);
                    emailEditText.setEnabled(true);
                    cellphoneEditText.setEnabled(true);
                    brandEditText.setEnabled(true);
                    lineEditText.setEnabled(true);
                    modelEditText.setEnabled(true);
                    capacityEditText.setEnabled(true);

                    editSaveButton.setText(getString(R.string.save));

                    isEditing = true;
                }
                else
                {
                    String fullname = fullnameEditText.getText().toString().trim();
                    String document = documentEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();
                    String cellphone = cellphoneEditText.getText().toString().trim();
                    String brand = brandEditText.getText().toString().trim();
                    String line = lineEditText.getText().toString().trim();
                    String model = modelEditText.getText().toString().trim();
                    String capacity = capacityEditText.getText().toString().trim();

                    boolean validationError = false;
                    StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

                    if (fullname.length() == 0)
                    {
                        validationError = true;
                        validationErrorMessage.append(getString(R.string.error_blank_fullname));
                    }

                    if (document.length() == 0)
                    {
                        validationError = true;
                        validationErrorMessage.append(getString(R.string.error_blank_document));
                    }

                    /*
                    * Email must not be mandatory in this moment
                    *
                    if (email.length() == 0)

                    {
                        validationError = true;
                        validationErrorMessage.append(getString(R.string.error_blank_email));
                    }
                    */

                    if (cellphone.length() == 0)
                    {
                        validationError = true;
                        validationErrorMessage.append(getString(R.string.error_blank_cellphone));
                    }

                    if (brand.length() == 0)
                    {
                        validationError = true;
                        validationErrorMessage.append(getString(R.string.error_blank_brand));
                    }

                    if (line.length() == 0)
                    {
                        validationError = true;
                        validationErrorMessage.append(getString(R.string.error_blank_line));
                    }

                    if (model.length() == 0)
                    {
                        validationError = true;
                        validationErrorMessage.append(getString(R.string.error_blank_model));
                    }

                    if (capacity.length() == 0)
                    {
                        validationError = true;
                        validationErrorMessage.append(getString(R.string.error_blank_capacity));
                    }


                    validationErrorMessage.append(getString(R.string.error_end));

                    if (validationError) {
                        Toast.makeText(SettingsActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                                .show();
                        return;
                    }

                    // Set up and start a progress dialog
                    final ProgressDialog dialog = new ProgressDialog(SettingsActivity.this);
                    dialog.setMessage(getString(R.string.progress_signup));
                    dialog.show();

                    // Editing Parse user
                    user.put("fullname",fullname);
                    user.put("document", document);
                    user.setEmail(email);
                    user.put("cellphone", cellphone);
                    user.put("veh_brand", brand);
                    user.put("veh_line", line);
                    user.put("veh_model", Integer.parseInt(model));
                    user.put("veh_cap", Integer.parseInt(capacity));

                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            // Dismiss the dialog
                            dialog.dismiss();

                            if (e != null) {
                                // Show the error message
                                Toast.makeText(SettingsActivity.this, e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } else {

                                fullnameEditText.setEnabled(false);
                                documentEditText.setEnabled(false);
                                emailEditText.setEnabled(false);
                                cellphoneEditText.setEnabled(false);
                                brandEditText.setEnabled(false);
                                lineEditText.setEnabled(false);
                                modelEditText.setEnabled(false);
                                capacityEditText.setEnabled(false);

                                editSaveButton.setText(getString(R.string.edit));
                            }

                        }
                    });
                }


            }
        });

        // Set up the log out button click handler
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {


                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("¿Realmente quieres cerrar sesión en Relevos App?")
                        .setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        this.finish();
        return true;
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
                    // Call the Parse log out method
                    ParseUser.logOut();
                    // Start and intent for the dispatch activity
                    Intent intent = new Intent(SettingsActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
