package com.example.mikey.database.UserProfile.VoiceCall;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Profile.ContactProfile;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

/**
 * Created by zapatacajas on 24/03/2016.
 */
public class PlaceCallTest extends AppCompatActivity{
    ImageButton  endCall;
    TextView actioncalling, usercallingto;
    AudioPlayer ap;
    ImageView avatarcall;
    private String nameFriend;

    private String avatarimg;


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
        setContentView(R.layout.call_window);
        Intent intent = getIntent();
        String namef = intent.getStringExtra("callingto");
        String ava = intent.getStringExtra("avatar");

        ap = new AudioPlayer(this);
        usercallingto= (TextView) findViewById(R.id.namepersoncallingto);
        endCall = (ImageButton) findViewById(R.id.end_call);
        actioncalling = (TextView) findViewById(R.id.calltextid);
        actioncalling.setText("Calling to.....");
        avatarcall= (ImageView) findViewById(R.id.imgUserAvatarCallingProfile);
        usercallingto.setText(namef);

        int imgId = getResources().getIdentifier(ava, "drawable", getPackageName());
        avatarcall.setImageResource(imgId);




        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actioncalling.setText("Call ended");

                ContactProfile.call.hangup();

                ContactProfile.call.addCallListener(ContactProfile.callListener);

                Intent h = new Intent(PlaceCallTest.this, ContactProfile.class);
                startActivity(h);
                finish();


                Toast.makeText(PlaceCallTest.this, "Call ended by you.", Toast.LENGTH_LONG).show();

            }
        });





    }
    class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            ap.stopProgressTone();

              Toast.makeText(PlaceCallTest.this,"Call ended by your friend." , Toast.LENGTH_LONG).show();

            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }
        @Override
        public void onCallEstablished(Call establishedCall) {
            ap.stopProgressTone();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            Toast.makeText(PlaceCallTest.this,"Conected" , Toast.LENGTH_LONG).show();
            ap.stopProgressTone();


        }
        @Override
        public void onCallProgressing(Call progressingCall) {
            //call is ringing
            actioncalling.setText("Calling...");

            ap.playProgressTone();
        }
        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }




}
