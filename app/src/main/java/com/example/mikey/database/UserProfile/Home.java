package com.example.mikey.database.UserProfile;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Profile.Profile;
import com.example.mikey.database.UserProfile.VoiceCall.AudioPlayer;
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
    AudioPlayer ap;//holds the audio player

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
ap = new AudioPlayer(this);

      dbHandlerId = new DatabaseUsernameId(this);

        idUserHash = dbHandlerId.getUserDetails();

///////////////////////////////////////////////////////////////////////////// this is receiving calls
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
                ap.stopRingtone();

                call.addCallListener(new SinchCallListener() {
                });

                tablay.setVisibility(View.VISIBLE);
                sec.setVisibility(View.INVISIBLE);

                //Toast.makeText(ContactProfile.this,"Call ended by you." , Toast.LENGTH_LONG).show();


            }
        });


///////////////////////////////////////////////////////////////////////////////


        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("FirstTab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("SecondTab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Thirdtab");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("Fourthtab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected



        tab1.setIndicator("Profile");
        tab1.setContent(new Intent(this, Profile.class));


        tab2.setIndicator("Search");
        tab2.setContent(new Intent(this, Search.class));


        tab3.setIndicator("Edit");
        tab3.setContent(new Intent(this, UserSettings.class));

        tab4.setIndicator("Friends");
        tab4.setContent(new Intent(this, Favourites.class));



        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab4);
        tabHost.addTab(tab3);


    }

    public void notification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.end_call)
                .setContentTitle("Companion4Me")
                .setContentText("Incoming Call!")
                .setAutoCancel(true);

        Intent answer = new Intent (this, Home.class);
        PendingIntent pendingIntentAnswer = PendingIntent.getActivity(this, 0, answer, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent decline = new Intent (this, Home.class);
        PendingIntent pendingIntentDecline = PendingIntent.getActivity(this, 0, decline, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.addAction(R.drawable.end_call,"Accept",pendingIntentAnswer);
        mBuilder.addAction(R.drawable.end_call,"Decline",pendingIntentDecline);


        int mNotificationID =001;
        NotificationManager mNotify = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotify.notify(mNotificationID, mBuilder.build());


    }





public class SinchCallClientListener implements CallClientListener {
    @Override
    public void onIncomingCall(CallClient callClient, Call incomingCall) {
        //Pick up the call!
        tablay.setVisibility(View.INVISIBLE);
        sec.setVisibility(View.VISIBLE);
        ap.playRingtone();
        notification();

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




