package com.keyur43.book_tracker.ui.charty.common.axis

import androidx.compose.ui.graphics.Color

data class AxisConfig(
    val showAxis: Boolean,
    val isAxisDashed: Boolean,
    val showUnitLabels: Boolean,
    val showXLabels: Boolean,
    val xAxisColor: Color = Color.LightGray,
    val yAxisColor: Color = Color.LightGray,
)

internal object AxisConfigDefaults {

    fun axisConfigDefaults() = AxisConfig(
        xAxisColor = Color.LightGray,
        showAxis = true,
        isAxisDashed = false,
        showUnitLabels = true,
        showXLabels = true,
        yAxisColor = Color.LightGray,
    )
}
