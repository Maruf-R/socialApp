package com.example.mikey.database.UserProfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Profile.EditUserProfile;

public class UserSettings extends AppCompatActivity {

    private Button btnEditUserProfile;
    private Button btnEditUserInterests;
    private Button btnBlockContact;
    private Button btnLogOut;
    private Button btnDeleteAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        // Starting EditUserProfile activity
        btnEditUserProfile = (Button)findViewById(R.id.btnEditProfile);
        btnEditUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettings.this, EditUserProfile.class);
                startActivity(intent);
            }
        });

        //Start UserInterests activity
        btnEditUserInterests = (Button)findViewById(R.id.btnEditUserInterests);
        btnEditUserInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettings.this, UserInterests.class);
                startActivity(intent);
            }
        });

        //start activity which will show blocked contacts
        btnBlockContact = (Button)findViewById(R.id.btnBlockContact);
        btnBlockContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //start activity which will show blocked contacts
        btnBlockContact = (Button)findViewById(R.id.btnBlockContact);
        btnBlockContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO intent for blocked activity

            }
        });

        // log out of the profile and redirect to log in screen
        btnLogOut = (Button)findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO log out of profile and redirect to login screen
                Intent intent = new Intent(UserSettings.this, Login.class);
                startActivity(intent);
                finish();

            }
        });

        //
        btnDeleteAccount = (Button)findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO ask the user to enter their password, confirm they want to delete account,
                // TODO then use the appropriate SQL command to delete and redirecet to login activity.
                Intent intent = new Intent(UserSettings.this, Login.class);
                startActivity(intent);
                finish();


            }
        });

//        //TODO delete this method
//        // testing if stuff is stored
//        final Button test = (Button)findViewById(R.id.btnTest);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserSettings.this, testActivity.class);
//                startActivity(intent);
//            }
//        });

    }


}
