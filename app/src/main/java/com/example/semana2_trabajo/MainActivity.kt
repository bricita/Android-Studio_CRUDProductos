package com.example.semana2_trabajo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.semana2_trabajo.modelo.Producto
import com.example.semana2_trabajo.network.RetrofitClient

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Ejemplos de uso de las operaciones CRUD:

        // 1. LEER (Read)
        cargarProductos()

        // 2. CREAR (Create)
        val nuevo = Producto(1,"Teclado Mecánico", 45.99)
        crearProducto(nuevo)
    }

    private fun cargarProductos() {
        // 1. Creamos la petición de red (el objeto Call)
        val call = RetrofitClient.instance.obtenerProductos()

        // 2. Lo ejecutamos de forma asíncrona en segundo plano automáticamente (.enqueue)
        call.enqueue(object : retrofit2.Callback<List<Producto>> {

            // Esta función se ejecuta si el servidor responde (exitoso o con error de servidor)
            override fun onResponse(
                call: retrofit2.Call<List<Producto>>,
                response: retrofit2.Response<List<Producto>>
            ) {
                if (response.isSuccessful) {
                    val lista = response.body()
                    // Aquí ya estás en el hilo principal de la UI de forma segura
                    Log.d("CRUD", "Productos: $lista")
                } else {
                    Toast.makeText(this@MainActivity, "Error al cargar", Toast.LENGTH_SHORT).show()
                }
            }

            // Esta función se ejecuta si no hay internet o el servidor está apagado
            override fun onFailure(call: retrofit2.Call<List<Producto>>, t: Throwable) {
                Log.e("CRUD_ERROR", t.message ?: "Error de conexión")
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun crearProducto(producto: Producto) {
        RetrofitClient.instance.crearProducto(producto).enqueue(object : retrofit2.Callback<Producto> {
            override fun onResponse(call: retrofit2.Call<Producto>, response: retrofit2.Response<Producto>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "¡Creado con éxito!", Toast.LENGTH_SHORT).show()
                    cargarProductos() // Recargar lista automáticamente
                } else {
                    Toast.makeText(this@MainActivity, "Error al crear producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Producto>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de red al crear", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun editarProducto(id: Int, productoActualizado: Producto) {
        RetrofitClient.instance.actualizarProducto(id, productoActualizado).enqueue(object : retrofit2.Callback<Producto> {
            override fun onResponse(call: retrofit2.Call<Producto>, response: retrofit2.Response<Producto>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Actualizado con éxito", Toast.LENGTH_SHORT).show()
                    cargarProductos()
                } else {
                    Toast.makeText(this@MainActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Producto>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de red al actualizar", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun eliminarProducto(id: Int) {
        RetrofitClient.instance.eliminarProducto(id).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Eliminado con éxito", Toast.LENGTH_SHORT).show()
                    cargarProductos()
                } else {
                    Toast.makeText(this@MainActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de red al eliminar", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
