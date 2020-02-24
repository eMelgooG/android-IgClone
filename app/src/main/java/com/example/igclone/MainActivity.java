package com.example.igclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.security.Key;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    Boolean signUpModeActive = true;
    TextView logInTextView;
EditText userNameEditText;
EditText passwordEditText;
Button signUpButton;
ConstraintLayout backgroundLayout;
ImageView logoImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoImageView = findViewById(R.id.logoImageView);
        backgroundLayout = findViewById(R.id.backgroundLayout);
        logInTextView = findViewById(R.id.loginTextView);
        signUpButton = findViewById(R.id.signUpButton);
        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        if(ParseUser.getCurrentUser()!=null) {      // if user is logged in when we open the app -> navigate to UserListActivity
            showUserList();
        }

        backgroundLayout.setOnClickListener(new View.OnClickListener() {                                               //Hide keyboard when touch the background
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });

        logoImageView.setOnClickListener(new View.OnClickListener() {                                    //Hide keyboard when touch the logo
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {                           //Pressing enter on the keyboard inside the password editext will automatically sign up or log in
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    signUp(v);
                }
return false; }
        });


    }


    public void showUserList() {            // navigate to UserListActivity class
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }

    public void login(View view) {                                                      // we switch the text of the sign up button and the login textview
            if(signUpModeActive){
                signUpModeActive = false;
                signUpButton.setText("Login");
                logInTextView.setText("or, Sign Up");
            } else {
                signUpModeActive = true;
                signUpButton.setText("Sign Up");
                logInTextView.setText("or, Login");
            }

    }

    public void signUp(View view) {
        if (userNameEditText.getText().length() == 0 || passwordEditText.getText().length()==0) {         // if either edittext is empty we pop up an error.
            Toast.makeText(getApplicationContext(), "A username and a password is required.", Toast.LENGTH_SHORT).show();
        } else {
            if(signUpModeActive) {                        //else if the signUpMode is true we try to register the user on the server
                ParseUser.getCurrentUser().logOut();
                String userName = userNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                ParseUser user = new ParseUser();
                user.setUsername(userName);
                user.setPassword(password);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {                                                      //no error -> SUCCESS
                            Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {                                                             // Else prompt the user with the error
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {                     //if signUpMode is false we try to log in the user
                //Login

                ParseUser.logInInBackground(userNameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null) {                            // If log in credientials are correct we log in the user and open call showUserList()
                            Toast.makeText(getApplicationContext(), "Log in succesfully.", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {                                            // else we prompt an error
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        }
    }

}
