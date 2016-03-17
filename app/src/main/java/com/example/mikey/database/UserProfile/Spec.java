package com.example.mikey.database.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandlerContacts;
import com.example.mikey.database.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Spec extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button search_btn,clear_btn;
    private EditText min_age,max_age, etEmail, etName;
    Spinner eduSpinner;
    private Spinner nationalityS;

    final String[] EDUCATION = {"Not stated", "Further Education", "Higher Education"};


    public String getUserEt() {
        return userEt;
    }

    public void setUserEt(String userEt) {
        this.userEt = userEt;
    }

    public String getNameEt() {
        return nameEt;
    }

    public void setNameEt(String nameEt) {
        this.nameEt = nameEt;
    }

    private String userEt;
    private String nameEt;
    private int age;

    public String getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(String minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(String maximumAge) {
        this.maximumAge = maximumAge;
    }

    private String minimumAge;
    private String maximumAge;

    public String getNationalityEt() {
        return nationalityEt;
    }

    public void setNationalityEt(String nationality) {
        this.nationalityEt = nationality;
    }

    private String nationalityEt;


    DatabaseHandlerContacts dbHandler;
    HashMap<String, String> hash;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_info);
        dbHandler = new DatabaseHandlerContacts(this);
        hash = dbHandler.getUserContacts();

        //Methods
        spinner_method();
        min_age = (EditText) findViewById(R.id.minage);
        max_age = (EditText) findViewById(R.id.maxage);
        etEmail = (EditText) findViewById(R.id.email_search);
        etName = (EditText) findViewById(R.id.name_search_info);
        spinnerCountries();//creates nationality spinner



        search_btn = (Button) findViewById(R.id.btn_search);
        clear_btn = (Button) findViewById(R.id.btn_clear);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                clearForm((ViewGroup) findViewById(R.id.testClear));



            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setNameEt(etName.getText().toString());
                setUserEt(etEmail.getText().toString());
                setMaximumAge(max_age.getText().toString());
                setMinimumAge(min_age.getText().toString());



                dbHandler.resetTablesContacts();
                dbHandler.addUserContacts(getNameEt(), null, getNationalityEt(), getUserEt(), getMaximumAge(),getMinimumAge(), null);




                Intent displaycontact = new Intent(getApplicationContext(), DisplayContact.class);
                startActivity(displaycontact);


            }
        });


    }



    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }
            if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0);
            }
            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }




    // this needs to be added to the database php
    public void spinner_method(){
        eduSpinner = (Spinner) findViewById(R.id.edu_spinner);



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EDUCATION);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        eduSpinner.setAdapter(adapter);



    }

    //TODO create algorithms to search for other users. Assign data into each object.
// gets nationality to search it - ---------------------------------

    public void spinnerCountries() {

        nationalityS = (Spinner) findViewById(R.id.nationalitySearch);
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
        String item = parent.getItemAtPosition(position).toString();
        setNationalityEt(item);

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
//---------------------------------------------------------------------------------------------

}
