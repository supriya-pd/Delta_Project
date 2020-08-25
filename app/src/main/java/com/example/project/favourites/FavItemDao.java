package com.example.project.favourites;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavItemDao {

    @Insert
     void insert(FavItem favItem);

     @Delete
    void delete(FavItem favItem);

     @Query("DELETE FROM fav_table")
     void deleteAll();

    @Query("SELECT * FROM fav_table ORDER BY ID DESC")
    LiveData<List<FavItem>> getAllFav();


}
