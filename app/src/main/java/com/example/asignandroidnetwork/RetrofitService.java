package com.example.asignandroidnetwork;

import com.example.asignandroidnetwork.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    @GET("services/rest/?method=flickr.favorites.getList&api_key=2eb9ea3b4537fe49ca1d6e0eb1cdfd42&user_id=191213941%40N03&extras=views%2C+media%2C+path_alias%2C+url_sq&per_page=30&page=1&format=json&nojsoncallback=1")
    Call<Example> getFavorite();
}
