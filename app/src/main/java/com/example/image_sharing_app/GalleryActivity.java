package com.example.image_sharing_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.parse.ParseUser;

import static com.example.image_sharing_app.SecondActivity.transferredImage;

public class GalleryActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    
        ImageView imageView = findViewById(R.id.mainView);
        imageView.setImageBitmap(transferredImage);
    
        TableRow.LayoutParams layparam_tr = new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
    
        imageView.setLayoutParams(layparam_tr);
        
        // TODO: float scale is dp value                                           |
        //       instead of "int resolution" in SecondActivity                     |
        //       which is just px value                                            |
        //                                                                         |
        //       Very Important btw, cause scaling always messes up on old devices |
        
        final float scale = getResources().getDisplayMetrics().density;
        int dpWidthInPx  = (int) (300 * scale);
        int dpHeightInPx = (int) (300 * scale);
        
        
        imageView.getLayoutParams().height = dpHeightInPx;
        imageView.getLayoutParams().width = dpWidthInPx;
    
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
    
            dpWidthInPx  = (int) (135 * scale);
            dpHeightInPx = (int) (135 * scale);
    
            imageView.getLayoutParams().height = dpHeightInPx;
            imageView.getLayoutParams().width = dpWidthInPx;
            
        } else {
            // In portrait
        }
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
    
    public void goBackToUserFeed(View v) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        
        finish();
    }
}
