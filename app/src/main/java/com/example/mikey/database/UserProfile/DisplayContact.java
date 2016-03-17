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
import android.widget.ListView;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandlerContacts;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Profile.ContactProfile;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zapatacajas on 13/03/2016.
 */
public class DisplayContact extends AppCompatActivity {
    private static final String REGISTER_DATA = "http://www.companion4me.x10host.com/webservice/allregisterdata.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "userdataC";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_NAME = "name";
    private static final String TAG_NATIONALITY = "nationality";
    ListView listview;
    JSONParser jParserC = new JSONParser();
    JSONArray ldataC = null;
    DatabaseHandlerContacts dbHandler;
    HashMap<String, String> hashC;
    HashMap<String, String> userHash;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_results);

        dbHandler = new DatabaseHandlerContacts(this);
        hashC = dbHandler.getUserContacts();
        userHash = new HashMap<>();


        System.out.println("this is the name search obj: for real " + hashC.get("name"));
        System.out.println("this is the name search obj: for real" + hashC.get("email"));
        System.out.println("this is the name search obj: for real" + hashC.get("maxa"));

        System.out.println("this is the name search obj: for real" + hashC.get("mina"));


        new DisplayContact.GetMatchedContacts().execute();

    }

    public void listItems() {

        listview = (ListView) findViewById(R.id.contacts_result);


        final ArrayList<String> textViewObjects = new ArrayList<String>();

        Iterator it = userHash.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey().toString() + " = " + pair.getValue().toString());

            textViewObjects.add(pair.getKey().toString());


        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, textViewObjects);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listview.getItemAtPosition(position);


                System.out.println("it worked name" + userHash.get(itemValue));
                System.out.println("it worked username" + userHash.get(itemValue));


                dbHandler.resetTablesContacts();
                dbHandler.addUserContacts(null, null, null, null, null, null, userHash.get(itemValue));


                Intent friend = new Intent(getApplicationContext(), ContactProfile.class);
                startActivity(friend);


            }
        });


    }

    public class GetMatchedContacts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DisplayContact.this);
            pDialog.setMessage("Loading Contatcts Found...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {


            try {

                System.out.println("this is the email db for real" + hashC.get("email"));
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", hashC.get("email")));
                params.add(new BasicNameValuePair("name", hashC.get("name")));
                params.add(new BasicNameValuePair("nationality", hashC.get("nationality")));
                params.add(new BasicNameValuePair("maximum", hashC.get("maxa")));
                params.add(new BasicNameValuePair("minimum", hashC.get("mina")));



                System.out.println("this is the email db for real" + hashC.get("name"));
                System.out.println("this is the natio db for real" + hashC.get("nationality"));
                System.out.println("this is the email db for real" + hashC.get("maxa"));
                System.out.println("this is the natio db for real" + hashC.get("mina"));


                Log.d("request!", "starting");

                JSONObject json = jParserC.makeHttpRequest(
                        REGISTER_DATA, "POST", params);
                Log.d("get json array", json.toString());


                ldataC = json.getJSONArray(TAG_USERS);


                // looping through all users according to the json object returned
                for (int i = 0; i < ldataC.length(); i++) {
                    JSONObject c = ldataC.getJSONObject(i);

                    //gets the content of each tag
                    String username = c.getString(TAG_USERNAME);
                    String name = c.getString(TAG_NAME);
                    //   String age = c.getString(TAG_AGE);
                    String nationality = c.getString(TAG_NATIONALITY);

                    System.out.println("this is the username php: CONTAT " + username);
                    System.out.println("this is the name php:  C " + name);

                    //    System.out.println("this is the age php: C " + age);
                    System.out.println("this is the natio php: C " + nationality);
                    //  userNameArray.add(name);
                    userHash.put(name, username);



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return null;

        }

        protected void onPostExecute(String file_url) {

            listItems();

            pDialog.dismiss();


            if (file_url != null) {
                Toast.makeText(DisplayContact.this, file_url, Toast.LENGTH_LONG).show();
            }

        }




    }


}