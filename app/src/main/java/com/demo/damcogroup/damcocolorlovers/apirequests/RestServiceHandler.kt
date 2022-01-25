package com.demo.damcogroup.damcocolorlovers.apirequests

import DataModel
import com.demo.damcogroup.damcocolorlovers.utils.baseUrl
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface RestServiceHandler {

    @Headers(
            "Content-type: application/json"
    )

    @GET("colors ")
    fun getListOfColors(@Query("keywords") keyword: String, @Query("format") format: String, @Query("numResults") numResults: String): Observable<List<DataModel>>

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
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .build()
            return retrofit.create(
                    RestServiceHandler::class.java
            )
        }
    }
}