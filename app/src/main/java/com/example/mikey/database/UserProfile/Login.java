package com.example.mikey.database.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandler;
import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Password.ForgotPassword;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by mikey on 02/02/2016.
 */

public class Login extends AppCompatActivity {
    private static final String LOGIN_URL = "http://www.companion4me.x10host.com/webservice/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    DatabaseUsernameId dbHandlerId;
    HashMap<String,String> idUserHash;
    DatabaseHandler dbHandler;
    //buttons
    Button login;
    Button signUp;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private String email;
    //TextView signUp;
    private EditText emailBox, passwordBox;                 //textfields for user to enter their emails and password

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        dbHandler = new DatabaseHandler(this);
        dbHandlerId = new DatabaseUsernameId(this);


        passwordBox = (EditText) findViewById(R.id.passwordBox);
        emailBox = (EditText) findViewById(R.id.emailBoxLogin);

        signUp = (Button) findViewById(R.id.btn_signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

               dbHandler.resetTables();
                Intent intent = new Intent(getApplicationContext(), registerUser.class);
                startActivity(intent);
                finish();   //end current activity
            }
        });

        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail(emailBox.getText().toString())==true && validatePassword(passwordBox.getText().toString())) {
                    setEmail(emailBox.getText().toString());

                    String username = getEmail();
                    String password = computeMD5Hash(passwordBox.getText().toString());
                    dbHandler.resetTables();
                    dbHandler.addUser(null, null, null, getEmail(), null, null, null,null,null,null,null);
                    dbHandlerId.resetTables();

                    dbHandlerId.addUser(null, null,null,getEmail(),null,null,null);
                    new AttemptLogin().execute(username,password);


                }
            }
        });


    }

    /**
     * used to validate email (username)
     * */

    public boolean validateEmail(String email) {
        boolean valid = true;
      /*  emailBox = (EditText) findViewById(R.id.emailBoxLogin);
        String email = emailBox.getText().toString();
        setEmail(email);
*/

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailBox.setError("Enter a valid email address");
            //   Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();

            valid = false;

        }


        return valid;


    }

    /**
     * passwordLegth is the password the user enters,
     * passwordBox.setError("Password must be between 4 and 10 alphanumeric characters"); will need to be added depending on how it will be implemented
     * swordBox is the EditText for (R.id.passwordBox)
     * */
    public boolean validatePassword(String password) {
        boolean valid = true;

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            passwordBox.setError("Enter a valid password");

            valid = false;
            //Toast.makeText(getApplicationContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
        }


        return valid;
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

    public void onClickHelp(View view) {
        dbHandler.resetTables();
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    class AttemptLogin extends AsyncTask<String, String, String> {


        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String username = args[0];
            String password = args[1];
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    Intent i = new Intent(Login.this, Home.class);
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
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }
}


