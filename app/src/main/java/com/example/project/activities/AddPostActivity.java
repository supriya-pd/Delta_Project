package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private ImageButton mPostImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitButton;
    private StorageReference mStorage;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    private Uri mImageUri;
    private Uri resultUri;
    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();


        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Projects_Blog");


        mPostImage = (ImageButton) findViewById(R.id.imageButton);
        mPostTitle = (EditText) findViewById(R.id.postTitleEt);
        mPostDesc = (EditText)findViewById(R.id.descriptionEt);
        mSubmitButton = (Button) findViewById(R.id.submitPost);

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Posting to our database
                startPosting();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();
                    mPostImage.setBackgroundResource(0);
                    mPostImage.setImageURI(resultUri);

                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    mPostImage.setBackgroundResource(R.drawable.add_btn);

                    Exception error = result.getError();
                    Toast.makeText(AddPostActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
                }
    }
    }

    private void startPosting() {



        final String titleVal = mPostTitle.getText().toString().trim();
        final String descVal = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal)
                && mImageUri != null) {
            mProgress.setMessage("Posting to blog...");
            mProgress.show();

            final StorageReference filepath = mStorage.child("Project_Blog_Images").
                    child(resultUri.getLastPathSegment());
            filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                     //final Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                     if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {

                       Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();

                                DatabaseReference newPost = mPostDatabase.push();


                                Map<String, String> dataToSave = new HashMap<>();
                                dataToSave.put("title", titleVal);
                                dataToSave.put("desc", descVal);
                                dataToSave.put("image", imageUrl);
                                dataToSave.put("timestamp", String.valueOf(java.lang.System.currentTimeMillis()));
                                dataToSave.put("userId", mUser.getUid());
                                dataToSave.put("username", mUser.getEmail());
                                dataToSave.put("key",newPost.getKey());

                                newPost.setValue(dataToSave);

                          }
                });

                  }

                    mProgress.dismiss();

                    startActivity(new Intent(AddPostActivity.this, DashActivity.class));
                    finish();

                }
            });

        }
        else if(mImageUri==null)
        {
            Toast.makeText(AddPostActivity.this,"please select a photo",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(AddPostActivity.this,"please enter TITLE and DESCRIPTION ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AddPostActivity.this,DashActivity.class);
        startActivity(intent);
        finish();
    }
}