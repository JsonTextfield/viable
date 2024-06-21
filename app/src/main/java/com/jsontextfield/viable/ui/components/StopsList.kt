package com.jsontextfield.viable.ui.components

import android.text.Html
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import com.jsontextfield.viable.R
import com.jsontextfield.viable.data.model.Stop

@Composable
fun StopsList(
    modifier: Modifier = Modifier,
    stops: List<Stop> = emptyList(),
    listState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(modifier = modifier, state = listState) {
        items(stops) { stop ->
            ListItem(
                modifier = Modifier.alpha(if (stop.eta != "ARR") 1f else 0.5f),
                headlineContent = { Text(stop.name) },
                supportingContent = {
                    Text(
                        if (stop.eta == "ARR") {
                            stringResource(id = R.string.departed)
                        }
                        else {
                            stringResource(
                                id = R.string.arrives_in,
                                Html.fromHtml(
                                    stop.eta,
                                    Html.FROM_HTML_MODE_LEGACY
                                ),
                            )
                        }
                    )
                },
            )
        }
    }
}