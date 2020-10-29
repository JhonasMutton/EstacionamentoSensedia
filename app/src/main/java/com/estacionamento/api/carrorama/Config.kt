package com.estacionamento.api.carrorama

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//class Config {
//    companion object {
//        fun getRetrofitInstance(path : String) : Retrofit {
//            return Retrofit.Builder()
//                .baseUrl(path)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//        }
//    }
//}
object Config {
    lateinit var context: Context
    private const val BASE_URL = "http://supermock.demo.sensedia.com/"

    fun <T> provideApi(clazz: Class<T>, context: Context?): T {

        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okhttpClient(context)) // Add our Okhttp client
            .build()

        return retrofit.create(clazz)
    }

    private fun okhttpClient(context: Context?): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(40, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
//            .addInterceptor(AuthInterceptor(context))
            .build()
    }
}