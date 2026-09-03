package com.example.semana2_trabajo.network

import com.example.semana2_trabajo.modelo.Equipo
import retrofit2.http.*

interface ApiService {
    @GET("equipos")
    suspend fun obtenerEquipos(): List<Equipo>

    @POST("equipos")
    suspend fun crearEquipo(@Body equipo: Equipo): Equipo

    @PUT("equipos/{id}")
    suspend fun actualizarEquipo(@Path("id") id: String, @Body equipo: Equipo): Equipo

    @DELETE("equipos/{id}")
    suspend fun eliminarEquipo(@Path("id") id: String)
}
