package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPostActivity extends AppCompatActivity {

    EditText titleET,descET;
    Button saveBTN;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);



        Intent intent=getIntent();
        String key=intent.getStringExtra("key");
        final String title=intent.getStringExtra("title");
        final String desc=intent.getStringExtra("desc");

        ref= FirebaseDatabase.getInstance().getReference().child("Projects_Blog").child(key);

        titleET=(EditText)findViewById(R.id.editTitle);
        descET=(EditText)findViewById(R.id.editDesc);
        saveBTN=(Button)findViewById(R.id.save);

        titleET.setText(title);
        descET.setText(desc);


        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 String changedTitle=titleET.getText().toString();
                 String changedDesc=descET.getText().toString();

                ref.child("title").setValue(changedTitle);
                ref.child("desc").setValue(changedDesc);
                startActivity(new Intent(EditPostActivity.this,DashActivity.class));
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(EditPostActivity.this,DashActivity.class);
        startActivity(intent);
        finish();
    }
}