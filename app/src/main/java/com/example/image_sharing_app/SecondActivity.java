package com.example.image_sharing_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    
    //TODO: Setup ParseSetting on a New Installation
    //      Remove API Key and other Specifics When Sharing the Build
    
    

    
    
    String username;
    TableLayout tabitha;
    
    //TODO: IMPORTANT: saving the ParseObject records here, so we don't fetch them each time
    ArrayList<ParseObject> feed_records = new ArrayList<ParseObject>();
    
    ArrayList<String> names = new ArrayList<String>();
    
    // need this bitmap globally;
    Bitmap bitmap = null;
    
    ArrayList<ImageView> arrayImages = new ArrayList<ImageView>();
    int imageIndex = 0;
    
    static Bitmap transferredImage = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        
        username = ParseUser.getCurrentUser().getUsername();
        
        tabitha = findViewById(R.id.tabitha);
        
        TextView pane = findViewById(R.id.userPane);
        TextView temp = findViewById(R.id.greeting);
        
        pane.setText(username + " account:");
        temp.setText("Welcome to your Activity Feed, " + username + "!");
        
        
        getLatestInfo(10);
    }
    
    public void signOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        
        finish();
        
        ParseUser.logOut();
    }
    
    public void friendsList(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
        
        finish();
    }
    
    public void pressUpload(View view) {
        uploadPhoto();
    }
    
    public void renderNewsFeed() {
        
        //TODO
        // Selection Sort - Selection Sort - Selection Sort - Selection Sort
        // *****************************************************************************************
        if (feed_records.size() > 0) {
            for (int i = 0; i < feed_records.size(); i++) {
        
                ParseObject current = feed_records.get(i);
                ParseObject hold;
        
                for (int j = i + 1; j < feed_records.size(); j++) {
            
                    // TODO: IMPORTANT: YOU MUST USE ".equals" for OBJECTS and "==" for PRIMITIVES (int, boolean.. etc)
                    //       ParseObjects are almost never equal each other because of primary key
            
                    if (current.getString("username").equals(feed_records.get(j).getString("username"))) {
                
                        hold = feed_records.get(j);
                        feed_records.set(j, feed_records.get(i + 1));
                        feed_records.set(i + 1, hold);
                
                    }
                }
            }
            //TODO: ************************************************************************************
    
            // Name Logger: essentially
            for (ParseObject object : feed_records) {
                names.add(object.getString("username"));
            }
    
    
            final ArrayList<Integer> instance_of_each_name = new ArrayList<Integer>(); // saves how many copy of each name there is
            ArrayList<String> namesUnique = new ArrayList<String>(); // saves names without duplication
    
            namesUnique.add(names.get(0)); // add first name, because of course its unique
            instance_of_each_name.add(1); // add one counter for the first name, because of course its unique
    
            int temp = 0; // used to save index for unique instances checker, to go and add 1 to it
            //instance of each name checker, should give something like [2,4,1,2]
            for (int i = 1; i < names.size(); i++) {
                if (names.get(i).equals(names.get(i - 1))) {
                    int holder = instance_of_each_name.get(temp);
                    instance_of_each_name.set(temp, holder + 1);
                } else {
                    temp++;
                    instance_of_each_name.add(1);
                    namesUnique.add(names.get(i));
                }
            }
    
            Log.i("UniqueNameList", namesUnique.toString());
            Log.i("Instances", instance_of_each_name.toString());
            Log.i("NameList", names.toString());
    
    
            // we must allow rows to span the whole screen!
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    
            // TODO: Defining LayoutParams Here:
            //     : It is Extremely Important that you define the Layout Params
            //     : (e.g. : TableLayout.LayoutParams, TableRow.LayoutParams)
    
            final int screenWidth = displayMetrics.widthPixels;
    
            TableLayout.LayoutParams layparam_tb = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.MATCH_PARENT);
    
            TableRow.LayoutParams layparam_tr = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT);
    
            LinearLayout.LayoutParams layparam_ll = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 5f);
    
            // TODO: MAJOR BUG : NOT SURE HOW TO FIX
            //  REMOVING THIS ENLARGES THE FIRST IMAGE
            //  KEEPING THIS SHRINKS THE TEXT VIEW BADLY
            // layparam_tr.span = screenWidth;
    
    
            // Adding views
    
            int imageIndex = 0; // resetting ParseObject index holder counter
    
            for (int i = 0; i < namesUnique.size(); i++) {
        
                arrayImages.clear();
        
        
                String nameFeed = namesUnique.get(i);
                nameFeed = nameFeed.substring(0, 1).toUpperCase() + nameFeed.substring(1);
        
                final TableRow tablerow_anon_a = new TableRow(this);
                tablerow_anon_a.setLayoutParams(layparam_tb);
                tablerow_anon_a.setWeightSum(5);
                tablerow_anon_a.setBackgroundResource(R.drawable.list_theme);
                tablerow_anon_a.setPadding(25, 0, 25, 10);
        
                final TableRow tablerow_anon_b = new TableRow(this);
                tablerow_anon_b.setLayoutParams(layparam_tb);
                tablerow_anon_b.setWeightSum(5);
                tablerow_anon_b.setBackgroundResource(R.drawable.list_theme);
                tablerow_anon_b.setPadding(25, 0, 25, 20);
        
                final LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(layparam_tr);
                linearLayout.setWeightSum(5);
        
                TextView textview_anon = new TextView(this);
                textview_anon.setLayoutParams(layparam_ll); //X
                textview_anon.setText(nameFeed + " uploaded:");
                textview_anon.setMaxLines(1);
                textview_anon.setTextColor(Color.WHITE);
                textview_anon.setTextSize(2, 20);
                textview_anon.setPadding(25, 5, 25, 15);
                textview_anon.setEllipsize(android.text.TextUtils.TruncateAt.END);
        
                linearLayout.addView(textview_anon);
                tablerow_anon_a.addView(linearLayout);
        
        
                // draw each image
                    for (int j = 0; j < instance_of_each_name.get(i); j++) {
    
                        try {
                        // going through the record we want and getting the image column file
                        ParseFile file = (ParseFile) feed_records.get(imageIndex).get("image");
        
                        imageIndex++;
        
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null) {
                                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    Log.i("ByteArray", "" + data);
                                } else {
                                    Log.i("Image Error", "Parse Image Fetch Error");
                                }
                
                                ImageView imageView = new ImageView(getApplicationContext());
                                imageView.setImageBitmap(bitmap);
                
                                TableRow.LayoutParams layparam_tr = new TableRow.LayoutParams(
                                        100,
                                        100);
                
                                imageView.setLayoutParams(layparam_tr);
                
                                int resolution = 150;
                
                                imageView.getLayoutParams().height = 150;
                                imageView.getLayoutParams().width = 150;
                
                                imageView.setPadding(20, 0, 0, 20);
                
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                
                                imageView.setMaxWidth(resolution);
                                imageView.setMaxHeight(resolution);
                
                                imageView.setMinimumWidth(resolution);
                                imageView.setMinimumHeight(resolution);
                
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        trasferBitmap(v);
                                    }
                                });
                
                                tablerow_anon_b.addView(imageView);
                            }
                        });
    
                        } catch (IndexOutOfBoundsException e) {
                            Log.i("Error", "Index Error");
                        }
                    }
        
                tabitha.addView(tablerow_anon_a);
                tabitha.addView(tablerow_anon_b);
            }
        }
    }
    
    public void trasferBitmap(View v) {
        
        // TODO: Here we are just using a global variable which is not very great
        //       Ideally, we would give the ParseObject's Primary Key and class
        //       or Username column of the ParseObject
        //       or the ParseObject itself (not great)
        
        ImageView image = (ImageView)v;
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        transferredImage = bitmap;
        
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
        
        // we do not finish here, as to allow backing out
        // finish();
        
    }
    public void getLatestInfo(final int results_needed) {
        
        final ParseQuery query = ParseQuery.getQuery("User_Images");
        
        query.orderByDescending("updatedAt");
        query.setLimit(results_needed);
        query.addDescendingOrder("username");
        
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (int i = 0; i < objects.size(); i++) {
                            feed_records.add(objects.get(i));
                        }
                    }
                } else {
                    Log.i("ParseQuery", "Error Occurred");
                }
                
                // render News Feed just when Callback is done, you can only place this inside the call back
                // because it is asymmetric, if you do it after this clause
                // it might render before the query is finished
                
                renderNewsFeed();
            }
        });
        
        
    }
    
    public void uploadPhoto() {
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 88);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadPhoto();
            } else {
                Toast.makeText(getApplicationContext(), "Settings Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        Bitmap bitmap = null;
        final Toast toast = Toast.makeText(SecondActivity.this, "Loading..", Toast.LENGTH_LONG);
        
        if (requestCode == 88 && resultCode == RESULT_OK && data != null) {
            
            try {
                
                Uri selectedImage = data.getData();
                
                if (android.os.Build.VERSION.SDK_INT >= 29) {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), selectedImage));
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                }
            } catch (IOException e) {
                // TODO:
                //        IMPORTANT: untested if the context is correct, may be wrong
                Toast.makeText(getApplicationContext(), "Error: Please Restart App", Toast.LENGTH_SHORT).show();
            }
    
            // change now
            // if you change later it will be done to all requestCodes
            // even failing ones
            
            toast.show();
            
            final CountDownTimer countDownTimer;
            
            // here we are assuming that Toast.LENGTH_LONG will always be 3500
            // we should not make assumptions about toast length
            countDownTimer = new CountDownTimer(3550, 50) {
                @Override
                public void onTick(long millisUntilFinished) {
        
                }
    
                @Override
                public void onFinish() {
                    toast.show();
                }
            }.start();
            
            // Turning image to bytes
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, stream);
            byte[] byteArray = stream.toByteArray();
            
            // ParseFile only accepts byte arrays
            // which is better than ObjectSerialized in this case, because the image remains as a file and not a string
            ParseFile file = new ParseFile("image.png", byteArray);
            
            // creating an Image Entity (a user may have many images) .. think Database Design
            ParseObject parseObject = new ParseObject("User_Images");
            
            parseObject.put("username", ParseUser.getCurrentUser().getUsername());
            parseObject.put("image", file);
            //
            
            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        toast.cancel();
                        countDownTimer.cancel();
                        Toast.makeText(SecondActivity.this, "Image uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        
                        getLatestInfo(10);
                    } else {
                        // i think we are using "SecondActivity.this" because we are in a pre-built method in this class?
                        Toast.makeText(SecondActivity.this, "Error: Image Could not be Shared", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            
        }
    }
}
