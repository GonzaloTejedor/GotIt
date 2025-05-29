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
import com.example.gotit.data.model.ObjetoColeccion
import com.example.gotit.viewmodel.ColeccionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregar(
    navController: NavController,
    viewModel: ColeccionViewModel
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var precioTexto by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<String>("") }

    val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    // Selector de imagen
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imagenUri = uri?.toString() ?: ""
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuevo objeto") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (nombre.isNotBlank()) {
                        val precio = precioTexto.toDoubleOrNull() ?: 0.0
                        val objeto = ObjetoColeccion(
                            nombre = nombre,
                            descripcion = descripcion,
                            categoria = categoria,
                            fecha = fechaActual,
                            imagenUri = imagenUri,
                            precio = precio
                        )
                        viewModel.agregarObjeto(objeto)
                        Toast.makeText(context, "Objeto añadido", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Guardar")
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
            Text("Añadir nuevo objeto", style = MaterialTheme.typography.headlineSmall)

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

            // Imagen
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(Icons.Default.Photo, contentDescription = "Seleccionar imagen")
                }
                Text("Añadir imagen", style = MaterialTheme.typography.bodyMedium)
            }

            if (imagenUri.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(imagenUri),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 8.dp)
                )
            }

            // Fecha
            Text(
                text = "Fecha: $fechaActual",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}