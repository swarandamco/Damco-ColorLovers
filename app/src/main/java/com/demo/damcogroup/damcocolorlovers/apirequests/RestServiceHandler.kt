package com.demo.damcogroup.damcocolorlovers.apirequests

import ColorDataModel
import com.demo.damcogroup.damcocolorlovers.utils.BASE_URL
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RestServiceHandler {

    @Headers(
        "Content-type: application/json"
    )

    // get api call using retrofit
    @GET("colors ")
    fun getColourList(
        @Query("keywords") keyword: String,
        @Query("format") format: String,
        @Query("numResults") numResults: String
    ): Observable<List<ColorDataModel>>

    companion object {

        fun create(): RestServiceHandler {

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
            return retrofit.create(
                RestServiceHandler::class.java
            )
        }
    }
}