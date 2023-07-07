package com.example.apirecyclerview.api;

import com.example.apirecyclerview.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiService apiservice =  new Retrofit.Builder().baseUrl("https://64a448b6c3b509573b575985.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(ApiService.class);
    @GET("test")
    Call<List<User>> getListuser();

    @DELETE("test/{id}")
    Call<User> deleteUser(@Path("id") int id);

    @POST("test")
    Call<User> createUser(@Body User user);
    @PUT("test/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);
}
