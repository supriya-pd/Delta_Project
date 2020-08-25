package com.example.project.favourites;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavItem.class}, version = 1,exportSchema = false)
public abstract class FavDatabase extends RoomDatabase {

    private static FavDatabase favDatabase;
    public abstract FavItemDao favItemDao();
    public static synchronized FavDatabase getInstance(Context context){
        if(favDatabase==null){
            favDatabase= Room.databaseBuilder(context.getApplicationContext(),FavDatabase.class,"fav_database")
                    .fallbackToDestructiveMigration().build();
        }
        return favDatabase;
    }

}
