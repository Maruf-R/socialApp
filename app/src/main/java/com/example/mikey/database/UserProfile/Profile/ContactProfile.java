package com.example.mikey.database.UserProfile.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandlerContacts;
import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.VoiceCall.AudioPlayer;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactProfile extends AppCompatActivity {
    private static final String APP_KEY = "ef1889da-3241-4700-a675-200b6b15fecd";
    private static final String APP_SECRET = "fsNJI+ziSkG1d+rDsW84wA==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    private static final String REGISTER_DATA = "http://www.companion4me.x10host.com/webservice/registerdata.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "userdata";
    private static final String TAG_AGE = "age";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_NAME = "name";
    private static final String TAG_NATIONALITY = "nationality";

    DatabaseUsernameId dbHandlerId;
    HashMap<String,String> idUserHash;
    TextView nameF;
    TextView ageF;
    TextView nationalityF;
    JSONParser jParser = new JSONParser();
    JSONArray ldata = null;
    DatabaseHandlerContacts dbHandler;
    HashMap<String, String> hashC;
    ImageButton callUser, endCall;
    RelativeLayout contactProf;
    LinearLayout callWin;
    SinchCallListener callListener;
    private String _username;
    private String _name;
    private String _age;
    private String _nationality;
    private Call call;
    private ProgressDialog pDialog;

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String education,gender;
    private String music;
    private String food;
    private String hobbies;
    private String sports;
    private String biography;
    private String movies;
    private TextView educationv,genderv;
    private TextView musicv;
    private TextView foodv;
    private TextView hobbiesv;
    private TextView sportsv;
    private TextView biographyv;
    private TextView moviesv;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String avatar;
    private ImageView avatarv;



    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getMovies() {
        return movies;
    }

    public void setMovies(String movies) {
        this.movies = movies;
    }


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


  AudioPlayer ap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);


        ap = new AudioPlayer(this);
        dbHandlerId = new DatabaseUsernameId(this);

        idUserHash = dbHandlerId.getUserDetails();


        contactProf = (RelativeLayout) findViewById(R.id.contact_layout);

        callWin = (LinearLayout) findViewById(R.id.call_window);


        callUser = (ImageButton) findViewById(R.id.btnCallUser);
        endCall = (ImageButton) findViewById(R.id.end_call);


        dbHandler = new DatabaseHandlerContacts(this);
        hashC = dbHandler.getUserContacts();

        // put final may cause error
       final SinchClient sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
               .userId(idUserHash.get("email"))
               .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();

        new ContactProfile.GetTheProfileData().execute();


        sinchClient.setSupportCalling(true);

     /*   sinchClient.startListeningOnActiveConnection();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
*/
        sinchClient.start();

        callListener = new SinchCallListener();
        callUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               contactProf.setVisibility(View.INVISIBLE);
                callWin.setVisibility(View.VISIBLE);

                call = sinchClient.getCallClient().callUser(hashC.get("namef"));
               /////// call = sinchClient.getCallClient().callUser(hashC.get("namef"));

                call.addCallListener(callListener);




                /*else {
                    call.hangup();
                    call = null;
                    callUser.setText("Call");
                }*/
            }
        });
        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.hangup();

                call.addCallListener(callListener);
                callWin.setVisibility(View.INVISIBLE);
                contactProf.setVisibility(View.VISIBLE);


                Toast.makeText(ContactProfile.this,"Call ended by you." , Toast.LENGTH_LONG).show();

            }
        });


    }

    class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            ap.stopProgressTone();
            callWin.setVisibility(View.INVISIBLE);
            contactProf.setVisibility(View.VISIBLE);
            Toast.makeText(ContactProfile.this,"Call ended by your friend."+ "SHOULD I PUT USER'S NAME?." , Toast.LENGTH_LONG).show();

            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }
        @Override
        public void onCallEstablished(Call establishedCall) {
            ap.stopProgressTone();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            Toast.makeText(ContactProfile.this,"Conected" , Toast.LENGTH_LONG).show();
            ap.stopProgressTone();


        }
        @Override
        public void onCallProgressing(Call progressingCall) {
            //call is ringing
            ap.playProgressTone();
        }
        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }

    public class GetTheProfileData extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ContactProfile.this);
            pDialog.setMessage("Loading Contact...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {

            try {
// namef is the email(username) of the friend
                System.out.println("this is the email db " + hashC.get("email"));
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", hashC.get("namef")));
                System.out.println("username of friend" + hashC.get("namef") );

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
                    setFood(c.getString("food"));

                    setEducation(c.getString("education"));
                    setGender(c.getString("gender"));
                    setHobbies(c.getString("hobbies"));
                    setMovies(c.getString("movies"));
                    setMusic(c.getString("music"));
                    setBiography(c.getString("description"));
                    setSports(c.getString("sports"));
                    setAvatar(c.getString("avatar"));

//                    setCountry(c.getString("country"));
//                    setCity(c.getString("city"));
//






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
            nameF = (TextView) findViewById(R.id.txtUserName);
            ageF = (TextView) findViewById(R.id.txtUserAge);
            nationalityF = (TextView) findViewById(R.id.txtNationFriend);
            educationv= (TextView) findViewById(R.id.txtUserEducationFriend);
            genderv= (TextView) findViewById(R.id.txtUserGender);
            musicv= (TextView) findViewById(R.id.fmusic);
            foodv= (TextView) findViewById(R.id.ffood);
            hobbiesv= (TextView) findViewById(R.id.fhobbies);
            sportsv= (TextView) findViewById(R.id.fsports);
            biographyv= (TextView) findViewById(R.id.fbio);
            moviesv= (TextView) findViewById(R.id.fmovies);
            avatarv= (ImageView) findViewById(R.id.imgUserAvatar);

            int imgId = getResources().getIdentifier(getAvatar(), "drawable", getPackageName());
            avatarv.setImageResource(imgId);



            educationv.setText(getEducation());
            genderv.setText(getGender());
            musicv.setText(getMusic());
            foodv.setText(getFood());
            hobbiesv.setText(getHobbies());
            sportsv.setText(getSports());
            biographyv.setText(getBiography());
            moviesv.setText(getMovies());
            nameF.setText(get_name());
            ageF.setText(get_age());
            nationalityF.setText(get_nationality());

            System.out.println("this is the name l: " + get_name());

            System.out.println("this is the age l: " + get_age());
            System.out.println("this is the natio l: " + get_nationality());


            pDialog.dismiss();
            contactProf.setVisibility(View.VISIBLE);


            if (file_url != null){
                Toast.makeText(ContactProfile.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }
}
