package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import com.jsontextfield.viable.data.model.Stop
import com.jsontextfield.viable.replaceHtmlEntities
import org.jetbrains.compose.resources.stringResource
import viable.composeapp.generated.resources.Res
import viable.composeapp.generated.resources.arrives_in
import viable.composeapp.generated.resources.departed

@Composable
fun StopListItem(
    stop: Stop,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            stop.name,
            modifier = Modifier.padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(LayoutDirection.Ltr),
            )
        )
        Text(
            if (stop.eta == "ARR") {
                stringResource(Res.string.departed)
            } else {
                stringResource(
                    Res.string.arrives_in,
                    stop.eta.replaceHtmlEntities(),
                )
            },
            modifier = Modifier.padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(LayoutDirection.Ltr)
            ),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}