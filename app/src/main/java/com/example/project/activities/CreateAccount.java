package com.example.project.activities;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
     private StorageReference mStorageRef;

     private ProgressDialog mProgressDialog;
    private EditText fName,lName,email,password;
   private ImageButton profileBtn;
   private Button acctBtn;

   private Uri resultUri=null;
   private Uri mImageUri;
   private String profile_pic_url;

    private static final int GALLERY_CODE=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

       mDatabase=FirebaseDatabase.getInstance();
        mDatabaseRef=mDatabase.getReference().child("Project_App_Users");
        mStorageRef= FirebaseStorage.getInstance().getReference().child("Profile_pics");
        mAuth=FirebaseAuth.getInstance();

        mProgressDialog=new ProgressDialog(this);
        fName=(EditText)findViewById(R.id.FirstNameET);
        lName=(EditText)findViewById(R.id.LastNameET);
        email=(EditText)findViewById(R.id.Create_EmailET);
        password=(EditText)findViewById(R.id.Create_acct_pwd);
        profileBtn=(ImageButton)findViewById(R.id.profileBTN);
        acctBtn=(Button)findViewById(R.id.create_acc_activityBTN);

        acctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);

            }
        });

    }

    private void createNewAccount() {
        final String firstName=fName.getText().toString().trim();
        final String lastName=lName.getText().toString().trim();
        String emailId=email.getText().toString().trim();
        String pwd=password.getText().toString().trim();

        if(!TextUtils.isEmpty(emailId) && !TextUtils.isEmpty(pwd) && mImageUri!=null)
        {
            mProgressDialog.setMessage("Creating Account...");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailId, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (authResult != null) {

                                StorageReference imagePath = mStorageRef
                                        .child(resultUri.getLastPathSegment());

                                imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                       // Uri downloadURI=taskSnapshot.getUploadSessionUri();

                                     if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {

                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    profile_pic_url = uri.toString();
                                                }
                                            });

                                        }
                                   /*  taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                         @Override
                                         public void onSuccess(Uri uri) {
                                             profile_pic_url = uri.toString();
                                         }
                                     });*/

                                        String userId = mAuth.getCurrentUser().getUid();

                                        DatabaseReference currentUserDb = mDatabaseRef.child(userId);
                                       /* User user=new User(firstName,lastName,profile_pic_url);
                                        currentUserDb.setValue(user);*/


                                       currentUserDb.child("firstName").setValue(firstName);
                                        currentUserDb.child("lastName").setValue(lastName);
                                        currentUserDb.child("image").setValue(profile_pic_url);


                                        mProgressDialog.dismiss();

                                        Intent intent = new Intent(CreateAccount.this,DashActivity.class );
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);


                                    }
                                });
                            }

                        }
                    }).addOnFailureListener(CreateAccount.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseNetworkException)
                    {
                        Toast.makeText(CreateAccount.this,"No internet connection", Toast.LENGTH_LONG).show();
                    }
                    else if (e instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(CreateAccount.this,"Account with this email already exists!", Toast.LENGTH_LONG).show();
                    }
                    else if(e instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(CreateAccount.this,"Please enter a valid email or Password should be minimum 6 characters!", Toast.LENGTH_LONG).show();
                    }
                    else if(e instanceof FirebaseAuthWeakPasswordException)
                    {
                        Toast.makeText(CreateAccount.this,"Password should be at least 6 characters " , Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(CreateAccount.this,e.toString(), Toast.LENGTH_LONG).show();
                    }

                    mProgressDialog.dismiss();


                }
            });

        }
        else if(TextUtils.isEmpty(emailId)){
            Toast.makeText(CreateAccount.this,"Please enter email Id!", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pwd)){
            Toast.makeText(CreateAccount.this,"Please enter password !", Toast.LENGTH_LONG).show();

        }
        else if(mImageUri==null)
        {
            Toast.makeText(CreateAccount.this,"Please select profile pic", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);



        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                profileBtn.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(CreateAccount.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}