package com.keyur43.book_tracker.ui.charty.line.config

import androidx.compose.ui.graphics.Color

data class CurveLineConfig(
    val hasDotMarker: Boolean,
    val dotColor: Color = Color.Transparent,
)

internal object CurveLineConfigDefaults {

    fun curveLineConfigDefaults() = CurveLineConfig(
        hasDotMarker = true,
        dotColor = Color.Green
    )
}
