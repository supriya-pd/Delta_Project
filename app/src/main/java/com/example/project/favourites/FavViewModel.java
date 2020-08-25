package com.example.project.favourites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class FavViewModel extends AndroidViewModel {

    private FavRepository repository;
    private LiveData<List<FavItem>> allFav;



    public FavViewModel(@NonNull Application application) {
        super(application);
        repository=new FavRepository(application);
        allFav=repository.getAllFav();
    }


    public void insert(FavItem favItem){
        repository.insert(favItem);
    }

    public void delete(FavItem favItem){
        repository.delete(favItem);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
    public LiveData<List<FavItem>> getAllFav(){
        return allFav;
    }
}
