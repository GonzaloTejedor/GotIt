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
import androidx.compose.ui.layout.ContentScale
import com.example.gotit.R
import androidx.compose.ui.graphics.Color

@Composable
fun PantallaInicio(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.mi_logo),
                contentDescription = "Logo de GotIt",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.Fit
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GotIt!",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1565C0) // azul fuerte

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
}