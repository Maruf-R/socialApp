package com.example.mikey.database.UserProfile.Profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandler;
import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;
import com.example.mikey.database.UserProfile.Home;
import com.example.mikey.database.UserProfile.Password.NewPassword;
import com.example.mikey.database.UserProfile.UserInterests;
import com.example.mikey.database.UserProfile.registerUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.mail.Session;

public class EditUserProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

  //  private EditText editFirstName, editLastName, editSecretAnswer;
    private TextView textBirthday;
    private Button  btnSaveChanges;
    private registerUser user;

    EditText reciep, sub, msg;
    String rec, subject, textMessage;

    private EditText editFirstName;
    private EditText editLastName;
    private TextView birthday;
    private Calendar calendar;
    private int year, month, day;
    private Spinner nationalityS;

//    private TextView errorMessage;

    Spinner eduSpinner, genSpinner;


    final String[] EDUCATION = {"Not stated", "Further Education", "Higher Education"};
    final String[] GENDER = {"Not stated", "Male", "Female"};



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
    private String country,city;

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

    private EditText emailBox;
    private String password;

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    private String nationality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private int age;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL ="http://www.companion4me.x10host.com/webservice/editRegister.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";





    DatabaseUsernameId dbHandlerId;



    HashMap<String, String> hash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        dbHandlerId = new DatabaseUsernameId(this);
        hash = dbHandlerId.getUserDetails();

        basicSetup();
    }

    private void basicSetup() {

        btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);


        spinnerCountries();
        spinner_methodGender();
        spinner_methodEdu();

        birthday = (TextView) findViewById(R.id.birthday);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        getTheAge(year, month, day);


        emailBox = (EditText) findViewById(R.id.emailBoxLogin);

        // dbHandlerId = new DatabaseUsernameId(this);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNames();
                validateAge();
                if (checkNames() == true&&validateAge()==true) {



   new EditUserProfile.EditUser().execute();

                }
            }


        });





    }

///////////////////////////////////////////////////////////////// php

    class EditUser extends AsyncTask<String, String, String> {


        //  boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditUserProfile.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", hash.get("email")));
                params.add(new BasicNameValuePair("name", getName()));
                params.add(new BasicNameValuePair("age", Integer.toString(getAge())));
                params.add(new BasicNameValuePair("nationality", getNationality()));
                params.add(new BasicNameValuePair("education", getEducation()));
                params.add(new BasicNameValuePair("gender", getGender()));
//                params.add(new BasicNameValuePair("maximum", hashC.get("country")));
//                params.add(new BasicNameValuePair("minimum", hashC.get("city")));
// add if done

                System.out.println("they pass to email ver gender" +  hash.get("email"));


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

                    Intent intent = new Intent(EditUserProfile.this, Home.class);
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
                Toast.makeText(EditUserProfile.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

//////////////////////////////////////////////////////////////////////































    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Please, Enter your date of birth.", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

            showDate(arg1, arg2 + 1, arg3);
            getTheAge(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        birthday.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

    }

    // gets the age of the user,
    private int getTheAge(int year, int month, int day)
    {
        LocalDate birthday = new LocalDate (year, month, day);          //Birth date
        LocalDate now = new LocalDate();                    //Today's date
        Period period = new Period(birthday, now, PeriodType.yearMonthDay());

        System.out.println("days are"+period.getDays());
        System.out.println("months are"+period.getMonths());
        System.out.println("years are" + period.getYears());
        age = period.getYears();
        setAge(age);
        return age;
    }
    //validates the age of the user
    public boolean validateAge(){

        boolean valid = true;
        if( age<18){
            birthday.setError("You must be at least 18 years old to Sign Up");
            //  Toast.makeText(getApplicationContext(), "You must be at least 18 years old to Sign Up", Toast.LENGTH_SHORT).show();
            System.out.println("your age is " + age);
            valid = false;
        }
        else{
            birthday.setError(null);
        }
        return valid;
    }







    public void spinner_methodGender(){
        genSpinner = (Spinner) findViewById(R.id.gender_spinner_register);
        genSpinner.setOnItemSelectedListener(this);
        List<String> genderSpinnerA = new ArrayList<String>();



        for (String countryCode : GENDER) {


            genderSpinnerA.add(countryCode);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderSpinnerA);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        genSpinner.setAdapter(adapter);






    }


    // this needs to be added to the database php
    public void spinner_methodEdu(){
        eduSpinner = (Spinner) findViewById(R.id.edu_spinner_register);
        eduSpinner.setOnItemSelectedListener(this);
        List<String> eduSpinnerA = new ArrayList<String>();



        for (String countryCode : EDUCATION) {


            eduSpinnerA.add(countryCode);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eduSpinnerA);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        eduSpinner.setAdapter(adapter);



    }

    public void spinnerCountries() {

        nationalityS = (Spinner) findViewById(R.id.Nationality);
        nationalityS.setOnItemSelectedListener(this);

        List<String> countriesSpinner = new ArrayList<String>();

        String[] locales = Locale.getISOCountries();

        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);
            countriesSpinner.add(obj.getDisplayCountry());
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countriesSpinner);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        nationalityS.setAdapter(dataAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item




        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.Nationality)
        {
            String item = parent.getItemAtPosition(position).toString();
            setNationality(item);

        }
         else if(spinner.getId() == R.id.edu_spinner_register)
        {

            String edu = parent.getItemAtPosition(position).toString();
            setEducation(edu);
            Toast.makeText(parent.getContext(), "Selected: " + edu, Toast.LENGTH_LONG).show();
        }
        else if(spinner.getId() == R.id.gender_spinner_register)
        {

            String gender = parent.getItemAtPosition(position).toString();
            setGender(gender);
            Toast.makeText(parent.getContext(), "Selected: " + gender, Toast.LENGTH_LONG).show();
        }



        // Showing selected spinner item
        //    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public boolean checkNames() {

        editFirstName = (EditText) findViewById(R.id.txtFirstName);
        editLastName = (EditText) findViewById(R.id.txtLastName);
        boolean valid = true;

        String firstNameText = editFirstName.getText().toString();
        String lastNameText = editLastName.getText().toString();

        setName(firstNameText+" "+lastNameText);

        if (firstNameText.equals(lastNameText)) {
         //   errorMessage.setText("Name and Last name cannot be the same.");

            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
        }

        else if (validateNames(firstNameText) == false) {
            editFirstName.setError("Name must be between 1 and 15 characters, using letters only.");
            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();


        }

        else if (validateNames(lastNameText) == false) {

            editLastName.setError("Last name must be between 1 and 15 characters, using letters only.");

            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
        }

        return valid;

    }

    public boolean validateNames(String namesLength) {
        boolean valid = true;

        if (namesLength.isEmpty() || namesLength.length() < 1 || namesLength.length() > 16 || !namesLength.matches("[a-zA-Z]+")) {

            valid = false;
        }
        return valid;
    }




}
