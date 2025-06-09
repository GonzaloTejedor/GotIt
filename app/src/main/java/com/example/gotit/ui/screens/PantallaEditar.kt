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
    var expandedCategorias by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imagenUri = uri?.toString() ?: imagenUri
    }

    val categoriasDisponibles = viewModel.categoriasConocidas

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Editar objeto") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val precioValido = precioTexto.toDoubleOrNull() != null && precioTexto.toDouble() >= 0.0
                    val camposValidos = nombre.isNotBlank() && categoria.isNotBlank() && precioValido

                    if (camposValidos) {
                        val precio = precioTexto.toDouble()
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
                        val mensajeError = when {
                            nombre.isBlank() -> "El nombre es obligatorio"
                            categoria.isBlank() -> "La categoría es obligatoria"
                            !precioValido -> "Introduce un precio válido (número positivo)"
                            else -> "Revisa los campos"
                        }
                        Toast.makeText(context, mensajeError, Toast.LENGTH_SHORT).show()
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
            Text("Editar objeto", style = MaterialTheme.typography.headlineSmall)

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

            // Categoría sólo selección, no editable
            ExposedDropdownMenuBox(
                expanded = expandedCategorias,
                onExpandedChange = { expandedCategorias = !expandedCategorias }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { /* No permitir editar */ },
                    label = { Text("Categoría") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategorias)
                    },
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expandedCategorias,
                    onDismissRequest = { expandedCategorias = false }
                ) {
                    categoriasDisponibles.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                categoria = opcion
                                expandedCategorias = false
                            }
                        )
                    }
                }
            }

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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(Icons.Default.Photo, contentDescription = "Seleccionar imagen")
                }
                Text("Cambiar imagen", style = MaterialTheme.typography.bodyMedium)
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
        }
    }
}