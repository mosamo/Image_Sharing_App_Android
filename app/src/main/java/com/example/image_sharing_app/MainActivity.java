package com.example.image_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {
    
    EditText userText;
    EditText passText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
        }
    
        
    
        userText = findViewById(R.id.userText);
        passText = findViewById(R.id.passText);
    
        ParseObject parseObject = new ParseObject("User");
    
        parseObject.put("username", "babobsda");
        parseObject.saveInBackground();
    
        ConstraintLayout con_m = findViewById(R.id.conLayMain);
                         con_m.setOnClickListener(this);
                         con_m.setSoundEffectsEnabled(false);
                         // Removes Clicking sound Effect
    }
    
    
    
    public void signupButton(View view) {
        
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
        }
        
        ParseUser newAcc = new ParseUser();
        
        newAcc.setUsername(userText.getText().toString());
        newAcc.setPassword(passText.getText().toString());
        
        if (isEmpty(userText) || isEmpty(passText)) {
            Toast.makeText(this, "Please Fill Required Fields", Toast.LENGTH_SHORT).show();
        } else {
            
            toggleButtons(false);
            
            final Toast toast = Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT);
            toast.show(); // starting "Loading.." toast
            
            
            newAcc.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        
                        toast.cancel(); // canceling "Loading.." toast
                        Toast.makeText(getApplicationContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                        
                        
                        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                        startActivity(intent);
                        finish(); // killing MainActivity so the user doesn't go back
                        
                    } else {
                        toast.cancel();
                        
                        Toast.makeText(getApplicationContext(), "Sign Up Failed: Account Already Exists", Toast.LENGTH_SHORT).show();
                        Log.i("SignUpFailed", e.toString());
                        
                        toggleButtons(true);
                    }
                }
            });
        }
        
        // Hide Keyboard
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(userText.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(passText.getWindowToken(), 0);
    }
    
    public void loginButton(View view) {
        
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
        }
        
        // initiating them so they are not null
        // probably this is not required but whatever
        
        String userValue = "";
        String passValue = "";
        
        ParseUser loggedAcc = new ParseUser();
        
        if (isEmpty(userText) || isEmpty(passText)) {
            Toast.makeText(this, "Please Fill Required Fields", Toast.LENGTH_SHORT).show();
        } else {
            
            toggleButtons(false);
            
            final Toast toast = Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT);
            toast.show(); // starting "Loading.." toast
            
            userValue = userText.getText().toString();
            passValue = passText.getText().toString();
            
            loggedAcc.logInInBackground(userValue, passValue, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        
                        toast.cancel();
                        Toast.makeText(getApplicationContext(), "Log In Successful", Toast.LENGTH_SHORT).show();
                        
                        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                        startActivity(intent);
                        
                        finish();
                    } else {
                        toast.cancel();
                        
                        Toast.makeText(getApplicationContext(), "Login Failed: Bad Username or Password", Toast.LENGTH_SHORT).show();
                        Log.i("LoginFailed", e.toString());
                        toggleButtons(true);
                    }
                }
            });
        }
        
        // Hide Keyboard
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(userText.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(passText.getWindowToken(), 0);
        
    }
    
    private boolean isEmpty(EditText editText) {
        if (editText.getText().toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public void toggleButtons(boolean value) {
        final Button loginButton = findViewById(R.id.loginButton);
        final Button signupButton = findViewById(R.id.signupButton);
        
        loginButton.setClickable(value);
        loginButton.setEnabled(value);
        signupButton.setClickable(value);
        signupButton.setEnabled(value);
    }
    
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (v.getId() == R.id.loginButton) {
                
                // already works for some reason
            }
        }
        return false;
    }
    
    @Override
    public void onClick(View v) {
        // if anything is else clicked, hide keyboard
        //
        // TODO:
        //       HERE WE ARE ONLY COMPARING IDS
        //       DO NOT USE findViewById();
        if (v.getId() == R.id.conLayMain || v.getId() == R.id.conLay1 || v.getId() == R.id.conLay ||
                v.getId() == R.id.header || v.getId() == R.id.footer) {
            InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            
            // TODO: Instead of these:
            /*
            mgr.hideSoftInputFromWindow(userText.getWindowToken(), 0);
            mgr.hideSoftInputFromWindow(passText.getWindowToken(), 0);
            */
            // TODO: Use this:
            mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
