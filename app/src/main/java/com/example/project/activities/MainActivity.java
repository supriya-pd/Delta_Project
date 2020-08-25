package com.example.project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.project.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener mListener;

    //private EditText email,password;
    private Button signIn,signUp;


    private static final int RC_SIGN_IN=1;
    GoogleSignInClient mSignInClient;  // client needed for intent

    String GName=null;
    String GEmail=null;
    String GPhoto=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();


        /*email=(EditText)findViewById(R.id.emailET);
        password=(EditText)findViewById(R.id.passwordET);*/
        signIn=(Button)findViewById(R.id.loginBTN);
        //signUp=(Button)findViewById(R.id.accountBTN);


         //configure google sign in
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);




        mListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser=firebaseAuth.getCurrentUser();
                if(firebaseUser!=null)

                {
                    GName=firebaseUser.getDisplayName();
                    GEmail=firebaseUser.getEmail();
                    GPhoto=firebaseUser.getPhotoUrl().toString();
                    Toast.makeText(MainActivity.this, "signed in! "+GName, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,DashActivity.class);
                   /* intent.putExtra("GName",GName);
                    intent.putExtra("GEmail",GEmail);
                    intent.putExtra("GPhoto",GPhoto);*/
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(MainActivity.this, "Not Signed in!", Toast.LENGTH_SHORT).show();
                }


            }
        };

      /*  signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CreateAccount.class));

            }
        });*/
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent=mSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
             Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
             handleSignIn(task);
        }
    }

    private void handleSignIn(Task<GoogleSignInAccount> task){

            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                Toast.makeText(MainActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                FirebaseGoogleAuth(account);

            }

            catch (ApiException e) {
                Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                FirebaseGoogleAuth(null);
                e.printStackTrace();
            }
    }
    private void FirebaseGoogleAuth(GoogleSignInAccount account){

        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mListener != null) {
            firebaseAuth.removeAuthStateListener(mListener);
        }
    }

  /*  public void signInEmail(){
        final String emailId=email.getText().toString().trim();
        final String pwd=password.getText().toString().trim();
        if(!TextUtils.isEmpty(emailId) && !TextUtils.isEmpty(pwd)) {
            firebaseAuth.signInWithEmailAndPassword(emailId, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, DashActivity.class));
                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseAuthInvalidUserException)
                    {
                        Toast.makeText(MainActivity.this,"The Email Id is invalid! ", Toast.LENGTH_LONG).show();
                    }
                    else if(e instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(MainActivity.this,"Password is incorrect", Toast.LENGTH_LONG).show();
                    }
                    else if(e instanceof FirebaseNetworkException)
                    {
                        Toast.makeText(MainActivity.this,"You are not connected to the internet!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this,e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(MainActivity.this, "Please enter required fields!", Toast.LENGTH_SHORT).show();
        }

    }*/
}