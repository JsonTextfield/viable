package com.jsontextfield.viable.widget

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.padding
import com.jsontextfield.viable.R
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.network.TrainService
import com.jsontextfield.viable.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MyAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            GlanceTheme {
                var trains by remember { mutableStateOf(emptyList<Train>()) }
                val trainService = remember { TrainService() }
                LaunchedEffect(Unit) {
                    while (isActive){
                        trains = trainService.getTrains()
                        delay(30_000)
                    }
                }
                Scaffold(
                    titleBar = {
                        TitleBar(
                            title = context.getString(R.string.app_name),
                            startIcon = ImageProvider(R.drawable.ic_launcher_foreground),
                            modifier = GlanceModifier.clickable(actionStartActivity<MainActivity>()),
                        )
                    },
                ) {
                    LazyVerticalGrid(gridCells = GridCells.Fixed(1)) {
                        items(trains) { train ->
                            WidgetTrainListItem(train, modifier = GlanceModifier.padding(4.dp))
                        }
                    }
                }
            }
        }
    }
}