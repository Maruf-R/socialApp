package com.example.mikey.database.UserProfile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmailVerification extends AppCompatActivity {

    private static final String LOGIN_URL = "http://www.companion4me.x10host.com/webservice/register.php";
    private static final String CODE_URL = "http://www.companion4me.x10host.com/webservice/registerGetCode.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_CODE = "codearray";
    EditText inputCode;
    Button verifyRegister;
    DatabaseHandler dbHandler;
    DatabaseUsernameId dbHandlerId;

    public int getSuccessHolder() {
        return successHolder;
    }

    public void setSuccessHolder(int successHolder) {
        this.successHolder = successHolder;
    }

    private int successHolder;

    HashMap<String, String> hash;
    JSONParser jsonParser = new JSONParser();
    JSONParser jParser = new JSONParser();
    JSONArray ldata = null;
    String verificationcodesql;
    private ProgressDialog pDialog;

    public String getVerificationcodesql() {
        return verificationcodesql;
    }

    public void setVerificationcodesql(String verificationcodesql) {
        this.verificationcodesql = verificationcodesql;
    }

    public void lauchDialog() {
        final AlertDialog.Builder builders = new AlertDialog.Builder(this);

        builders.setTitle("You are already registered");
        builders.setMessage("If you don't remember your password, we will help you to create a new one. Select from the following options:");

        //builder.setIcon(android.R.drawable.ic_dialog_alert);

        builders.setPositiveButton("Create new password", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent fp = new Intent(EmailVerification.this, ForgotPassword.class);
                startActivity(fp);
                finish();
            }
        });

        builders.setNeutralButton("Back to Log In", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent log = new Intent(EmailVerification.this, Login.class);
                startActivity(log);
                finish();
            }
        });



        builders.show();
    }



    public void lauchDialogWrong() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Code not valid");
        builder.setMessage("The code you are entering does not match our records, please try again.");

        //builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Try again.", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });




        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_layout);


        new EmailVerification.GetUserCode().execute();

        dbHandler = new DatabaseHandler(this);
        hash = dbHandler.getUserDetails();

        dbHandlerId = new DatabaseUsernameId(this);

        inputCode = (EditText) findViewById(R.id.verify_input);
        verifyRegister = (Button) findViewById(R.id.verify_button);

        verifyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputcode = inputCode.getText().toString();
                System.out.println("can access input inside " + inputcode);
                if (!inputcode.equals(getVerificationcodesql())) {
                    lauchDialogWrong();
                }
                    else {
                    new CreateUser().execute();
                }




            }


        });

    }




    class CreateUser extends AsyncTask<String, String, String> {


        //  boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmailVerification.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;

            String username = hash.get("email");
            String password = hash.get("password");
            String name = hash.get("name");
            String age = hash.get("age");
            String nationality = hash.get("nationality");
            String answer = hash.get("answer");
            String question = hash.get("question");


            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("age", age));
                params.add(new BasicNameValuePair("nationality", nationality));
                params.add(new BasicNameValuePair("answer", answer));
                params.add(new BasicNameValuePair("question", question));
                params.add(new BasicNameValuePair("education", hash.get("education")));
                params.add(new BasicNameValuePair("gender", hash.get("gender")));
                params.add(new BasicNameValuePair("country", hash.get("country")));
               params.add(new BasicNameValuePair("city", hash.get("city")));
// add if done

                System.out.println("they pass to email ver gender" +  hash.get("education"));
                System.out.println("pass edu emai ver" + hash.get("gender"));
                System.out.println("pass country" + hash.get("country"));
                System.out.println("pass city" + hash.get("city"));


                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

                // json success element

                success = json.getInt(TAG_SUCCESS);
                setSuccessHolder(success);
                if (success == 1) {
                    Log.d("User Created!", json.toString());


                    dbHandlerId.resetTables();

                    dbHandlerId.addUser(null, null, null, hash.get("email"), null, null, null);



                    Intent intent = new Intent(EmailVerification.this, UserInterests.class);
                    startActivity(intent);

                    finish();
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

            if(getSuccessHolder()==0){
                lauchDialog();

            }


pDialog.dismiss();

            if (file_url != null) {
                Toast.makeText(EmailVerification.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }





    public class GetUserCode extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... args) {

            try {

                System.out.println("this is the email db " + hash.get("email"));
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", hash.get("email")));

                Log.d("request!", "starting");

                JSONObject json = jParser.makeHttpRequest(
                        CODE_URL, "POST", params);
                // check your log for json response
                Log.d("Login attempt", json.toString());
                ldata = json.getJSONArray(TAG_CODE);
                // looping through all users according to the json object returned
                for (int i = 0; i < ldata.length(); i++) {
                    JSONObject c = ldata.getJSONObject(i);

                    //gets the content of each tag
                    String verificationcode = c.getString("verificationcode");

                    setVerificationcodesql(verificationcode);

                    System.out.println("code got: "+verificationcode);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {



/*
            if (file_url != null) {
                Toast.makeText(EmailVerification.this, file_url, Toast.LENGTH_LONG).show();
            }
*/
        }


    }
}