package com.example.mikey.database.UserProfile.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Password.NewPassword;
import com.example.mikey.database.UserProfile.registerUser;

public class EditUserProfile extends AppCompatActivity {

    private EditText editFirstName, editLastName, editSecretAnswer;
    private Spinner spinnerSecretQuestion;
    private TextView textBirthday;
    private Button btnChangePassword, btnSaveChanges;
    private registerUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        basicSetup();
    }

    private void basicSetup() {
        editFirstName = (EditText)findViewById(R.id.txtProfileFirstName);
        editLastName = (EditText)findViewById(R.id.txtProfileLastName);
        editSecretAnswer = (EditText)findViewById(R.id.txtProfileSecretAnswer);
        textBirthday = (TextView)findViewById(R.id.txtProfileBirthday);

        btnChangePassword = (Button)findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserProfile.this, NewPassword.class);
                startActivity(intent);
                //TODO look into finishing this activity
            }
        });

        btnSaveChanges = (Button)findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Mike put method to save changes here

                //this closes the activity
                finish();

            }
        });

    }



}
