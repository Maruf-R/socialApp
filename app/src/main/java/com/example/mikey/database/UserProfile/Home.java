package com.example.mikey.database.UserProfile;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Profile.Profile;
import com.example.mikey.database.UserProfile.VoiceCall.AudioPlayer;
import com.example.mikey.database.UserProfile.VoiceCall.receiveCallTest;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Home extends TabActivity{
    private static final String APP_KEY = "ef1889da-3241-4700-a675-200b6b15fecd";
    private static final String APP_SECRET = "fsNJI+ziSkG1d+rDsW84wA==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    DatabaseUsernameId dbHandlerId;
    HashMap<String, String> idUserHash;
    static ImageButton accept, decline;

   public static Call call;
   public static SinchClient sinchClient;
    AudioPlayer ap;//holds the audio player
    static int mNotificationID =001;
    static NotificationManager mNotify;

    private static final String CALLER_DATA = "http://www.companion4me.x10host.com/webservice/registerdata.php";
    private String _username;
    private String _name;
    private String _age;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_age() {
        return _age;
    }

    public void set_age(String _age) {
        this._age = _age;
    }

    private String avatar;

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    JSONArray ldata = null;




/*ReceiveCallHolder mike;*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      /*  mike = new ReceiveCallHolder(this);
        mike.startMike(this);*/
        ap = new AudioPlayer(this);

      dbHandlerId = new DatabaseUsernameId(this);

        idUserHash = dbHandlerId.getUserDetails();

///////////////////////////////////////////////////////////////////////////// this is receiving calls
       sinchClient = Sinch.getSinchClientBuilder()
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


/*

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


                //Toast.makeText(ContactProfile.this,"Call ended by you." , Toast.LENGTH_LONG).show();


            }
        });

*/
///////////////////////////////////////////////////////////////////////////////


        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.getTabWidget().setBackgroundColor(Color.parseColor("#CDDC39"));


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
        tabHost.addTab(tab2);

        tabHost.addTab(tab1);

        tabHost.addTab(tab4);

        tabHost.addTab(tab3);

    }



    public void notification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.companion)
                .setContentTitle("Companion4Me")
                .setContentText("Incoming Call!")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
/*
        Intent resultIntent = new Intent(this, receiveCallTest.class);
        PendingIntent h = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/
/*
        Intent answer = new Intent (this, NotificationAcceptAction.class);
        PendingIntent pendingIntentAnswer = PendingIntent.getBroadcast(this, 0, answer, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent decline = new Intent (this, NotificationDeclineAction.class);
        PendingIntent pendingIntentDecline = PendingIntent.getBroadcast(this, 0, decline, PendingIntent.FLAG_UPDATE_CURRENT);
*/
       // mBuilder.setContentIntent(h);
     //   mBuilder.addAction(R.drawable.incoming_phone,"Accept",pendingIntentAnswer);
      //  mBuilder.addAction(R.drawable.end_call,"Decline",pendingIntentDecline);

        mNotify = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotify.notify(mNotificationID, mBuilder.build());

    }

/*
    public static class NotificationAcceptAction extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            accept.performClick();
            mNotify.cancel(mNotificationID);

        }
    }

    public static class NotificationDeclineAction extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            decline.performClick();
            mNotify.cancel(mNotificationID);

        }
    }
*/


    public class SinchCallClientListener implements CallClientListener {
    @Override
    public void onIncomingCall(CallClient callClient, Call incomingCall) {
        //Pick up the call!
        call = incomingCall;
        ap.playRingtone();

        call.addCallListener(new SinchCallListener());
        new Home.GetTheProfileData().execute(call.getRemoteUserId().toString());


        notification();

        Toast.makeText(Home.this, "receiving call", Toast.LENGTH_LONG).show();




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


    public class GetTheProfileData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            String caller = args[0];

            try {
// namef is the email(username) of the friend

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", caller));
                System.out.println("username of caller" + caller );

                Log.d("request!", "starting");

                JSONObject json = jParser.makeHttpRequest(
                        CALLER_DATA, "POST", params);
                // check your log for json response
                Log.d("Login attempt getdata", json.toString());
                ldata = json.getJSONArray("userdata");
                // looping through all users according to the json object returned
                for (int i = 0; i < ldata.length(); i++) {
                    JSONObject c = ldata.getJSONObject(i);

                    //gets the content of each tag

                    String name = c.getString("name");
                    String age = c.getString("age");


                    setAvatar(c.getString("avatar"));
                    set_name(name);
                    set_age(age);



                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



            return null;

        }

        protected void onPostExecute(String file_url) {

            Intent h = new Intent(Home.this,receiveCallTest.class);
            h.putExtra("callingto", get_name());
            h.putExtra("avatar", getAvatar());
            h.putExtra("age", get_age());

            startActivity(h);
            if (file_url != null){
                Toast.makeText(Home.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }







}




