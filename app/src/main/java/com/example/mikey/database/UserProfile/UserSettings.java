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

    }


}
