package com.example.mikey.database.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandlerContacts;
import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zapatacajas on 19/03/2016.
 */
public class ChangeSecurityQuestionAnswer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerSecretQuestions;
    private String[] spinnerQuestion={"What was your first pet's name?",
            "What was your first car?",("What was your first love's name?"),("What was the city you were born?")};
    public String[] getSpinnerQuestion() { return spinnerQuestion; }

    EditText answerEt;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    private String question;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL ="http://www.companion4me.x10host.com/webservice/editSecRegister.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private Button btnSaveChanges;




    DatabaseUsernameId dbHandlerId;



    HashMap<String, String> hash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_security_details);
        dbHandlerId = new DatabaseUsernameId(this);
        hash = dbHandlerId.getUserDetails();
        btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);
        spinnerQuestion();


        // dbHandlerId = new DatabaseUsernameId(this);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateAnswer();
                if(validateAnswer()==true) {


                    new ChangeSecurityQuestionAnswer.EditSecurityDetails().execute(getAnswer());
                }
            }



        });

    }

    class EditSecurityDetails extends AsyncTask<String, String, String> {


        //  boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangeSecurityQuestionAnswer.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;
            String answer = args[0];

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", hash.get("email")));
                params.add(new BasicNameValuePair("answer", answer));
                params.add(new BasicNameValuePair("question", getQuestion()));
//                params.add(new BasicNameValuePair("minimum", hashC.get("city")));
// add if done

                System.out.println("they pass to email ver gender" +  hash.get("email"));

                System.out.println("they pass to email ver gender" +  answer);
                System.out.println("pass edu emai ver" + getQuestion());

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

                // json success element

                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Log.d("profile edited", json.toString());

                    Intent intent = new Intent(ChangeSecurityQuestionAnswer.this, Home.class);
                    startActivity(intent);

                    finish();
                    return json.getString(TAG_MESSAGE);


                } else {

                    Log.d("edit Failure!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (file_url != null) {
                Toast.makeText(ChangeSecurityQuestionAnswer.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

    public boolean validateAnswer() {
        boolean valid = true;

        answerEt = (EditText) findViewById(R.id.answer_atregister);
        String answer = answerEt.getText().toString();
        setAnswer(answer);


        if (answer.isEmpty()) {
            answerEt.setError("Answer cannot be empty.");

            valid = false;

        }
        else{
            answerEt.setError(null);
        }


        return valid;


    }

    public void spinnerQuestion() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerQuestion);
        //   dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSecretQuestions = (Spinner)findViewById(R.id.spinnerSecretQuestion);
        spinnerSecretQuestions.setAdapter(dataAdapter);
        spinnerSecretQuestions.setOnItemSelectedListener(this);


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item




        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinnerSecretQuestion)
        {
            String itemQ = parent.getItemAtPosition(position).toString();
            setQuestion(itemQ);

        }


        // Showing selected spinner item
        //    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
