package com.example.semana2_trabajo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Si pruebas con el emulador de Android Studio, 10.0.2.2 apunta al localhost de tu PC
    private const val BASE_URL = "http://localhost:3000"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}