package com.example.igclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ShowUserFeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_feed);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username", username);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
            if(e==null && objects.size()>0) {
                for (ParseObject object : objects) {
                    ParseFile file = object.getParseFile("image");

                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if(e==null && data!=null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                LinearLayout linearLayout = findViewById(R.id.linearLayout);
                                ImageView image = new ImageView(getApplicationContext());
                                image.setPadding(0,0,0,200);
                                image.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                ));
                                image.setScaleType(ImageView.ScaleType.FIT_XY);
                                image.setImageBitmap(bitmap);
                                linearLayout.addView(image);
                            }
                        }
                    });
                }
            }
            }
        });
    }
}
