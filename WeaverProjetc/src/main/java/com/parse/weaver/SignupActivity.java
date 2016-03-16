package com.parse.weaver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends Activity
{

    private EditText fullnameEditText;
    private EditText documentEditText;
    private EditText emailEditText;
    private EditText cellphoneEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private EditText brandEditText;
    private EditText lineEditText;
    private EditText modelEditText;
    private EditText capacityEditText;

    private CheckBox termsCheckBox;
    private TextView termsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullnameEditText = (EditText) findViewById(R.id.fullname_edit_text);
        documentEditText = (EditText) findViewById(R.id.document_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        cellphoneEditText = (EditText) findViewById(R.id.cellphone_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        passwordAgainEditText = (EditText) findViewById(R.id.password_again_edit_text);
        brandEditText = (EditText) findViewById(R.id.veh_bran_edit_text);
        lineEditText = (EditText) findViewById(R.id.veh_line_edit_text);
        modelEditText = (EditText) findViewById(R.id.veh_model_edit_text);
        capacityEditText = (EditText) findViewById(R.id.veh_capacity_edit_text);

        termsCheckBox = (CheckBox) findViewById(R.id.terms_check_box);
        termsTextView = (TextView) findViewById(R.id.terms_text);

        Button mActionButton = (Button) findViewById(R.id.action_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                signup();
            }
        });

        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, TermsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signup()
    {
        String[] aux;
        String username = "";

        // Build username from Full name and Document
        aux = fullnameEditText.getText().toString().split(" ");
        for(String name : aux)
        {
            username += name.substring(0,1);
        }
        username += documentEditText.getText().toString();

        String fullname = fullnameEditText.getText().toString().trim();
        String document = documentEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String cellphone = cellphoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordAgain = passwordAgainEditText.getText().toString().trim();
        String brand = brandEditText.getText().toString().trim();
        String line = lineEditText.getText().toString().trim();
        String model = modelEditText.getText().toString().trim();
        String capacity = capacityEditText.getText().toString().trim();

        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

        if(!termsCheckBox.isChecked())
        {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_terms));
        }
        else
        {
            if (fullname.length() == 0)
            {
                if (validationError)
                {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
                validationError = true;
                validationErrorMessage.append(getString(R.string.error_blank_fullname));
            }

            if (document.length() == 0)
            {
                if (validationError)
                {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
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
                if (validationError)
                {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
                validationError = true;
                validationErrorMessage.append(getString(R.string.error_blank_cellphone));
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

            if (!password.equals(passwordAgain))
            {
                if (validationError)
                {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
                validationError = true;
                validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
            }

            if (brand.length() == 0)
            {
                if (validationError)
                {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
                validationError = true;
                validationErrorMessage.append(getString(R.string.error_blank_brand));
            }

            if (line.length() == 0)
            {
                if (validationError)
                {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
                validationError = true;
                validationErrorMessage.append(getString(R.string.error_blank_line));
            }

            if (model.length() == 0)
            {
                if (validationError)
                {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
                validationError = true;
                validationErrorMessage.append(getString(R.string.error_blank_model));
            }

            if (capacity.length() == 0)
            {
                if (validationError)
                {
                    validationErrorMessage.append(getString(R.string.error_join));
                }
                validationError = true;
                validationErrorMessage.append(getString(R.string.error_blank_capacity));
            }
        }

        validationErrorMessage.append(getString(R.string.error_end));

        if (validationError) {
            Toast.makeText(SignupActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up and start a progress dialog
        final ProgressDialog dialog = new ProgressDialog(SignupActivity.this);
        dialog.setMessage(getString(R.string.progress_signup));
        dialog.show();

        // Set up a new Parse user
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.put("fullname",fullname);
        user.put("document", document);

        if (email.equals(""))
        {
            user.setEmail(document+"@weavercol.com");
        }
        else user.setEmail(email);

        user.setEmail(email);
        user.put("cellphone",cellphone);
        user.setUsername(username);
        user.setPassword(password);
        user.put("veh_brand", brand);
        user.put("veh_line", line);
        user.put("veh_model", Integer.parseInt(model));
        user.put("veh_cap",Integer.parseInt(capacity));

        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(ParseException e)
            {

                // Dismiss the dialog
                dialog.dismiss();

                if (e != null) {
                    // Show the error message
                    Toast.makeText(SignupActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    Intent intent = new Intent(SignupActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

    }

}
