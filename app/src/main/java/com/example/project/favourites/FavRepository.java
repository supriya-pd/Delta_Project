package com.example.project.favourites;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavRepository {

    private FavItemDao favItemDao;
    private LiveData<List<FavItem>> allFav;

    public FavRepository(Application application){
        FavDatabase favDatabase=FavDatabase.getInstance(application);
        favItemDao=favDatabase.favItemDao();
        allFav=favItemDao.getAllFav();
    }
    public void insert(FavItem favItem){

        new InsertAsyncTask(favItemDao).execute(favItem);

    }
    public void delete(FavItem favItem){
        new DeleteAsyncTask(favItemDao).execute(favItem);


    }
    public void deleteAll(){

        new DeleteAllAsyncTask(favItemDao).execute();

    }
    public LiveData<List<FavItem>> getAllFav(){
        return allFav;
    }

    private static class InsertAsyncTask extends AsyncTask<FavItem,Void,Void>{
        private FavItemDao favItemDao;

        private InsertAsyncTask(FavItemDao favItemDao) {
            this.favItemDao = favItemDao;
        }

        @Override
        protected Void doInBackground(FavItem... favItems) {
            favItemDao.insert(favItems[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<FavItem,Void,Void>{
        private FavItemDao favItemDao;

        private DeleteAsyncTask(FavItemDao favItemDao) {
            this.favItemDao = favItemDao;
        }

        @Override
        protected Void doInBackground(FavItem... favItems) {
            favItemDao.delete(favItems[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private FavItemDao favItemDao;

        private DeleteAllAsyncTask(FavItemDao favItemDao) {
            this.favItemDao = favItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            favItemDao.deleteAll();
            return null;
        }
    }
}
