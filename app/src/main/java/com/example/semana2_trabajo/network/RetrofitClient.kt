package com.example.semana2_trabajo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // REEMPLAZAR con tu URL de MockAPI (ejemplo: https://66d5...mockapi.io/)
    private const val BASE_URL = "https://your-mockapi-url.com/" 

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
