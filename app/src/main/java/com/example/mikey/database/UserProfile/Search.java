package com.example.mikey.database.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.mikey.database.R;

public class Search extends AppCompatActivity {

    private ImageButton searchInfo,searchNear;

    @Override
    public void onBackPressed() {

        Intent setIntent = new Intent(Intent.ACTION_MAIN);

        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);


        searchNear = (ImageButton) findViewById(R.id.search_map);
        searchNear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent display = new Intent(getApplicationContext(), NearPeople.class);
                startActivity(display);
            }
        });


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


