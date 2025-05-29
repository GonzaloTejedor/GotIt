package com.example.gotit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gotit.data.model.ObjetoColeccion
import com.example.gotit.ui.components.TarjetaObjeto
import com.example.gotit.ui.components.DetalleObjeto
import com.example.gotit.viewmodel.ColeccionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaColeccion(
    viewModel: ColeccionViewModel,
    navController: NavController
) {
    val objetos by viewModel.objetos.collectAsState()
    var objetoSeleccionado by remember { mutableStateOf<ObjetoColeccion?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var categoriaSeleccionada by remember { mutableStateOf("Todas") }
    val categoriasDisponibles = listOf("Todas") + objetos.map { it.categoria }.distinct()

    val totalArticulos = objetos.size
    val totalValor = objetos.sumOf { it.precio }

    if (objetoSeleccionado != null) {
        ModalBottomSheet(
            onDismissRequest = { objetoSeleccionado = null },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            objetoSeleccionado?.let {
                DetalleObjeto(
                    objeto = it,
                    onEditar = { objeto ->
                        objetoSeleccionado = null
                        navController.navigate("editar/${objeto.id}")
                    }
                )
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("agregar") },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar objeto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // CABECERA CON TÍTULO, RESUMEN Y FILTRO
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Colecciones", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "Total: %.2f € · $totalArticulos artículos".format(totalValor),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                var expanded by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categoriasDisponibles.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria) },
                                onClick = {
                                    categoriaSeleccionada = categoria
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val objetosFiltrados = if (categoriaSeleccionada == "Todas") {
                objetos
            } else {
                objetos.filter { it.categoria == categoriaSeleccionada }
            }

            if (objetosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay objetos en esta categoría")
                }
            } else {
                val objetosAgrupados = objetosFiltrados.groupBy { it.categoria }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    objetosAgrupados
                        .toList()
                        .sortedByDescending { (_, lista) -> lista.sumOf { it.precio } }
                        .forEach { (categoria, lista) ->
                            item {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "— $categoria —",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Total: %.2f €".format(lista.sumOf { it.precio }),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "Artículos: ${lista.size}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }

                            items(lista) { objeto ->
                                TarjetaObjeto(
                                    objeto = objeto,
                                    onClick = { objetoSeleccionado = objeto },
                                    onEliminar = { viewModel.eliminarObjeto(objeto) }
                                )
                            }
                        }
                }
            }
        }
    }
}
