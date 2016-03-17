package com.example.mikey.database.BackEnd;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.junit.Test;

import com.example.mikey.database.UserProfile.Login;

/**
 * Created by Mohammad Humaid on 14/03/2016 at 10:56.
 */
public class LoginTest extends ApplicationTestCase<Application> {

    public LoginTest() { super(Application.class); }

    @Test
    public void testValidatePassword() {
        Login login = new Login();
        boolean password = login.validatePassword("password");
        assertEquals("Test failed", true, password);
    }


}
