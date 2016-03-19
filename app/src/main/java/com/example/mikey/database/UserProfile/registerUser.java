package com.example.mikey.database.UserProfile;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikey.database.Database.DatabaseHandler;
import com.example.mikey.database.Database.DatabaseUsernameId;
import com.example.mikey.database.Database.JSONParser;
import com.example.mikey.database.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class registerUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHandler dbHandler;
    Session session = null;
   // Context context = null;
    EditText reciep, sub, msg;
    String rec, subject, textMessage;

    private EditText editFirstName;
    private EditText editLastName;
    private TextView birthday;
    private Calendar calendar;
    private int year, month, day;
    private Spinner nationalityS;
    private EditText password1;
    private EditText password2;
    private Button signUpBtn;
    private TextView errorMessage;
    private Spinner spinnerSecretQuestions;
    Spinner eduSpinner, genSpinner;


    final String[] EDUCATION = {"Not stated", "Further Education", "Higher Education"};
    final String[] GENDER = {"Not stated", "Male", "Female"};
    private String[] spinnerQuestion={"What was your first pet's name?",
   "What was your first car?",("What was your first love's name?"),("What was the city you were born?")};

    public String[] getSpinnerQuestion() { return spinnerQuestion; }

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
    private static final String LOGIN_URL ="http://www.companion4me.x10host.com/webservice/registerCode.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    public void setPassword(String password) {
        this.password = computeMD5Hash(password);
    }

    public String getPassword() {
        return password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String useremail) {
        this.verificationCode = computeMD5Hash(useremail).substring(0, Math.min(useremail.length(),8));
    }

    EditText answerEt;


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    private String question;


    DatabaseUsernameId dbHandlerId;


    private String verificationCode;

    // First Layout
    private RelativeLayout firstReg;
    private Button btnFirstRegNext, btnFirstRegBack;

    // Second Layout
    private RelativeLayout secondReg;
    private Button btnSecondRegBack, btnSecondRegNext;

    // Third Layout
    private RelativeLayout thirdReg;
    private Button btnThirdRegBack, btnThirdRegNext;

    // Fourth Layout
    private RelativeLayout fourthReg;
    private Button btnFourthRegBack, btnFourthSignUp;

    HashMap<String, String> hash;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        firstReg = (RelativeLayout)findViewById(R.id.firstRegLayout);
        secondReg = (RelativeLayout)findViewById(R.id.secondRegLayout);
       // thirdReg = (RelativeLayout)findViewById(R.id.thirdRegLayout);
        fourthReg = (RelativeLayout)findViewById(R.id.fourthRegLayout);
        firstReg.setVisibility(View.VISIBLE);
        secondReg.setVisibility(View.INVISIBLE);
      //  thirdReg.setVisibility(View.INVISIBLE);
        fourthReg.setVisibility(View.INVISIBLE);

        btnFirstRegBack = (Button) findViewById(R.id.btn1Back);
        btnFirstRegNext = (Button)findViewById(R.id.btn1Next);
        btnSecondRegBack = (Button)findViewById(R.id.btn2Back);
        btnSecondRegNext = (Button)findViewById(R.id.btn2Next);
      //  btnThirdRegBack = (Button)findViewById(R.id.btn3Back);
      //  btnThirdRegNext = (Button)findViewById(R.id.btn3Next);
        btnFourthRegBack = (Button)findViewById(R.id.btn4Back);

        btnFirstRegNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail();
                checkNames();
                validateAge();

                if(validateEmail() == true && checkNames() == true&&validateAge()==true) {
                    firstReg.setVisibility(View.INVISIBLE);
                    secondReg.setVisibility(View.VISIBLE);
                }
            }
        });
        btnFirstRegBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), Login.class);
                startActivity(login);
                finish();
            }
        });
        btnSecondRegNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                secondReg.setVisibility(View.INVISIBLE);
                fourthReg.setVisibility(View.VISIBLE);
            }
        });

        btnSecondRegBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondReg.setVisibility(View.INVISIBLE);
                firstReg.setVisibility(View.VISIBLE);
            }
        });

        btnFourthRegBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourthReg.setVisibility(View.INVISIBLE);
                secondReg.setVisibility(View.VISIBLE);
            }
        });

/*
        btnThirdRegNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdReg.setVisibility(View.INVISIBLE);
                fourthReg.setVisibility(View.VISIBLE);
            }
        });

*/

        signUpBtn = (Button) findViewById(R.id.btnSaveChanges);
        spinnerQuestion();
        spinnerCountries();
        spinner_methodGender();
        spinner_methodEdu();
        answerEt = (EditText) findViewById(R.id.answer_atregister);

        birthday = (TextView) findViewById(R.id.birthday);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        getTheAge(year, month, day);


        emailBox = (EditText) findViewById(R.id.emailBoxLogin);


        dbHandler = new DatabaseHandler(this);
        hash = dbHandler.getUserDetails();

       // dbHandlerId = new DatabaseUsernameId(this);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPasswords();
               if (checkPasswords() == true) {

                    String answer = answerEt.getText().toString();


                    String username = emailBox.getText().toString();
                    setVerificationCode(username);
                    String password3 = getPassword();
  System.out.println("username: " +username);
                    System.out.println("code short onc: " + getVerificationCode());
                    System.out.println("code: long onc" + computeMD5Hash(username).substring(0, Math.min(username.length(), 8)));



                    new CreateUserCode().execute(username, getVerificationCode());
System.out.println("for real spinner getquestion " + getQuestion());
                    dbHandler.resetTables();
                    dbHandler.addUser(getName(), Integer.toString(getAge()), getNationality(), getEmail(), password3, answer, getQuestion(), getEducation(), getGender(), null, null);//TODO to add county and city
    sendingEmail(username);


                }
            }


        });
    }

    public void sendingEmail(String username){
        rec = username;

        subject = "Companion4me verification code.";
        textMessage = "Thank you for using our App."+"\n"+ "Your verification code is: "+getVerificationCode()+"\n "+"Please insert this in the app verification window to register with us.";


        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("companion4meapp@gmail.com", "Companion4");
            }
        });

      //  pdialog = ProgressDialog.show(registerUser.this, "", "Sending Mail...", true);


        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();    }




    class RetreiveFeedTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }


        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("companion4meapp@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
               Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }


    class CreateUserCode extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(registerUser.this);
            pDialog.setMessage("Sending email...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            String username = args[0];
            String verificationcode = args[1];

            System.out.println("args0 in php" + username);
            System.out.println("args1 in php" + verificationcode);


            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("verificationcode", verificationcode));

                System.out.println("username: in php" + username);
                System.out.println("code in php" + verificationcode);
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();


            Intent i = new Intent(registerUser.this, EmailVerification.class);
            //  finish();
            startActivity(i);

            //  finish();

           // finish();
            if (file_url != null){
                Toast.makeText(registerUser.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

    //-------------------------------------------------------------------------------------------------------------

    /** UTILITY METHODS */

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





    public void spinnerQuestion() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerQuestion);
     //   dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSecretQuestions = (Spinner)findViewById(R.id.spinnerSecretQuestion);
        spinnerSecretQuestions.setAdapter(dataAdapter);
        spinnerSecretQuestions.setOnItemSelectedListener(this);


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
        else if(spinner.getId() == R.id.spinnerSecretQuestion)
        {
            String itemQ = parent.getItemAtPosition(position).toString();
            setQuestion(itemQ);

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
          //  errorMessage.setText("Name and Last name cannot be the same.");

            Toast.makeText(getApplicationContext(), "Name and Last name cannot be the same.", Toast.LENGTH_SHORT).show();
        }

        else if (validateNames(firstNameText) == false) {
            editFirstName.setError("Name must be between 1 and 15 characters, using letters only.");
            Toast.makeText(getApplicationContext(), "Name must be between 1 and 15 characters, using letters only.", Toast.LENGTH_SHORT).show();


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

    public boolean checkPasswords() {

        boolean valid = false;

        password1 = (EditText) findViewById(R.id.passwordBoxSignup1);
        password2 = (EditText) findViewById(R.id.passwordBoxSignup2);
        signUpBtn = (Button) findViewById(R.id.btnSaveChanges);
        errorMessage = (TextView) findViewById(R.id.error_message);


        String password1Text = password1.getText().toString();
        String password2Text = password2.getText().toString();


        if (password1Text.equals(password2Text) && validatePassword(password1Text) == true) {


            setPassword(password1.getText().toString());

            //signupOk();

            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            valid = true;

        }

        else if (validatePassword(password1Text) == false) {
            password1.setError("Password must be between 4 and 20 alphanumeric characters");
            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();


        }

        else if (!password1Text.equals(password2Text) && validatePassword(password1Text) == false) {

            errorMessage.setText("Passwords do not match, Please check and try again.");
            password1.setError("Password must be between 4 and 20 characters, including numbers and letters.");

            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
        }

        else {
            errorMessage.setText("Passwords do not match, Please check and try again.");

            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
        }

        return valid;

    }


    public boolean validatePassword(String passwordLength) {
        boolean valid = true;

        if (passwordLength.isEmpty() || passwordLength.length() < 4 || passwordLength.length() > 20) {

            valid = false;
        }



        return valid;
    }

    public boolean validateEmail() {
        boolean valid = true;

        emailBox = (EditText) findViewById(R.id.emailBoxSignup);
        String email = emailBox.getText().toString();
        setEmail(email);


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailBox.setError("Enter a valid email address");

            valid = false;

        }
        else{
            emailBox.setError(null);
        }


        return valid;


    }

    public String computeMD5Hash(String password) {

        StringBuffer MD5Hash = new StringBuffer();
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

            Log.d("The hash is: ", String.valueOf(MD5Hash));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return MD5Hash.toString();
    }
}

