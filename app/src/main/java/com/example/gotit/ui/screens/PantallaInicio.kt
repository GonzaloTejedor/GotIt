package com.example.gotit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PantallaInicio(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Puedes reemplazar esto por un logo con Image()
            Text(
                text = "GotIt!",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController.navigate("coleccion") },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Iniciar")
            }
        }
    }
}
