package com.example.gotit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gotit.data.model.ObjetoColeccion

@Composable
fun DetalleObjeto(
    objeto: ObjetoColeccion,
    onEditar: (ObjetoColeccion) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Categoría
        Text(
            text = objeto.categoria,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Imagen si existe
        if (objeto.imagenUri.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(objeto.imagenUri),
                contentDescription = "Imagen del objeto",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
            )
        }

        // Nombre del objeto
        Text(
            text = objeto.nombre,
            style = MaterialTheme.typography.headlineSmall
        )

        Divider()

        // Descripción
        Text("Descripción", style = MaterialTheme.typography.labelMedium)
        Text(text = objeto.descripcion)

        // Precio
        Text("Precio estimado: ${objeto.precio} €", style = MaterialTheme.typography.bodyMedium)

        // Fecha
        Text("Fecha: ${objeto.fecha}", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de editar
        Button(onClick = { onEditar(objeto) }) {
            Text("Editar")
        }
    }
}
