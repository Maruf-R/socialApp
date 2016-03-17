package com.example.mikey.database.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.mikey.database.R;

public class Search extends AppCompatActivity {

    private ImageButton searchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);


        searchInfo = (ImageButton) findViewById(R.id.search_info);
        searchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoSearch = new Intent(getApplicationContext(), Spec.class);
                startActivity(infoSearch);


            }


        });
    }
}


