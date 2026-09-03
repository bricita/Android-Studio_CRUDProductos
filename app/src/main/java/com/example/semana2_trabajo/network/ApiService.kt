package com.example.semana2_trabajo.network

import com.example.semana2_trabajo.modelo.Producto
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("productos")
    fun obtenerProductos(): Call<List<Producto>> // Usamos Call para que sea 100% compatible con Java y Kotlin

    @POST("productos")
    fun crearProducto(@Body producto: Producto): Call<Producto>

    @PUT("productos/{id}")
    fun actualizarProducto(@Path("id") id: Int, @Body producto: Producto): Call<Producto>

    @DELETE("productos/{id}")
    fun eliminarProducto(@Path("id") id: Int): Call<Void>
}