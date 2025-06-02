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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gotit.data.model.ObjetoColeccion
import com.example.gotit.ui.components.TarjetaObjeto
import com.example.gotit.ui.components.DetalleObjeto
import com.example.gotit.viewmodel.ColeccionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaColeccion(
    viewModel: ColeccionViewModel,
    navController: NavController
) {
    val objetos by viewModel.objetos.collectAsState()
    var objetoSeleccionado by remember { mutableStateOf<ObjetoColeccion?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Filtros activos
    var categoriaSeleccionada by remember { mutableStateOf("Todas") }
    var precioRangoActivo by remember { mutableStateOf(0f..1000f) }
    var fechaInicioActiva by remember { mutableStateOf("") }
    var fechaFinActiva by remember { mutableStateOf("") }

    // Filtro temporales
    var categoriaTemp by remember { mutableStateOf("Todas") }
    var precioRangoTemp by remember { mutableStateOf(0f..1000f) }
    var fechaInicioTemp by remember { mutableStateOf("") }
    var fechaFinTemp by remember { mutableStateOf("") }

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
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(300.dp)
                    ) {
                        Text("Categoría", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(8.dp))
                        categoriasDisponibles.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria) },
                                onClick = {
                                    categoriaTemp = categoria
                                }
                            )
                        }

                        Divider()

                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Rango de precios (€):", style = MaterialTheme.typography.labelLarge)
                            RangeSlider(
                                value = precioRangoTemp,
                                onValueChange = { precioRangoTemp = it },
                                valueRange = 0f..1000f,
                                steps = 10
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("%.2f €".format(precioRangoTemp.start))
                                Text("%.2f €".format(precioRangoTemp.endInclusive))
                            }
                        }

                        Divider()

                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Desde (yyyy-MM-dd):", style = MaterialTheme.typography.labelLarge)
                            TextField(
                                value = fechaInicioTemp,
                                onValueChange = { fechaInicioTemp = it },
                                placeholder = { Text("2024-01-01") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Hasta (yyyy-MM-dd):", style = MaterialTheme.typography.labelLarge)
                            TextField(
                                value = fechaFinTemp,
                                onValueChange = { fechaFinTemp = it },
                                placeholder = { Text("2025-01-01") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Divider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = {
                                categoriaTemp = "Todas"
                                precioRangoTemp = 0f..1000f
                                fechaInicioTemp = ""
                                fechaFinTemp = ""
                            }) {
                                Text("Limpiar")
                            }
                            Button(onClick = {
                                categoriaSeleccionada = categoriaTemp
                                precioRangoActivo = precioRangoTemp
                                fechaInicioActiva = fechaInicioTemp
                                fechaFinActiva = fechaFinTemp
                                expanded = false
                            }) {
                                Text("Aplicar")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val objetosFiltrados = objetos.filter { objeto ->
                (categoriaSeleccionada == "Todas" || objeto.categoria == categoriaSeleccionada) &&
                        objeto.precio in precioRangoActivo &&
                        estaEnRangoDeFecha(objeto.fecha, fechaInicioActiva, fechaFinActiva)
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

private val fechaFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

private fun estaEnRangoDeFecha(fecha: String, inicio: String, fin: String): Boolean {
    return try {
        val fechaObj = LocalDate.parse(fecha, fechaFormatter)
        val inicioObj = if (inicio.isNotBlank()) LocalDate.parse(inicio, fechaFormatter) else null
        val finObj = if (fin.isNotBlank()) LocalDate.parse(fin, fechaFormatter) else null

        (inicioObj == null || !fechaObj.isBefore(inicioObj)) &&
                (finObj == null || !fechaObj.isAfter(finObj))
    } catch (e: Exception) {
        true 
    }
}
