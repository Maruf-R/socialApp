package com.example.mikey.database.UserProfile.VoiceCall;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseUsernameId;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;



import java.util.HashMap;
import java.util.List;

/**
 * Created by zapatacajas on 16/03/2016.
 */
public class ReceiveCallHolder extends AppCompatActivity{
    private static final String APP_KEY = "ef1889da-3241-4700-a675-200b6b15fecd";
    private static final String APP_SECRET = "fsNJI+ziSkG1d+rDsW84wA==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    protected Context context;
    DatabaseUsernameId dbHandlerId;
    HashMap<String,String> idUserHash;
    private Call call;

    public ReceiveCallHolder(Context context) {
        this.context = context.getApplicationContext();
    }

    public void startMike(Context context) {

        dbHandlerId = new DatabaseUsernameId(context);
        idUserHash = dbHandlerId.getUserDetails();

        SinchClient sinchClient = Sinch.getSinchClientBuilder()
                .context(context)
                .userId(idUserHash.get("email"))
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();

        sinchClient.setSupportCalling(true);

        sinchClient.startListeningOnActiveConnection();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        sinchClient.start();
    }

         class SinchCallClientListener implements CallClientListener {

            @Override
            public void onIncomingCall(CallClient callClient, Call incomingCall) {
                //Pick up the call!
/*                Intent inCall = new Intent(getApplicationContext(), receiveCallTest.class);
                startActivity(inCall);
*/
/*                Toast.makeText(getApplicationContext(), "receiving call", Toast.LENGTH_LONG).show();*/

                call = incomingCall;
                call.answer();
                call.addCallListener(new SinchCallListener());

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
