package com.estacionamento.api.carrorama

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkConfig {
    lateinit var context: Context
    private const val BASE_URL = "https://starterapi.staging.g2fleet.com/carroramafleet/ws/"
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