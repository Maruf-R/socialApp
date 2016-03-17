package com.example.mikey.database.UserProfile.Profile;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandler;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Profile extends AppCompatActivity {
    private static final String REGISTER_DATA = "http://www.companion4me.x10host.com/webservice/registerdata.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "userdata";
    private static final String TAG_AGE = "age";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_NAME = "name";
    private static final String TAG_NATIONALITY = "nationality";
    TextView nameV;
    TextView ageV;
    TextView nationalityV;
    JSONParser jParser = new JSONParser();
    JSONArray ldata = null;
    DatabaseHandler dbHandler;
    HashMap<String, String> hash;
    private String _username;
    private String _name;
    private String _age;
    private String _nationality;
    private ProgressDialog pDialog;

    public String get_age() {
        return _age;
    }

    public void set_age(String _age) {
        this._age = _age;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_nationality() {
        return _nationality;
    }

    public void set_nationality(String _nationality) {
        this._nationality = _nationality;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dbHandler = new DatabaseHandler(this);
        hash = dbHandler.getUserDetails();

        new Profile.GetTheProfileData().execute();


    }

    public class GetTheProfileData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Profile.this);
            pDialog.setMessage("Loading Profile...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {

            try {

                System.out.println("this is the email db " + hash.get("email"));
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", hash.get("email")));

                Log.d("request!", "starting");

                JSONObject json = jParser.makeHttpRequest(
                        REGISTER_DATA, "POST", params);
                // check your log for json response
                Log.d("Login attempt", json.toString());
                ldata = json.getJSONArray(TAG_USERS);
                // looping through all users according to the json object returned
                for (int i = 0; i < ldata.length(); i++) {
                    JSONObject c = ldata.getJSONObject(i);

                    //gets the content of each tag
                    String username = c.getString(TAG_USERNAME);
                    String name = c.getString(TAG_NAME);
                    String age = c.getString(TAG_AGE);
                    String nationality = c.getString(TAG_NATIONALITY);

                    set_name(name);
                    set_age(age);
                    set_nationality(nationality);
                    set_username(username);


                    System.out.println("this is the username php: " + username);
                    System.out.println("this is the name php: " + name);

                    System.out.println("this is the age php: " + age);
                    System.out.println("this is the natio php: " + nationality);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            nameV = (TextView) findViewById(R.id.Name_txt);
            ageV = (TextView) findViewById(R.id.Age_txt);
            nationalityV = (TextView) findViewById(R.id.txtMyNation);

            nameV.setText(get_name());
            ageV.setText(get_age());
            nationalityV.setText(get_nationality());

            System.out.println("this is the name l: " + get_name());

            System.out.println("this is the age l: " + get_age());
            System.out.println("this is the natio l: " + get_nationality());


            pDialog.dismiss();


            if (file_url != null){
                Toast.makeText(Profile.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }

}

















