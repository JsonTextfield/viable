package com.jsontextfield.viable

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase

fun String.toMixedCase(): String {
    return toLowerCase(Locale.current)
        .split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
}

fun String.replaceHtmlEntities(): String {
    return this.replace("&mdash;", "—")
}