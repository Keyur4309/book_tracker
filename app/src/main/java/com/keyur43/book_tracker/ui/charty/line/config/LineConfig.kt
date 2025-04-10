package com.keyur43.book_tracker.ui.charty.line.config

data class LineConfig(
    val hasSmoothCurve: Boolean = false,
    val hasDotMarker: Boolean = false
)

internal object LineConfigDefaults {

    fun lineConfigDefaults() = LineConfig(
        hasSmoothCurve = true,
        hasDotMarker = true
    )
}
