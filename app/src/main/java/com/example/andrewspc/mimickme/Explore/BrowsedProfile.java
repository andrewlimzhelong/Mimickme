package com.example.andrewspc.mimickme.Explore;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.mimickme.AccountLogin.MainLoginPage;
import com.example.andrewspc.mimickme.Chats.ChatsDisplay;
import com.example.andrewspc.mimickme.PostPage.PostActivity;
import com.example.andrewspc.mimickme.PostPage.PostedImages;
import com.example.andrewspc.mimickme.R;
import com.example.andrewspc.mimickme.UserProfileSettings.EditProfilePage;
import com.example.andrewspc.mimickme.UserProfileSettings.ProfilePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Collections;

public class BrowsedProfile extends AppCompatActivity {

    // XML Code
    private TextView HomeTitleText;
    private ImageView DisplayPic;
    private TextView userSavedName;
    private TextView Hpnum;
    private ImageView backArrow;
    private ImageView chatIcon;

    String userIDOfClickedUser;

    // Firebase
    private DatabaseReference mStatusDatabase;
    DatabaseReference reference;
    private String pushIdOfImage;
    RecyclerView myRecycler;
    ArrayList<ExploreModelClass> list;

    // Bottom Navigation Bar
    private RelativeLayout ExploreBtn;
    private RelativeLayout PostsBtn;
    private RelativeLayout sellBtn;
    private RelativeLayout ChatsBtn;
    private RelativeLayout ProfileBtn;
    private static final int GALLERY_REQUEST = 1;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsed_profile);

        backArrow = findViewById(R.id.backArrow);
        chatIcon = findViewById(R.id.chatIcon);

        // Bottom Navigation Bar
        ExploreBtn = findViewById(R.id.ExploreBtn);
        PostsBtn = findViewById(R.id.PostsBtn);
        sellBtn = findViewById(R.id.sellBtn);
        ChatsBtn = findViewById(R.id.ChatsBtn);
        ProfileBtn = findViewById(R.id.ProfileBtn);

        final Intent intent2 = getIntent();
        pushIdOfImage = intent2.getExtras().getString("UserID");

        // Retrieving the data from ThirdAdapter class
        final Intent intent = getIntent();
        String profilePicture = intent.getExtras().getString("profileImage");
        String Name = intent.getExtras().getString("Username");
        String Occupation = intent.getExtras().getString("Occupation");
        String ContactNumber = intent.getExtras().getString("ContactNumber");
        userIDOfClickedUser = intent.getExtras().getString("UserID");

        HomeTitleText = findViewById(R.id.HomeTitleText);
        DisplayPic = findViewById(R.id.DisplayPic);
        userSavedName = findViewById(R.id.userSavedName);
        Hpnum = findViewById(R.id.Hpnum);
        myRecycler = findViewById(R.id.myRecycler);

        myRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Youtube : https://www.youtube.com/watch?v=SD2t75T5RdY&t=716s
        // 3 item grid view is this below line of code
        myRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        list = new ArrayList<ExploreModelClass>();

        Picasso.get().load(profilePicture).fit().into(DisplayPic);
        userSavedName.setText(Occupation);
        HomeTitleText.setText(Name);
        Hpnum.setText(ContactNumber);

        getUserPosts();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BrowsedProfile.this, ExploreHomePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });

        // Everything from here onwards is for the bottom navigation bar
        ExploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(BrowsedProfile.this, ExploreHomePage.class);
                startActivity(loginIntent);
            }
        });

        PostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(BrowsedProfile.this, PostedImages.class);
                startActivity(loginIntent);
            }
        });

        ChatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(BrowsedProfile.this, ChatsDisplay.class);
                startActivity(loginIntent);
            }
        });

        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
            }
        });

        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(BrowsedProfile.this, ProfilePage.class);
                startActivity(loginIntent);
            }
        });
    }

    private void openChat() {

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile");
        // Generating Chat id of current user
        mStatusDatabase.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean keyPresent = false;

                if (dataSnapshot.hasChild("Chats")) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.hasChild(userIDOfClickedUser)) {
                            String chatID = snapshot.getValue().toString();

                            keyPresent = true;

                            Intent intent = new Intent(BrowsedProfile.this, ChatsDisplay.class);
                            intent.putExtra("chatIDD", chatID);
                            startActivity(intent);
                            break;
                        }
                    }
                }

                if (!keyPresent) {
                    // Generating Chat Id of current user (yourself)
                    String chatPushId = mStatusDatabase.child(FirebaseAuth.getInstance().getUid()).child("Chats").child(userIDOfClickedUser).push().getKey();
                    mStatusDatabase.child(FirebaseAuth.getInstance().getUid()).child("Chats").child(userIDOfClickedUser).child("UniqueChatID").setValue(chatPushId);
                    mStatusDatabase.child(FirebaseAuth.getInstance().getUid()).child("Chats").child(userIDOfClickedUser).child("UniqueUserID").setValue(userIDOfClickedUser);

                    // Generating Chat Id of clicked user (the other person)
                    mStatusDatabase.child(userIDOfClickedUser).child("Chats").child(FirebaseAuth.getInstance().getUid()).child("UniqueChatID").setValue(chatPushId);
                    mStatusDatabase.child(userIDOfClickedUser).child("Chats").child(FirebaseAuth.getInstance().getUid()).child("UniqueUserID").setValue(FirebaseAuth.getInstance().getUid());

                    // Generating Chat Id
                    DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Chats").child(chatPushId);
                    chatsRef.child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());
                    chatsRef.child(userIDOfClickedUser).setValue(userIDOfClickedUser);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Image Cropper and posting code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            filePath = data.getData();
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setMinCropResultSize(350, 350)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                final Intent intent = new Intent(BrowsedProfile.this, PostActivity.class);
                intent.putExtra("imageSelected", resultUri);
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void getUserPosts() {
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(userIDOfClickedUser).child("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ExploreModelClass p = dataSnapshot1.getValue(ExploreModelClass.class);
                    list.add(p);
                }
                Collections.reverse(list);
                OthersProfileAdapter othersProfileAdapter = new OthersProfileAdapter(BrowsedProfile.this, list);
                myRecycler.setAdapter(othersProfileAdapter);
                othersProfileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BrowsedProfile.this, "Error something is not right", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
