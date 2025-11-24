package com.dmc.goyiyi.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.76:5001/goyiyi-dffc9/us-central1/"

    // --- CLIENT PROFESIONAL ----
    private val client: OkHttpClient by lazy {

        // Logging del request/response (solamente para desarrollo)
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(25, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)
            .build()
    }

    // --- RETROFIT ---
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // <--- agregado para logging + timeouts
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
