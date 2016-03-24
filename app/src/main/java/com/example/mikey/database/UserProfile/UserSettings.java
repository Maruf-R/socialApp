package com.example.mikey.database.UserProfile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandler;
import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Password.NewPassword;
import com.example.mikey.database.UserProfile.Profile.EditUserProfile;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserSettings extends AppCompatActivity {

    private Button btnEditUserProfile;
    private Button btnEditUserInterests;
    private Button btnBlockContact;
    private Button btnLogOut;
    private Button btnDeleteAccount;
    private Button btnChangeSecurityDetails;
    private Button btnUserGuide;
    DatabaseUsernameId dbHandlerId;
    HashMap<String, String> idUserHash;
    JSONParser jsonParserg = new JSONParser();
    private ProgressDialog pDialog;
    private Button btnChangePassword;

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
        setContentView(R.layout.activity_user_settings);

        dbHandlerId = new DatabaseUsernameId(this);
        idUserHash = dbHandlerId.getUserDetails();

        // Userguide button
              btnUserGuide = (Button) findViewById(R.id.btnUserGuide);
               btnUserGuide.setOnClickListener(new View.OnClickListener() {
                      @Override
                       public void onClick(View v) {
                               Intent guide = new Intent(UserSettings.this, Guide.class);
                          startActivity(guide);


                           }
                    });



        // Starting EditUserProfile activity
        btnEditUserProfile = (Button) findViewById(R.id.btnEditProfile);
        btnEditUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettings.this, EditUserProfile.class);
                startActivity(intent);
            }
        });

        btnChangeSecurityDetails = (Button) findViewById(R.id.btnSecDet);
        btnChangeSecurityDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettings.this, ChangeSecurityQuestionAnswer.class);
                startActivity(intent);
            }
        });


        //Start UserInterests activity
        btnEditUserInterests = (Button) findViewById(R.id.btnEditUserInterests);
        btnEditUserInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettings.this, UserInterests.class);
                startActivity(intent);
            }
        });

        //start activity which will show blocked contacts
        btnBlockContact = (Button) findViewById(R.id.btnBlockContact);
        btnBlockContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //start activity which will show blocked contacts
        btnBlockContact = (Button) findViewById(R.id.btnBlockContact);
        btnBlockContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             Intent getblock = new Intent(UserSettings.this, BlockedContacts.class);
                startActivity(getblock);

            }
        });

        // log out of the profile and redirect to log in screen
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO log out of profile and redirect to login screen
                Intent intent = new Intent(UserSettings.this, Login.class);
                finish();
                startActivity(intent);


            }
        });
        btnChangePassword = (Button)findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettings.this, NewPassword.class);
                startActivity(intent);
                //TODO look into finishing this activity
            }
        });

        //
        btnDeleteAccount = (Button) findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO ask the user to enter their password, confirm they want to delete account,
                // TODO then use the appropriate SQL command to delete and redirecet to login activity.
                AlertDialog.Builder builder = new AlertDialog.Builder(UserSettings.this);
              final  EditText input = new EditText(UserSettings.this);
                builder.setTitle("Enter your password to delete account");




                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String h = computeMD5Hash(input.getText().toString());

                        new UserSettings.deleteAccount().execute(h);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

                builder.show();
            }
        });


    }

    private static final String LOGIN_URL = "http://www.companion4me.x10host.com/webservice/deleteAccount.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    class deleteAccount extends AsyncTask<String, String, String> {


        boolean failure = false;

        @Override
        protected void onPreExecute() {
           /* super.onPreExecute();
            pDialog = new ProgressDialog(UserSettings.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String username = idUserHash.get("email");
            String password = args[0];
            System.out.println("username has passed delete"+username);
            System.out.println("password has passed delete"+password);

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                System.out.println("username has passed delete in" + username);
                System.out.println("password has passed delete in" + password);
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParserg.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("deleted succesful", json.toString());
                    Intent i = new Intent(UserSettings.this, Login.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog
            //  pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(UserSettings.this, file_url, Toast.LENGTH_LONG).show();
            }

        }


    }
    public String computeMD5Hash(String password) {

        StringBuffer MD5Hash = new StringBuffer();
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

            Log.d("The hash is: ", String.valueOf(MD5Hash));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return MD5Hash.toString();
    }
}
