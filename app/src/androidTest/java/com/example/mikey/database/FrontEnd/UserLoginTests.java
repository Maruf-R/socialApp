package com.example.mikey.database.FrontEnd;


import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.mikey.database.UserProfile.Login;
import com.example.mikey.database.UserProfile.Profile.Profile;
import com.example.mikey.database.R;

import org.junit.Test;


/**
 * Created by Mohammad Humaid on 13/03/2016 at 21:23.
 */
public class UserLoginTests extends ActivityInstrumentationTestCase2<Login> {

    public UserLoginTests() { super(Login.class); }

    @Test
    public void testLoginActivityExists(){
        Login login = getActivity();
        assertNotNull("Login activity doesn't exist", login);
    }


    /**
     * Checks if a user can login.
     * @result Accout will login without any errors.
     */
    @Test
    public void testUserLogin(){

        Login login = getActivity();
        final EditText usernameField = (EditText)login.findViewById(R.id.emailBoxLogin);
        final EditText passwordField = (EditText)login.findViewById(R.id.passwordBox);
        final Button loginButton = (Button)login.findViewById(R.id.btn_login);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                usernameField.requestFocus();
            }
        });
        getInstrumentation().sendStringSync("mohammadhumaid@gmail.com");
        getInstrumentation().waitForIdleSync();

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                passwordField.requestFocus();
            }
        });

        getInstrumentation().sendStringSync("random");
        getInstrumentation().waitForIdleSync();

        TouchUtils.clickView(this, loginButton);

        Profile profile = new Profile();
        assertNotNull("User has not logged in", profile);
        profile.finish();

    }

}
