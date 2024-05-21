package com.jsontextfield.viable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.jsontextfield.viable.ui.ViableViewModel
import com.jsontextfield.viable.ui.components.MainScreen
import com.jsontextfield.viable.ui.theme.ViableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viableViewModel: ViableViewModel by viewModels { ViableViewModel.ViableViewModelFactory }
        enableEdgeToEdge()
        setContent { ViableTheme { MainScreen(viableViewModel) } }
    }
}