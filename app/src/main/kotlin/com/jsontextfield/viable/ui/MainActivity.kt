package com.jsontextfield.viable.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jsontextfield.viable.ui.components.MainScreen
import com.jsontextfield.viable.ui.theme.ViableTheme
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViableTheme {
                MainScreen(koinViewModel())
            }
        }
    }
}