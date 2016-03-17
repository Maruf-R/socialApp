package com.example.mikey.database.UserProfile.Password;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForgotPassword extends AppCompatActivity {

    private static final String PASSREC = "http://www.companion4me.x10host.com/webservice/recoverypassword.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    JSONParser jParser = new JSONParser();
    JSONArray ldata = null;
    DatabaseHandler dbHandler;
    private EditText editForgotEmail;
    private EditText editSecretAnswer;
    private TextView secQue;
    private Button btnForgot;
    private String usemail, usquestion, usanswer;
    private String inputEmail;
    private ProgressDialog pDialog;

    public String getUsemail() {
        return usemail;
    }

    public void setUsemail(String usemail) {
        this.usemail = usemail;
    }

    public String getUsquestion() {
        return usquestion;
    }

    public void setUsquestion(String usquestion) {
        this.usquestion = usquestion;
    }

    public String getUsanswer() {
        return usanswer;
    }

    public void setUsanswer(String usanswer) {
        this.usanswer = usanswer;
    }

    public String getInputEmail() {
        return inputEmail;
    }

    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }

    public void launchDialogError() {
        final AlertDialog.Builder builders = new AlertDialog.Builder(this);

        builders.setTitle("The details you entered do not match with our records");
        builders.setMessage("The details you are entered do not match our records,"+"\n"+"Please try again.");

        //builder.setIcon(android.R.drawable.ic_dialog_alert);

        builders.setPositiveButton("Try again", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        builders.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        secQue = (TextView)findViewById(R.id.textQuestion);
        btnForgot = (Button) findViewById(R.id.btnForgotSend);
        dbHandler = new DatabaseHandler(this);

        secQue.setText(getUsquestion());

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail();

                new GetRecoveryData().execute();

                checkData();


                if(checkData()==true&&validateEmail()==true){

                    Intent newPass = new Intent(getApplicationContext(),NewPassword.class);
                    startActivity(newPass);
                    finish();
                    Toast.makeText(ForgotPassword.this, "SUCCESS, NOW ENTER A VALID PASSWORD", Toast.LENGTH_LONG).show();

                    dbHandler.resetTables();
                    dbHandler.addUser(null, null, null, getInputEmail(), null, getUsanswer(), getUsquestion());


            }

                else if(checkData()==false&&validateEmail()==false) {
                    launchDialogError();


                    Toast.makeText(ForgotPassword.this, "FAILED, TRY AGAIN", Toast.LENGTH_LONG).show();
                }

            }
        });



    }

public boolean checkData(){
    boolean valid = false;

    editForgotEmail = (EditText)findViewById(R.id.editForgotEmail);
    editSecretAnswer = (EditText)findViewById(R.id.editSecretAnswer);
    String inputAnswer = editSecretAnswer.getText().toString();

    System.out.println("this is the username get: " + getUsemail());
    System.out.println("this is the answer:  get" + getUsanswer());

    // System.out.println("this is the age php: " + age);
    System.out.println("this is the question php: get" + getUsquestion());
    System.out.println("this is the username input " + getUsemail());
    System.out.println("this is the answer:  input" + getUsanswer());

    // System.out.println("this is the age php: " + age);
    System.out.println("this is the question php: input" + getUsquestion());




    if(getInputEmail().equals(getUsemail())&&inputAnswer.equals(getUsanswer())){
            valid = true;

    }


return valid;


}



    public void onClickReturnToLogin(View view) {
        Intent log = new Intent(getApplicationContext(),Login.class);
        startActivity(log);
        finish();
    }

    protected void onPostExecute(String file_url) {

//            pDialog.dismiss();


        if (file_url != null) {
            Toast.makeText(ForgotPassword.this, file_url, Toast.LENGTH_LONG).show();
        }

    }

    public boolean validateEmail() {
        boolean valid = true;
        editForgotEmail = (EditText) findViewById(R.id.editForgotEmail);
        String email = editForgotEmail.getText().toString();
        setInputEmail(email);


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editForgotEmail.setError("Enter a valid email address");

            valid = false;

        }

        return valid;


    }

    public class GetRecoveryData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
         /*   super.onPreExecute();
            pDialog = new ProgressDialog(ForgotPassword.this);
            pDialog.setMessage("Loading Profile...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
*/

        }

        @Override
        protected String doInBackground(String... args) {

            try {

               // System.out.println("this is the email db " + hash.get("email"));
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username",getInputEmail()));

                Log.d("request!", "starting");

                JSONObject json = jParser.makeHttpRequest(
                        PASSREC, "POST", params);
                // check your log for json response
                Log.d("Login attempt", json.toString());
                ldata = json.getJSONArray("recoveryarray");
                // looping through all users according to the json object returned
                for (int i = 0; i < ldata.length(); i++) {
                    JSONObject c = ldata.getJSONObject(i);

                    //gets the content of each tag
                    String username = c.getString("username");
                    String answer = c.getString("answer");
                  //  String age = c.getString("age");
                    String question = c.getString("question");
                    //  String nationality = c.getString("nationality");

setUsanswer(answer);
setUsemail(username);
setUsquestion(question);
                    System.out.println("this is the username php: " + username);
                    System.out.println("this is the name answer php: " + answer);

                    // System.out.println("this is the age php: " + age);
                    System.out.println("this is the question php: " + question);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }
    }


}
