package com.example.gotit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gotit.data.model.ObjetoColeccion

@Composable
fun TarjetaObjeto(
    objeto: ObjetoColeccion,
    onClick: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (objeto.imagenUri.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(objeto.imagenUri),
                    contentDescription = "Imagen del objeto",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 16.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(objeto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Categoría: ${objeto.categoria}", style = MaterialTheme.typography.bodySmall)
                Text("Fecha: ${objeto.fecha}", style = MaterialTheme.typography.bodySmall)
                Text("Precio: %.2f €".format(objeto.precio), style = MaterialTheme.typography.bodySmall)
            }

            IconButton(
                onClick = onEliminar,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
