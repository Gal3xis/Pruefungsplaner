package com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit

import com.fachhochschulebib.fhb.pruefungsplaner.model.URLs.*
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper{
    fun getInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(serverResourcesUrl).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}