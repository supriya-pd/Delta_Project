package com.example.project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.fragments.Fragment_News;
import com.example.project.fragments.Fragment_Posts;
import com.example.project.model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DashActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseRef;

    private ImageView profile_image;
    private TextView user_email,user_name;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        profile_image=(ImageView) findViewById(R.id.header_image_view);
        user_name=(TextView)findViewById(R.id.header_fName);
        user_email=(TextView)findViewById(R.id.header_email);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

       NavigationView navigationView=findViewById(R.id.nav_view);
       navigationView.setNavigationItemSelectedListener(this);
       if(savedInstanceState==null) {
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Posts()).commit();
           navigationView.setCheckedItem(R.id.drawer_nav_post);
       }

        mAuth=FirebaseAuth.getInstance();
       /*mUser=mAuth.getCurrentUser();
       databaseRef= FirebaseDatabase.getInstance().getReference().child("Project_App_Users").child(mUser.getUid());
       databaseRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               User user_ob=snapshot.getValue(User.class);
               user_name.setText(user_ob.getFirstName()+" "+user_ob.getLastName());
               user_email.setText(mUser.getEmail());
               Picasso.get().load(user_ob.getImage()).into(profile_image);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(DashActivity.this,error.getCode(), Toast.LENGTH_LONG).show();

           }
       });*/

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.signOut)
        {
            mAuth.signOut();
             Intent intent= new Intent(DashActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// In app it's not working: dunno why
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.drawer_nav_news:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_News()).commit();
                break;
            case R.id.drawer_nav_post:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Fragment_Posts()).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}