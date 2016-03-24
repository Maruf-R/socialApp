package com.example.mikey.database.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseCity;
import com.example.mikey.database.Database.DatabaseHandlerContacts;
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
 * Created by zapatacajas on 22/03/2016.
 */
public class NearPeople extends AppCompatActivity {

    private static final String REGISTER_DATA = "http://www.companion4me.x10host.com/webservice/getNearPeople.php";
    private static final String GET_BLOCKED = "http://www.companion4me.x10host.com/webservice/getblocked.php";
    private static final String GET_BLOCKEDME = "http://www.companion4me.x10host.com/webservice/getwhoblockedme.php";


    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "userdataC";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_NAME = "name";
    private static final String TAG_NATIONALITY = "nationality";
    JSONParser jParserC = new JSONParser();
    JSONArray ldataC = null;

    DatabaseCity dbcity;
    HashMap<String, String> hashcity;

    DatabaseHandlerContacts dbHandler;
     DatabaseUsernameId dbId;


    HashMap<String, String> userHash;
    private ProgressDialog pDialog;
    HashMap<String, String> hashId;
    HashMap<String, String> hashAvatar;
    HashMap<String, String> hashAge;


    ArrayList<String> arrayIB;
    ArrayList<String> arrayBM;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_results);
        dbcity = new DatabaseCity(this);
        hashcity = dbcity.getCity();
        System.out.println("city oncreate"+hashcity.get("city"));

        dbId = new DatabaseUsernameId(this);
        hashId= dbId.getUserDetails();
        dbHandler = new DatabaseHandlerContacts(this);
        userHash = new HashMap<>();
        hashAvatar = new HashMap<>();
        hashAge = new HashMap<>();
        arrayIB = new ArrayList<>();
        arrayBM = new ArrayList<>();





        new NearPeople.GetMatchedContacts().execute();

    }



    ListView list;
    public void listItems(){

        final ArrayList<String> textViewObjects = new ArrayList<String>();
        final ArrayList<Integer> imgIdArray = new ArrayList<>();
        final ArrayList<String> ageIdArray = new ArrayList<>();

       /* Iterator<String> IB = arrayIB.iterator();
        Iterator<String> BM = arrayBM.iterator();*/

/*
        for(String list2Val : ){
            for(String list1Val : myList1){
                for (String list3Val : myList3) {
                    if(list3Val.equalsIgnoreCase(list1Val) && list3Val.equalsIgnoreCase(list2Val)){
                        commonList.add(list3Val);
                    }
                }

            }
        }
*/

        Iterator it = userHash.entrySet().iterator();
        if(!arrayIB.isEmpty() && !arrayBM.isEmpty()) {
            for (String list2Val : arrayIB) {
                for (String list1Val : arrayBM) {
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        if (!pair.getValue().toString().equals(list1Val) && !pair.getValue().toString().equals(list2Val)) {


                            textViewObjects.add(pair.getKey().toString());
                            int imgId = getResources().getIdentifier(hashAvatar.get(pair.getValue().toString()), "drawable", getPackageName());
                            String hAge = hashAge.get(pair.getValue().toString());
                            ageIdArray.add(hAge);
                            imgIdArray.add(imgId);
                            System.out.println("while " + pair.getValue().toString());
                            System.out.println("bm " + list1Val);
                            System.out.println("ib " + list2Val);


                        }

                    }


                }
            }

        }

else if(arrayIB.isEmpty()) {

        for (String list1Val : arrayBM) {
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (!pair.getValue().toString().equals(list1Val)) {


                    textViewObjects.add(pair.getKey().toString());
                    int imgId = getResources().getIdentifier(hashAvatar.get(pair.getValue().toString()), "drawable", getPackageName());
                    String hAge = hashAge.get(pair.getValue().toString());
                    ageIdArray.add(hAge);
                    imgIdArray.add(imgId);
                    System.out.println("while " + pair.getValue().toString());
                    System.out.println("bm " + list1Val);



                }

            }


        }
    }


      else  if(arrayBM.isEmpty()) {
            for (String list2Val : arrayIB) {

                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        if (!pair.getValue().toString().equals(list2Val)) {


                            textViewObjects.add(pair.getKey().toString());
                            int imgId = getResources().getIdentifier(hashAvatar.get(pair.getValue().toString()), "drawable", getPackageName());
                            String hAge = hashAge.get(pair.getValue().toString());
                            ageIdArray.add(hAge);
                            imgIdArray.add(imgId);
                            System.out.println("while " + pair.getValue().toString());

                            System.out.println("ib " + list2Val);


                        }




                }
            }

        }

else{

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();



                    textViewObjects.add(pair.getKey().toString());
                    int imgId = getResources().getIdentifier(hashAvatar.get(pair.getValue().toString()), "drawable", getPackageName());
                    String hAge = hashAge.get(pair.getValue().toString());
                    ageIdArray.add(hAge);
                    imgIdArray.add(imgId);
                    System.out.println("while " + pair.getValue().toString());



                }
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

    ///Load contacts method
   public void getMatchedContactsMethod() {

       try {


           List<NameValuePair> params = new ArrayList<NameValuePair>();
           params.add(new BasicNameValuePair("city", hashcity.get("city")));

           System.out.println("this is the city db for real new" +
                   hashcity.get("city"));


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
               String age = c.getString("age");

               String avatar = c.getString("avatar");

               //  setAvatar(avatar);

               System.out.println("this is the age tesetinh" + age);
               System.out.println("this is the name php:  C " + name);


               userHash.put(name, username);
               hashAvatar.put(username, avatar);
               hashAge.put(username, age);

           }
       } catch (JSONException e) {
           e.printStackTrace();
       }

   }
       /// gets contacts you have blocked
       public void getBlockedContactsMethod(){
        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", hashId.get("email")));
            Log.d("request!", "starting");

            JSONObject json = jParserC.makeHttpRequest(
                    GET_BLOCKED, "POST", params);
            Log.d("get json array", json.toString());


            ldataC = json.getJSONArray(TAG_USERS);

            for (int i = 0; i < ldataC.length(); i++) {
                JSONObject c = ldataC.getJSONObject(i);

                //gets the content of each tag
                String usernameIB = c.getString("blocked");
              /*  String name = c.getString(TAG_NAME);
                String age = c.getString("age");
                String avatar = c.getString("avatar");*/

                arrayIB.add(usernameIB);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //get contacts blocked me
    public void getContactsWhoBlockedMe(){
        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("blocked", hashId.get("email")));
            Log.d("request!", "starting");

            JSONObject json = jParserC.makeHttpRequest(
                    GET_BLOCKEDME, "POST", params);
            Log.d("get json array", json.toString());


            ldataC = json.getJSONArray(TAG_USERS);

            for (int i = 0; i < ldataC.length(); i++) {
                JSONObject c = ldataC.getJSONObject(i);

                //gets the content of each tag
                String usernameBM = c.getString("username");
             /*   String name = c.getString(TAG_NAME);
                String age = c.getString("age");
                String avatar = c.getString("avatar");*/

                arrayBM.add(usernameBM);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }









    public class GetMatchedContacts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NearPeople.this);
            pDialog.setMessage("Loading Contatcts Found...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {


            getMatchedContactsMethod();
            getBlockedContactsMethod();
            getContactsWhoBlockedMe();

            return null;


        }

        protected void onPostExecute(String file_url) {

            listItems();

            pDialog.dismiss();


            if (file_url != null) {
                Toast.makeText(NearPeople.this, file_url, Toast.LENGTH_LONG).show();
            }

        }




    }




}
