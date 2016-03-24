package com.example.mikey.database.UserProfile.Messaging;

import com.example.mikey.database.Database.DatabaseHandlerMessaging;
import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Profile.ContactProfile;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagingActivity extends BaseActivity implements MessageClientListener, Serializable {

    private static final String TAG = MessagingActivity.class.getSimpleName();

    //for online database
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String SEND_MSG_URL ="http://www.companion4me.x10host.com/webservice/messaging.php";
    private static final String TAG_SUCCESS = "success";


    private MessageAdapter mMessageAdapter;
    private TextView mTxtRecipient;
    private EditText mTxtTextBody;
    private Button mBtnSend;
    protected Context context;

    //For database purposes
    DatabaseHandlerMessaging dbHandlerMsg;
    HashMap<String,String> AllUserMessages_Hash;
    DatabaseUsernameId userDB;
    HashMap<String,String> userDB_Hash;

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    private String recipientId;

    /* EMERGENCY SOLUTION
    @Override
    public void onBackPressed(){


        Intent intent = new Intent(MessagingActivity.this, ContactProfile.class);
     //   moveTaskToBack(true);
        startActivity(intent);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        dbHandlerMsg = new DatabaseHandlerMessaging(this);
        AllUserMessages_Hash = dbHandlerMsg.getAllUserMessages();
        userDB = new DatabaseUsernameId(this);
        userDB_Hash = userDB.getUserDetails();
        Intent intent = getIntent();
      String recipientUsername = intent.getStringExtra("idf");
        setRecipientId(recipientUsername);
        System.out.println(recipientUsername);

        mTxtRecipient = (TextView) findViewById(R.id.Message_Recipient);
        mTxtRecipient.setText(recipientUsername);
        mTxtTextBody = (EditText) findViewById(R.id.Message_TextBody);

        mMessageAdapter = new MessageAdapter(this);
        ListView messagesList = (ListView) findViewById(R.id.Messages);
        messagesList.setAdapter(mMessageAdapter);

        mBtnSend = (Button) findViewById(R.id.sendButton);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    @Override
    public void onDestroy() {
        /*if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().removeMessageClientListener(this);
        }*/
        super.onDestroy();
    }

    @Override
    public void onServiceConnected() {
        getSinchServiceInterface().addMessageClientListener(this);
        setButtonEnabled(true);
    }

    @Override
    public void onServiceDisconnected() {
        setButtonEnabled(false);
    }

    private void sendMessage() {
       // String recipient = mTxtRecipient.getText().toString();
        String textBody = mTxtTextBody.getText().toString();
        if (getRecipientId().isEmpty()) {
            Toast.makeText(this, "No recipient added", Toast.LENGTH_SHORT).show();
            return;
        }
        if (textBody.isEmpty()) {
            Toast.makeText(this, "No text message", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHandlerMsg.addMessage(userDB_Hash.get("email"), getRecipientId(), textBody);
        getSinchServiceInterface().sendMessage(getRecipientId(), textBody);

        System.out.println("sender" + userDB_Hash.get("email"));
        System.out.println("receiver"+getRecipientId());
        System.out.println("textbody"+textBody);



        mTxtTextBody.setText("");

        new MessagingActivity.SendMessage().execute(userDB_Hash.get("email"), getRecipientId(),textBody);
    }

    private void setButtonEnabled(boolean enabled) {
        mBtnSend.setEnabled(enabled);
    }

    @Override
    public void onIncomingMessage(MessageClient client, Message message) {
        mMessageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
    }

    @Override
    public void onMessageSent(MessageClient client, Message message, String recipientId) {
        mMessageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
    }

    @Override
    public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {
        // Left blank intentionally
    }

    @Override
    public void onMessageFailed(MessageClient client, Message message,
                                MessageFailureInfo failureInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sending failed: ")
                .append(failureInfo.getSinchError().getMessage());

        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        Log.d(TAG, sb.toString());
    }

    @Override
    public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {
        Log.d(TAG, "onDelivered");
    }




    class SendMessage extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MessagingActivity.this);
            pDialog.setMessage("Sending message...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            String FromUser = args[0];
            String ToUser = args[1];
            String MessageBody = args[2];

            System.out.println("args0 in php" + FromUser);
            System.out.println("args1 in php" + ToUser);
            System.out.println("args2 in php" + MessageBody);


            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("FromUser", FromUser));
                params.add(new BasicNameValuePair("ToUser", ToUser));
                params.add(new BasicNameValuePair("MessageBody", MessageBody));

                System.out.println("FromUser in php" + FromUser);
                System.out.println("ToUser in php" + ToUser);
                System.out.println("Message in php" + MessageBody);
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        SEND_MSG_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }
}