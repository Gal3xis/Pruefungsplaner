package com.fachhochschulebib.fhb.pruefungsplaner.model.retrofit

import com.fachhochschulebib.fhb.pruefungsplaner.model.URLs.*
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Helper class to create the access to the remote database via the [RetrofitInterface]
 *
 * @author Alexander Lange
 * @since 1.6
 */
object RetrofitHelper{
    /**
     * Returns a [Retrofit]-Object that provides access to the remote database.
     *
     * @return The [Retrofit]-Object with access to the remote database
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(serverResourcesUrl).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}