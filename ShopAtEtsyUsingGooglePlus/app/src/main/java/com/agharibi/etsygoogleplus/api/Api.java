package com.agharibi.etsygoogleplus.api;

import com.agharibi.etsygoogleplus.model.ActiveListings;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


public interface Api {

    @GET("/listings/active")
    public void activeListings(@Query("includes") String includes, Callback<ActiveListings> callback);
}
