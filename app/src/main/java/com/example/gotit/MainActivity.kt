package com.example.gotit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.gotit.ui.navigation.AppNavigation
import com.example.gotit.ui.theme.GotItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GotItTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}
