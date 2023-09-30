package com.example.apptesttmbd.data.api

import com.example.apptesttmbd.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object{
        private var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build()


            retrofit  =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.API_SERVER_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())

                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit
        }
    }
}