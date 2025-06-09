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
    var usarCategoriaExistente by remember { mutableStateOf(true) }
    var categoriaExistente by remember { mutableStateOf("") }
    var nuevaCategoria by remember { mutableStateOf("") }
    var precioTexto by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf("") }
    var expandedCategorias by remember { mutableStateOf(false) }

    val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imagenUri = uri?.toString() ?: ""
    }

    val categoriasDisponibles = viewModel.categoriasConocidas

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuevo objeto") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val categoriaFinal = if (usarCategoriaExistente) categoriaExistente.trim() else nuevaCategoria.trim()
                    val precioValido = precioTexto.toDoubleOrNull() != null && precioTexto.toDouble() >= 0.0
                    val camposValidos = nombre.isNotBlank() && categoriaFinal.isNotBlank() && precioValido

                    if (camposValidos) {
                        val precio = precioTexto.toDouble()
                        val objeto = ObjetoColeccion(
                            nombre = nombre,
                            descripcion = descripcion,
                            categoria = categoriaFinal,
                            fecha = fechaActual,
                            imagenUri = imagenUri,
                            precio = precio
                        )
                        viewModel.agregarObjeto(objeto)
                        Toast.makeText(context, "Objeto añadido", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        val mensajeError = when {
                            nombre.isBlank() -> "El nombre es obligatorio"
                            categoriaFinal.isBlank() -> "La categoría es obligatoria"
                            !precioValido -> "Introduce un precio válido (número positivo)"
                            else -> "Revisa los campos"
                        }
                        Toast.makeText(context, mensajeError, Toast.LENGTH_SHORT).show()
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

            // Switch para elegir modo de categoría
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Usar categoría existente")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = usarCategoriaExistente,
                    onCheckedChange = { usarCategoriaExistente = it }
                )
            }

            if (usarCategoriaExistente) {
                // Selección desplegable de categorías existentes
                ExposedDropdownMenuBox(
                    expanded = expandedCategorias,
                    onExpandedChange = { expandedCategorias = !expandedCategorias }
                ) {
                    OutlinedTextField(
                        value = categoriaExistente,
                        onValueChange = { categoriaExistente = it },
                        label = { Text("Categoría") },
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
                                    categoriaExistente = opcion
                                    expandedCategorias = false
                                }
                            )
                        }
                    }
                }
            } else {
                // Campo para escribir nueva categoría
                OutlinedTextField(
                    value = nuevaCategoria,
                    onValueChange = { nuevaCategoria = it },
                    label = { Text("Nueva categoría") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
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

            Text(
                text = "Fecha: $fechaActual",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
