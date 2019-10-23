package com.example.andrewspc.mimickme.Explore;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.mimickme.GeneralViewPages.ViewFullImage;
import com.example.andrewspc.mimickme.R;
import com.example.andrewspc.mimickme.UserProfileSettings.ProfilePage;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostedImage extends AppCompatActivity {

    private ImageView backArrow;
    private TextView username;
    private CircleImageView UserProfilePic;
    private ImageView postingImage;
    private TextView descriptionOfItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_image);

        backArrow = findViewById(R.id.backArrow);
        username = findViewById(R.id.UsernameOfUser);
        UserProfilePic = findViewById(R.id.UserProfilePic);
        postingImage = findViewById(R.id.postingImage);
        descriptionOfItem = findViewById(R.id.descriptionOfItem);

        final Intent intent = getIntent();
        String imageOfPost = intent.getExtras().getString("PostedImage");
        String Description = intent.getExtras().getString("Description");
        final String UserProfileImage = intent.getExtras().getString("UserProfileImage");
        final String Username = intent.getExtras().getString("Username");

        UserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(PostedImage.this, ViewFullImage.class);
                intent.putExtra("PostedImagePassed", UserProfileImage);
                intent.putExtra("Username", Username);
                startActivity(intent);
            }
        });

        Picasso.get().load(imageOfPost).fit().into(postingImage);
        username.setText(Username);
        Picasso.get().load(UserProfileImage).fit().into(UserProfilePic);
        descriptionOfItem.setText(Description);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
