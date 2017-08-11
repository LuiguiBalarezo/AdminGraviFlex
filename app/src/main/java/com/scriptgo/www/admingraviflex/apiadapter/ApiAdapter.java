package com.scriptgo.www.admingraviflex.apiadapter;

import com.scriptgo.www.admingraviflex.apiservices.ApiServices;
import com.scriptgo.www.admingraviflex.constans.ConstansHelps;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class ApiAdapter {

    private static ApiServices API_SERVICE;

    public static ApiServices getApiService(){
        HttpLoggingInterceptor logging =  new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        String baseUrl = ConstansHelps.URL_BASE_API;

        if(API_SERVICE == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            API_SERVICE = retrofit.create(ApiServices.class);
        }

        return API_SERVICE;
    }
}
