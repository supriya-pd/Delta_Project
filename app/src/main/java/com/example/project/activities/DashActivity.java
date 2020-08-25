package com.example.project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.project.R;
import com.example.project.fragments.Fragment_News;
import com.example.project.fragments.Fragment_Posts;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class DashActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseRef;

    private ImageView profile_image;
    private TextView user_email,user_name;
    private DrawerLayout drawerLayout;
    GoogleSignInClient mSignInClient;
    FirebaseUser userOfAccount;

    private String GName=null;
    String GEmail=null;
    String GPhoto=null;

    ToggleButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

      /* Intent intent=getIntent();
       GName= intent.getStringExtra("GName");
       GEmail= intent.getStringExtra("GEmail");
       GPhoto= intent.getStringExtra("GPhoto");*/

       /* GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);*/


        mAuth=FirebaseAuth.getInstance();
        userOfAccount=mAuth.getCurrentUser();

        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);


        profile_image=(ImageView) headerView.findViewById(R.id.header_image_view);
        user_name=(TextView)headerView.findViewById(R.id.header_fName);
        user_email=(TextView)headerView.findViewById(R.id.header_email);
        button=(ToggleButton)headerView.findViewById(R.id.toggleButton);

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
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    button.setChecked(true);
                    editor.putBoolean("isDarkModeOn",true);

                }
                editor.apply();
            }
        });


        if(userOfAccount!=null)
        {

            GName=userOfAccount.getDisplayName();
            GEmail=userOfAccount.getEmail();
            GPhoto=userOfAccount.getPhotoUrl().toString();
            user_name.setText(GName);
            user_email.setText(GEmail);
            Picasso.get().load(Uri.parse(GPhoto)).into(profile_image);

        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


       navigationView.setNavigationItemSelectedListener(this);
       if(savedInstanceState==null) {
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Posts()).commit();
           navigationView.setCheckedItem(R.id.drawer_nav_post);
       }

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
            //mSignInClient.signOut();

             Intent intent= new Intent(DashActivity.this,MainActivity.class);
             startActivity(intent);
             finish();
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

    private void getPhoto(){
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
}