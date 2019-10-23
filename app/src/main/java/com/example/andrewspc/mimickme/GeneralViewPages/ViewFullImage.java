package com.example.andrewspc.mimickme.GeneralViewPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrewspc.mimickme.Explore.PostedImage;
import com.example.andrewspc.mimickme.R;
import com.squareup.picasso.Picasso;

public class ViewFullImage extends AppCompatActivity {

    private ImageView ImageFullSize;
    private TextView UsernameOfUser;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_image);

        ImageFullSize = findViewById(R.id.ImageFullSize);
        UsernameOfUser = findViewById(R.id.UsernameOfUser);
        backArrow = findViewById(R.id.backArrow);

        final Intent intent = getIntent();
        String imageOfPost = intent.getExtras().getString("PostedImagePassed");
        String Username = intent.getExtras().getString("Username");

        UsernameOfUser.setText(Username);
        Picasso.get().load(imageOfPost).fit().into(ImageFullSize);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
