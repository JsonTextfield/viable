package com.jsontextfield.viable.widget

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.layout.Column
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import com.jsontextfield.viable.R
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.data.model.replaceHtmlEntities

@Composable
fun WidgetTrainListItem(
    train: Train,
    modifier: GlanceModifier = GlanceModifier,
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        Text(
            text = train.name,
            style = TextDefaults.defaultTextStyle.copy(
                fontWeight = FontWeight.Bold,
                color = GlanceTheme.colors.onBackground,
            ),
        )
        Text(
            text = when {
                train.arrived -> {
                    context.getString(R.string.arrived)
                }

                train.nextStop != null -> {
                    context.getString(
                        R.string.next_stop, train.nextStop.name,
                        train.nextStop.eta.replaceHtmlEntities(),
                    )
                }

                train.departed -> {
                    context.getString(R.string.departed)
                }

                else -> {
                    ""
                }
            },
            style = TextDefaults.defaultTextStyle.copy(
                color = GlanceTheme.colors.onBackground,
            ),
        )
    }
}