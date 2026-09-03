package com.example.semana2_trabajo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.semana2_trabajo.modelo.Equipo
import com.example.semana2_trabajo.network.RetrofitClient
import com.example.semana2_trabajo.ui.theme.Semana2_TrabajoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Semana2_TrabajoTheme {
                EquipoCrudApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipoCrudApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var equipos by remember { mutableStateOf(emptyList<Equipo>()) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }

    fun refreshList() {
        scope.launch {
            isLoading = true
            try {
                equipos = RetrofitClient.instance.obtenerEquipos()
            } catch (e: Exception) {
                Toast.makeText(context, "Error al cargar: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshList()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("CRUD Equipos de Fútbol") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Formulario
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Equipo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = pais,
                onValueChange = { pais = it },
                label = { Text("País") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    if (nombre.isNotBlank() && pais.isNotBlank()) {
                        scope.launch {
                            try {
                                val equipo = Equipo(nombre = nombre, pais = pais)
                                if (editingId == null) {
                                    RetrofitClient.instance.crearEquipo(equipo)
                                    Toast.makeText(context, "Equipo creado", Toast.LENGTH_SHORT).show()
                                } else {
                                    RetrofitClient.instance.actualizarEquipo(editingId!!, equipo)
                                    Toast.makeText(context, "Equipo actualizado", Toast.LENGTH_SHORT).show()
                                }
                                nombre = ""; pais = ""; editingId = null
                                refreshList()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.padding(top = 16.dp).align(Alignment.End)
            ) {
                Text(if (editingId == null) "Registrar Equipo" else "Guardar Cambios")
            }

            if (editingId != null) {
                TextButton(
                    onClick = { nombre = ""; pais = ""; editingId = null },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancelar Edición", color = Color.Red)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(equipos) { equipo ->
                        EquipoItem(
                            equipo = equipo,
                            onEdit = {
                                nombre = equipo.nombre
                                pais = equipo.pais
                                editingId = equipo.id
                            },
                            onDelete = {
                                scope.launch {
                                    try {
                                        equipo.id?.let { RetrofitClient.instance.eliminarEquipo(it) }
                                        Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
                                        refreshList()
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EquipoItem(equipo: Equipo, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = equipo.nombre, style = MaterialTheme.typography.titleLarge)
                Text(text = equipo.pais, style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Blue)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}
