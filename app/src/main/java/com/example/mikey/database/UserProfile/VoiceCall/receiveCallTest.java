package com.example.mikey.database.UserProfile.VoiceCall;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Home;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zapatacajas on 16/03/2016.
 */
public class receiveCallTest extends AppCompatActivity {
  DatabaseUsernameId dbHandlerId;
    HashMap<String,String> idUserHash;
   ImageButton accept, decline;
    AudioPlayer ap;//holds the audio player


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
        setContentView(R.layout.receivecalls_layout);
        dbHandlerId = new DatabaseUsernameId(this);
        idUserHash = dbHandlerId.getUserDetails();

        ap = new AudioPlayer(this);

        accept = (ImageButton) findViewById(R.id.answer_call);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Home.call.answer();
                Home.call.addCallListener(new SinchCallListener());


            } });

        decline = (ImageButton) findViewById(R.id.end_incoming_call);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Home.call.hangup();
                Home.call.addCallListener(new SinchCallListener() {
                });

                    //Toast.makeText(ContactProfile.this,"Call ended by you." , Toast.LENGTH_LONG).show();


                }
        });



        }
/*
    public class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            //Pick up the call!

         //   ap.playRingtone();
          //  notification();
        //    Toast.makeText(Home.this, "receiving call", Toast.LENGTH_LONG).show();


            Home.callcall = incomingCall;
            call.addCallListener(new SinchCallListener());

        }
    }
  */  class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            //  Toast.makeText(ContactProfile.this, "Call ended by your friend." + "SHOULD I PUT USER'S NAME?.", Toast.LENGTH_LONG).show();
            Home.call.hangup();
            Intent h = new Intent(receiveCallTest.this, Home.class);
            finish();
            startActivity(h);
            finish();
            ap.stopRingtone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            ap.stopRingtone();

            //   Toast.makeText(ContactProfile.this,"Conected" , Toast.LENGTH_LONG).show();

        }
        @Override
        public void onCallProgressing(Call progressingCall) {
            //call is ringing
        }
        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }

    }




