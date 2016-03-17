package com.example.mikey.database.UserProfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mikey.database.R;

public class testActivity extends AppCompatActivity {

    // TODO DELETE THIS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        UserInterests interests = new UserInterests();
        TextView test = (TextView)findViewById(R.id.testText);
        test.setText("TEST " + interests.getInterests("biography"));
    }
}
