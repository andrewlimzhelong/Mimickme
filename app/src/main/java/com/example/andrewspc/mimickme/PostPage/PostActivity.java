package com.example.andrewspc.mimickme.PostPage;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.andrewspc.mimickme.Explore.ExploreHomePage;
import com.example.andrewspc.mimickme.R;
import com.example.andrewspc.mimickme.UserProfileSettings.EditProfilePage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private ImageView postingImage;
    private EditText descriptionOfItem;
    private RelativeLayout clickPostBtn;
    String Image;

    private Uri imageUriFilePath;

    // Firebase
    private StorageReference storageReference;
    private FirebaseUser mCurrentUser;
    //Image Uri Storage variable
    private String imageLink;
    private String Username;

    //Database reference
    private DatabaseReference mDatabaseDesign;
    private DatabaseReference mDatabasePosts;
    private DatabaseReference imageDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postingImage = findViewById(R.id.postingImage);
        descriptionOfItem = findViewById(R.id.descriptionOfItem);
        clickPostBtn = findViewById(R.id.clickPostBtn);

        final Intent intent = getIntent();
        Uri imageUri = intent.getParcelableExtra("imageSelected");
        imageUriFilePath = imageUri;

        //Storage Reference for uploading of images
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(current_uid).child("Posts").push();
        mDatabasePosts = FirebaseDatabase.getInstance().getReference().child("Users").child("Posts").push();
        imageDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(current_uid);

        Picasso.get().load(imageUri).fit().into(postingImage);

        imageDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("ProfilePicture")) {
                    Image = snapshot.child("ProfilePicture").getValue().toString();
                    Username = snapshot.child("Username").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        clickPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(descriptionOfItem.getEditableText().toString())) {

                    postingImage.setEnabled(false);
                    uploadFile();

                    Intent setupIntent = new Intent(PostActivity.this, ExploreHomePage.class);
                    startActivity(setupIntent);

                } else {
                    if (TextUtils.isEmpty(descriptionOfItem.getEditableText().toString())) {
                        descriptionOfItem.requestFocus();
                    }

                    Toast.makeText(PostActivity.this, "Please Enter A Description", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadFile() {

        if (imageUriFilePath != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUriFilePath));
            fileReference.putFile(imageUriFilePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    /// Suspect this line is not working
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        PostActivityModelClass upload = new PostActivityModelClass(downloadUri.toString());

                        imageLink = upload.getmImageUrl();

                        final String uploadTitle = descriptionOfItem.getEditableText().toString();

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                        String time = mdformat.format(calendar.getTime());

                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        long timeOfPostInNumbers = Calendar.getInstance().getTime().getTime();
                        String timeOfPostInString = Long.toString(timeOfPostInNumbers);

                        HashMap<String, String> multipleData = new HashMap<>();

                        mDatabaseDesign.child("Description").setValue(uploadTitle);
                        mDatabaseDesign.child("PostedImage").setValue(imageLink);
                        mDatabaseDesign.child("ProfilePicture").setValue(Image);

                        String uid = mCurrentUser.getUid();
                        String imageKey = FirebaseDatabase.getInstance().getReference().push().getKey();
                        mDatabaseDesign.child("userID").setValue(uid);
                        mDatabaseDesign.child("imageUniqueKey").setValue(imageKey);
                        mDatabaseDesign.child("time").setValue(time);
                        mDatabaseDesign.child("date").setValue(date);
                        mDatabaseDesign.child("Username").setValue(Username);

                        mDatabasePosts.child("Description").setValue(uploadTitle);
                        mDatabasePosts.child("PostedImage").setValue(imageLink);
                        mDatabasePosts.child("ProfilePicture").setValue(Image);
                        mDatabasePosts.child("userID").setValue(uid);
                        mDatabasePosts.child("imageUniqueKey").setValue(imageKey);
                        mDatabasePosts.child("time").setValue(time);
                        mDatabasePosts.child("date").setValue(date);
                        mDatabasePosts.child("Username").setValue(Username);

                        clickPostBtn.setEnabled(true);

                    } else {
                        Toast.makeText(PostActivity.this, "upload failed here2: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    ////////////////////////////////////////////////// The below method is just to get the file extension for the image //////////////////////////////////////////////////
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
