package com.example.mikey.database.UserProfile.Password;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandler;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Login;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewPassword extends AppCompatActivity {


     private EditText password1;
    private EditText password2;
    private Button createPassword;
    private TextView errorMessage;
    DatabaseHandler dbHandler;
    HashMap<String, String> hash;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL ="http://www.companion4me.x10host.com/webservice/updatePassword.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = computeMD5Hash(password);
    }

    private String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        createPassword = (Button) findViewById(R.id.btn_new_password);
        dbHandler = new DatabaseHandler(this);
        hash = dbHandler.getUserDetails();




        createPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPasswords();

                if (checkPasswords() == true) {
new NewPassword.updatePassword().execute(hash.get("email"),getPassword());

                }} });

    }


/*

    class updatePassword extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewPassword.this);
            pDialog.setMessage("Sending email...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            String username = args[0];
            String password = args[1];

            System.out.println("args0 in php" + username);
            System.out.println("args1 in php" + password);


            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                System.out.println("username: in php" + username);
                System.out.println("code in php" + password);
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

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


            Intent i = new Intent(NewPassword.this, Login.class);
            //  finish();
            startActivity(i);


             finish();
            if (file_url != null){
                Toast.makeText(NewPassword.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }




*/



    public boolean checkPasswords() {

        boolean valid = false;

        password1 = (EditText) findViewById(R.id.enterercovery);
        password2 = (EditText) findViewById(R.id.reenterercovery);
        errorMessage = (TextView) findViewById(R.id.forgot_error);


        String password1Text = password1.getText().toString();
        String password2Text = password2.getText().toString();


        if (password1Text.equals(password2Text) && validatePassword(password1Text) == true) {


            setPassword(password1.getText().toString());

            //signupOk();

            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            valid = true;

        }

        else if (validatePassword(password1Text) == false) {
            password1.setError("Password must be between 4 and 20 alphanumeric characters");
            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();


        }

        else if (!password1Text.equals(password2Text) && validatePassword(password1Text) == false) {

            errorMessage.setText("Passwords do not match, Please check and try again.");
            password1.setError("Password must be between 4 and 20 characters, including numbers and letters.");

            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
        }

        else {
            errorMessage.setText("Passwords do not match, Please check and try again.");

            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
        }

        return valid;

    }


    public boolean validatePassword(String passwordLength) {
        boolean valid = true;

        if (passwordLength.isEmpty() || passwordLength.length() < 4 || passwordLength.length() > 20) {

            valid = false;
        }



        return valid;
    }


    class updatePassword extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewPassword.this);
            pDialog.setMessage("Sending email...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            String username = args[0];
            String password = args[1];

            System.out.println("args0 in php" + username);
            System.out.println("args1 in php" + password);


            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                System.out.println("username: in php" + username);
                System.out.println("code in php" + password);
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

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


            Intent i = new Intent(NewPassword.this, Login.class);
            //  finish();
            startActivity(i);


            finish();
            if (file_url != null) {
                Toast.makeText(NewPassword.this, file_url, Toast.LENGTH_LONG).show();
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
