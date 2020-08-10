package com.example.project;

import com.example.project.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface News_Interface {
    @GET("top-headlines")
    Call<News> getHeadlines(@Query("country") String country,
                            @Query("apiKey") String apiKey);
}
