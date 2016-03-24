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
import android.widget.ListView;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandlerContacts;
import com.example.mikey.database.Database.DatabaseInterests;
import com.example.mikey.database.Database.DatabaseUsernameId;
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
   private static final String GET_FRIEND = "http://www.companion4me.x10host.com/webservice/getfavourites.php";

    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "userdataC";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_NAME = "name";
    private static final String TAG_AVATAR = "avatar";

    JSONParser jParserC = new JSONParser();
    JSONArray ldataC = null;
    DatabaseUsernameId dbHandlerId;
    HashMap<String,String> idUserHash;

    DatabaseHandlerContacts dbHandler;
    HashMap<String, String> hashC;
    HashMap<String, String> userHash;
    private ProgressDialog pDialog;
    DatabaseInterests dbInte;
    HashMap<String, String> hashInte;
    HashMap<String, String> hashAvatar;
    HashMap<String, String> hashAge;
Button upd;

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
        setContentView(R.layout.activity_contact_list);
        dbHandlerId = new DatabaseUsernameId(this);
        idUserHash = dbHandlerId.getUserDetails();
        dbHandler = new DatabaseHandlerContacts(this);
        hashC = dbHandler.getUserContacts();

        dbInte = new DatabaseInterests(this);
        hashInte = dbInte.getUserInterests();

        upd = (Button) findViewById(R.id.update_results);
       new Favourites.GetMatchedContacts().execute();

       // System.out.println("this is the name search obj: for real" + hashC.get("mina"));

        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Favourites.GetMatchedContacts().execute();


            }
        });


    }

    ListView list;
    public void listItems(){

        final ArrayList<String> textViewObjects = new ArrayList<String>();
        final ArrayList<Integer> imgIdArray = new ArrayList<>();
        final ArrayList<String> ageIdArray = new ArrayList<>();


        Iterator it = userHash.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //  System.out.println(pair.getKey().toString() + " = " + pair.getValue().toString());

            textViewObjects.add(pair.getKey().toString());
            int imgId = getResources().getIdentifier(hashAvatar.get(pair.getValue().toString()), "drawable", getPackageName());

            String hAge= hashAge.get(pair.getValue().toString());
            ageIdArray.add(hAge);
            imgIdArray.add(imgId);

        }




        CustomListAdapter adapter=new CustomListAdapter(this, textViewObjects, imgIdArray, ageIdArray);
        list=(ListView)findViewById(R.id.contacts_result);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) list.getItemAtPosition(position);


                System.out.println("it worked name" + userHash.get(itemValue));
                System.out.println("it worked username" + userHash.get(itemValue));


                dbHandler.resetTablesContacts();
                dbHandler.addUserContacts(null, null, null, null, null, null, userHash.get(itemValue),null,null,null,null);


                Intent friend = new Intent(getApplicationContext(), ContactProfile.class);
                startActivity(friend);


            }
        });





    }

    public class GetMatchedContacts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            userHash = new HashMap<>();
            hashAvatar = new HashMap<>();
            hashAge = new HashMap<>();
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
                params.add(new BasicNameValuePair("username", idUserHash.get("email")));




                Log.d("request!", "starting");

                JSONObject json = jParserC.makeHttpRequest(
                        GET_FRIEND, "POST", params);
                Log.d("get json array", json.toString());


                ldataC = json.getJSONArray(TAG_USERS);


                // looping through all users according to the json object returned
                for (int i = 0; i < ldataC.length(); i++) {
                    JSONObject c = ldataC.getJSONObject(i);

                    //gets the content of each tag
                    String username = c.getString("favourites");
                    String name = c.getString(TAG_NAME);
                      String age = c.getString("age");
                  String avatar = c.getString("avatar");


                   userHash.put(name, username);
                    hashAvatar.put(username,avatar);
                    hashAge.put(username,age);

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


    }
}