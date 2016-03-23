package com.ishabaev.reader4pda.service;

import com.ishabaev.reader4pda.converters.DescriptionConverterFactory;
import com.ishabaev.reader4pda.converters.PostsConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class PdaClient {

    private static Retrofit retrofit;
    private static PdaService pdaService;
    public static final String source = "http://4pda.ru/";

    public static PdaService getPdaService(){
        if(pdaService != null){
            return pdaService;
        }else{

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(source)
                    .client(client)
                    .addConverterFactory(PostsConverterFactory.create())
                    .addConverterFactory(DescriptionConverterFactory.create())
                    .build();
            pdaService = retrofit.create(PdaService.class);
            return pdaService;
        }
    }
}
