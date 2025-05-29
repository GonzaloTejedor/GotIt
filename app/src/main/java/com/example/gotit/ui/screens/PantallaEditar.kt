package com.example.gotit.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.gotit.viewmodel.ColeccionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditar(
    objetoId: Int,
    navController: NavController,
    viewModel: ColeccionViewModel
) {
    val context = LocalContext.current
    val objeto = viewModel.objetos.collectAsState().value.find { it.id == objetoId }

    if (objeto == null) {
        Text("Objeto no encontrado", modifier = Modifier.padding(16.dp))
        return
    }

    var nombre by remember { mutableStateOf(objeto.nombre) }
    var descripcion by remember { mutableStateOf(objeto.descripcion) }
    var categoria by remember { mutableStateOf(objeto.categoria) }
    var precioTexto by remember { mutableStateOf(objeto.precio.toString()) }
    var imagenUri by remember { mutableStateOf(objeto.imagenUri) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imagenUri = uri?.toString() ?: imagenUri
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Editar objeto") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (nombre.isNotBlank()) {
                        val precio = precioTexto.toDoubleOrNull() ?: 0.0
                        val objetoEditado = objeto.copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            categoria = categoria,
                            precio = precio,
                            imagenUri = imagenUri
                        )
                        viewModel.actualizarObjeto(objetoEditado)
                        Toast.makeText(context, "Objeto actualizado", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Save, contentDescription = "Guardar")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precioTexto,
                onValueChange = { precioTexto = it },
                label = { Text("Precio estimado (€)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Imagen:", style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(Icons.Default.Photo, contentDescription = "Cambiar imagen")
                }
            }

            if (imagenUri.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(imagenUri),
                    contentDescription = "Imagen del objeto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}
