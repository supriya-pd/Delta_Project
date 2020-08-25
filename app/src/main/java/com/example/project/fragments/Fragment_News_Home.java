package com.example.project.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.News_Interface;
import com.example.project.R;
import com.example.project.adapters.NewsAdapter;
import com.example.project.favourites.FavViewModel;
import com.example.project.model.Article;
import com.example.project.model.News;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_News_Home extends Fragment implements NewsAdapter.OnItemClickListener {
    String API_KEY;
    String BASE_URL;
    News_Interface news_interface;
    List<Article> articleList;
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    FavViewModel favViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        API_KEY = "d67c3388d72e470ca91064a10e33b78d";
        BASE_URL = "https://newsapi.org/v2/";
        articleList = new ArrayList<>();

        favViewModel= new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(FavViewModel.class);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Gson gson = new GsonBuilder().serializeNulls().create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build();


        news_interface = retrofit.create(News_Interface.class);
        Call<News> call = news_interface.getHeadlines("in", API_KEY);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful()) {
                    articleList = response.body().getArticle();
                    newsAdapter.updateList(articleList);
                    newsAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        newsAdapter = new NewsAdapter(getContext(), articleList,this,favViewModel);
        recyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void onItemClick(int position) {

        //Toast.makeText(getContext(), "position " + position, Toast.LENGTH_SHORT).show();
        String url = articleList.get(position).getUrl();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }
}