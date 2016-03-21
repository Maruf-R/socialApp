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
import com.example.mikey.database.Database.DatabaseInterests;
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
 * Created by zapatacajas on 19/03/2016.
 */
public class Favourites extends AppCompatActivity {
 /*   private static final String GET_FRIEND = "http://www.companion4me.x10host.com/webservice/getfavourites.php";

    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "userdataC";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_NAME = "name";
    private static final String TAG_AVATAR = "avatar";

    ListView listview;
    JSONParser jParserC = new JSONParser();
    JSONArray ldataC = null;
    DatabaseHandlerContacts dbHandler;
    HashMap<String, String> hashC;
    HashMap<String, String> userHash;
    private ProgressDialog pDialog;
    DatabaseInterests dbInte;
    HashMap<String, String> hashInte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_results);

        dbHandler = new DatabaseHandlerContacts(this);
        hashC = dbHandler.getUserContacts();
        userHash = new HashMap<>();
        dbInte = new DatabaseInterests(this);
        hashInte = dbInte.getUserInterests();

        System.out.println("this is the name search obj: for real" + hashC.get("mina"));


        new Favourites.GetMatchedContacts().execute();

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
                dbHandler.addUserContacts(null, null, null, null, null, null, userHash.get(itemValue), null, null, null, null);


                Intent friend = new Intent(getApplicationContext(), ContactProfile.class);
                startActivity(friend);


            }
        });


    }

    public class GetMatchedContacts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Favourites.this);
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
                //  params.add(new BasicNameValuePair("username", hashC.get("email")));
                params.add(new BasicNameValuePair("name", hashC.get("name")));
                params.add(new BasicNameValuePair("nationality", hashC.get("nationality")));
                params.add(new BasicNameValuePair("maximum", hashC.get("maxa")));
                params.add(new BasicNameValuePair("minimum", hashC.get("mina")));
                params.add(new BasicNameValuePair("education", hashC.get("education")));
                params.add(new BasicNameValuePair("gender", hashC.get("gender")));
                params.add(new BasicNameValuePair("music", hashInte.get("music")));
                params.add(new BasicNameValuePair("movies", hashInte.get("movies")));
                params.add(new BasicNameValuePair("sports", hashInte.get("sports")));
                params.add(new BasicNameValuePair("food", hashInte.get("food")));
                params.add(new BasicNameValuePair("hobbies", hashInte.get("hobbies")));


//                params.add(new BasicNameValuePair("maximum", hashC.get("country")));
//                params.add(new BasicNameValuePair("minimum", hashC.get("city")));
// add if done


                System.out.println("this is the email db for real" +
                        hashInte.get("movies"));
                System.out.println("this is the natio db for real" + hashInte.get("food"));


                Log.d("request!", "starting");

                JSONObject json = jParserC.makeHttpRequest(
                        GET_FRIEND, "POST", params);
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
                Toast.makeText(Favourites.this, file_url, Toast.LENGTH_LONG).show();
            }

        }


    }*/
}