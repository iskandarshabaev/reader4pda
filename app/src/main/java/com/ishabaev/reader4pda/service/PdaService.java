package com.ishabaev.reader4pda.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PdaService {

    @GET("/page/{page}")
    Call<Posts> getNews(@Path("page") int page);

    @GET("/?")
    Call<Description> getDescription(@Query("p") String p);
}
