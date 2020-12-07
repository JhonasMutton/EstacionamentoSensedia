package com.estacionamento.api.parking

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkConfig {
    lateinit var context: Context
    private const val BASE_URL = "http://192.168.2.104:3000/"
    private val retrofit: Retrofit

    init {
        retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okhttpClient()) // Add our Okhttp client
            .build()
    }

    fun <T> provideApi(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    private fun okhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(40, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
//            .addInterceptor(AuthInterceptor(context))
            .build()
    }

    fun getRetrofitInstance() =  retrofit
}