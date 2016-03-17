package com.example.mikey.database.UserProfile;

import android.app.TabActivity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Profile.Profile;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.HashMap;
import java.util.List;


public class Home extends TabActivity{
    private static final String APP_KEY = "ef1889da-3241-4700-a675-200b6b15fecd";
    private static final String APP_SECRET = "fsNJI+ziSkG1d+rDsW84wA==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    DatabaseUsernameId dbHandlerId;
    HashMap<String, String> idUserHash;
    ImageButton accept, decline;
    TabHost tablay;
    LinearLayout sec;
    private Call call;

/*ReceiveCallHolder mike;*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tablay = (TabHost)findViewById(android.R.id.tabhost);
        tablay.setVisibility(View.VISIBLE);
sec = (LinearLayout) findViewById(R.id.call_window);
      /*  mike = new ReceiveCallHolder(this);
        mike.startMike(this);*/


      dbHandlerId = new DatabaseUsernameId(this);

        idUserHash = dbHandlerId.getUserDetails();


        SinchClient sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(idUserHash.get("email"))
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();
        sinchClient.setSupportCalling(true);

       sinchClient.startListeningOnActiveConnection();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        sinchClient.start();




        accept = (ImageButton) findViewById(R.id.answer_call);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                call.answer();
                call.addCallListener(new SinchCallListener());


            } });

        decline = (ImageButton) findViewById(R.id.end_incoming_call);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call.hangup();
                call.addCallListener(new SinchCallListener() {
                });

                tablay.setVisibility(View.VISIBLE);
                sec.setVisibility(View.INVISIBLE);

                //Toast.makeText(ContactProfile.this,"Call ended by you." , Toast.LENGTH_LONG).show();


            }
        });





        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("Contacts");
        tab1.setContent(new Intent(this, Profile.class));

        tab2.setIndicator("Search");
        tab2.setContent(new Intent(this, Search.class));

        tab3.setIndicator("Settings");
        tab3.setContent(new Intent(this, UserSettings.class));

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);


    }

public class SinchCallClientListener implements CallClientListener {
    @Override
    public void onIncomingCall(CallClient callClient, Call incomingCall) {
        //Pick up the call!
        tablay.setVisibility(View.INVISIBLE);
        sec.setVisibility(View.VISIBLE);
       // Home.this.setContentView(R.layout.receivecalls_layout);
        /*
      Intent inCall = new Intent(Home.this,receiveCallTest.class);
        startActivity(inCall);
*/
        Toast.makeText(Home.this, "receiving call", Toast.LENGTH_LONG).show();

        call = incomingCall;
        call.addCallListener(new SinchCallListener());
     /*  call.answer();

*/
    }
}
    class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
          //  Toast.makeText(ContactProfile.this, "Call ended by your friend." + "SHOULD I PUT USER'S NAME?.", Toast.LENGTH_LONG).show();
           call.hangup();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
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




