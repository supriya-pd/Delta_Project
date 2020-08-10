package com.example.project.activities;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener mListener;

    private EditText email,password;
    private Button signIn,signUp;
    ToggleButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();


        email=(EditText)findViewById(R.id.emailET);
        password=(EditText)findViewById(R.id.passwordET);
        signIn=(Button)findViewById(R.id.loginBTN);
        signUp=(Button)findViewById(R.id.accountBTN);
        button=(ToggleButton)findViewById(R.id.toggleButton);

        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        final boolean isDarkModeOn=sharedPreferences.getBoolean("isDarkModeOn",false);

        if(isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            button.setChecked(true);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            button.setChecked(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDarkModeOn){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    button.setChecked(false);
                    editor.putBoolean("isDarkModeOn",false);
                    editor.apply();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    button.setChecked(true);
                    editor.putBoolean("isDarkModeOn",true);
                    editor.apply();

                }
            }
        });

        mListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser=firebaseAuth.getCurrentUser();
                if(firebaseUser!=null)
                {
                    Toast.makeText(MainActivity.this, "signed in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,DashActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, "Not Signed in!", Toast.LENGTH_SHORT).show();
                }


            }
        };

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CreateAccount.class));

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            }
        });
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
}