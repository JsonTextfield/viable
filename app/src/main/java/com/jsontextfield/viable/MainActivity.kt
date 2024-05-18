package com.jsontextfield.viable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jsontextfield.viable.ui.components.MainScreen
import com.jsontextfield.viable.ui.theme.ViableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { ViableTheme { MainScreen() } }
    }
}