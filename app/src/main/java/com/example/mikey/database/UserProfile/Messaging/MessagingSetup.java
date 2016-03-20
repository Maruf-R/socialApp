package com.example.mikey.database.UserProfile.Messaging;

import com.example.mikey.database.Database.DatabaseHandlerMessaging;
import com.sinch.android.rtc.SinchError;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

public class MessagingSetup extends BaseActivity implements SinchService.StartFailedListener {
    private ProgressDialog mSpinner;

    protected Context context;
    DatabaseHandlerMessaging dbHandlerMsg;
    HashMap<String,String> idUserHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MessagingSetup(Context context){
        this.context = context.getApplicationContext();
        setup();
    }

    public void setup(){
        dbHandlerMsg = new DatabaseHandlerMessaging(context);
        idUserHash = dbHandlerMsg.getUserDetails();

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(idUserHash.get("email"));    //TODO: NEED TO CHECK IS I AM SUPPOSED TO USE THE KEY_EMAIL HERE OR THE KEY_NAME
            showSpinner();
        } else {
            mSpinner.dismiss();
            openMessagingActivity();
        }
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openMessagingActivity();
    }

    private void openMessagingActivity() {
        Intent messagingActivity = new Intent(this, MessagingActivity.class);
        startActivity(messagingActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }
}