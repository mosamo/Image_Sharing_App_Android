package com.example.image_sharing_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {
    
    String username;
    TextView pane;
    ArrayAdapter arrayAdapter;
    final ArrayList<String> userList = new ArrayList<String>();
    ListView friendList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends); // links xml to this activity
        
        username = ParseUser.getCurrentUser().getUsername();
        
        // upper widget
        pane = findViewById(R.id.userPane);
        pane.setText(username + " account:");
        
        // greetings bar
        TextView temp = findViewById(R.id.greeting);
        temp.setText(username + "'s Friend list:");
        
        // ArrayAdapter boiler
        arrayAdapter = new ArrayAdapter(this, R.layout.hopefully_white, userList);
        friendList = findViewById(R.id.friendList);
        friendList.setAdapter(arrayAdapter);
    
        // query boiler
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", username);
        
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseUser foundUser : objects) {
                            userList.add("\u2022 " + foundUser.getUsername());
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.i("QueryError", e.toString());
                }
            }
        });
    }
    
    public void signOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        
        finish();
        
        ParseUser.logOut();
    }
    
    public void feedList(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        
        finish();
    }
}
