package com.example.project.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Fragment_News extends Fragment {
    public Fragment_News(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottom_nav= view.findViewById(R.id.bottom_nav);

        bottom_nav.setOnNavigationItemSelectedListener((navListener));


        if(savedInstanceState==null) {
            getChildFragmentManager().beginTransaction().replace(R.id.bottom_nav_container, new Fragment_News_Home(),"HOME").commit();

        }

        }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener=new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch(menuItem.getItemId()){

                        case R.id.home:
                            getChildFragmentManager().beginTransaction().replace(R.id.bottom_nav_container, new Fragment_News_Home(),"HOME").commit();


                            break;

                        case R.id.Favorites:
                            getChildFragmentManager().beginTransaction().replace(R.id.bottom_nav_container, new Fragment_News_Fav(),"FAV").commit();


                         /*   if(getChildFragmentManager().findFragmentByTag("FAV")!=null){
                                getChildFragmentManager().beginTransaction().show(getChildFragmentManager().findFragmentByTag("FAV")).commit();
                            }
                            else{
                                getChildFragmentManager().beginTransaction().add(R.id.bottom_nav_container, new Fragment_News_Fav(),"FAV").commit();
                                }
                            if(getChildFragmentManager().findFragmentByTag("HOME")!=null)
                            {
                                getChildFragmentManager().beginTransaction().hide(getChildFragmentManager().findFragmentByTag("HOME")).commit();
                            }*/



                            break;
                    }

                    return true;
                }

            };

}
