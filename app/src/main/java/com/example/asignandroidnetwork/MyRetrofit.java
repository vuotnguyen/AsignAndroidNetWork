package com.example.asignandroidnetwork;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit  {
    public static Retrofit retrofit;
    private static  final String BASE_URL = "https://www.flickr.com/";
    public  static  RetrofitService retrofitService;

    public static RetrofitService getInStance() {
        if(retrofitService ==  null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            retrofitService =retrofit.create(RetrofitService.class);
        }
        return retrofitService;
    }
}
