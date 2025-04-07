package com.keyur43.book_tracker.ui.charty.line

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.keyur43.book_tracker.ui.charty.common.axis.AxisConfig
import com.keyur43.book_tracker.ui.charty.common.axis.AxisConfigDefaults
import com.keyur43.book_tracker.ui.charty.common.axis.yAxis
import com.keyur43.book_tracker.ui.charty.common.dimens.ChartDimens
import com.keyur43.book_tracker.ui.charty.common.dimens.ChartDimensDefaults
import com.keyur43.book_tracker.ui.charty.line.config.LineConfig
import com.keyur43.book_tracker.ui.charty.line.config.LineConfigDefaults
import com.keyur43.book_tracker.ui.charty.line.model.LineData
import com.keyur43.book_tracker.ui.charty.line.model.maxYValue

@Composable
fun LineChart(
    lineData: List<LineData>,
    color: Color,
    modifier: Modifier = Modifier,
    chartDimens: ChartDimens = ChartDimensDefaults.chartDimesDefaults(),
    axisConfig: AxisConfig = AxisConfigDefaults.axisConfigDefaults(),
    lineConfig: LineConfig = LineConfigDefaults.lineConfigDefaults()
) {
    LineChart(
        lineData = lineData,
        colors = listOf(color, color),
        modifier = modifier,
        chartDimens = chartDimens,
        axisConfig = axisConfig,
        lineConfig = lineConfig
    )
}

@Composable
fun LineChart(
    lineData: List<LineData>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    chartDimens: ChartDimens = ChartDimensDefaults.chartDimesDefaults(),
    axisConfig: AxisConfig = AxisConfigDefaults.axisConfigDefaults(),
    lineConfig: LineConfig = LineConfigDefaults.lineConfigDefaults()
) {
    val maxYValueState =
        rememberSaveable { mutableFloatStateOf(lineData.maxYValue()) }
    val maxYValue = maxYValueState.floatValue
    val lineBound = remember { mutableFloatStateOf(0F) }
    val labelColor = MaterialTheme.colorScheme.onSurface

    Canvas(
        modifier = modifier
            .drawBehind {
                if (axisConfig.showAxis) {
                    yAxis(axisConfig, maxYValue, labelColor = labelColor)
                }
            }
            .padding(horizontal = chartDimens.padding)

    ) {
        lineBound.floatValue = size.width.div(lineData.count().times(1.2F))
        val scaleFactor = size.height.div(maxYValue)
        val brush = Brush.linearGradient(colors)
        val radius = size.width.div(70)
        val strokeWidth = size.width.div(100)
        val path = Path().apply {
            moveTo(0f, size.height)
        }

        lineData.forEachIndexed { index, data ->
            val centerOffset = dataToOffSet(index, lineBound.floatValue, size, data, scaleFactor)
            when (index) {
                0 -> path.moveTo(centerOffset.x, centerOffset.y)
                else -> path.lineTo(centerOffset.x, centerOffset.y)
            }
            if (lineConfig.hasDotMarker) {
                drawCircle(
                    center = centerOffset,
                    radius = radius,
                    brush = brush
                )
            }
            if (axisConfig.showXLabels) {
                drawXLabel(data, centerOffset, radius, lineData.count(), labelColor)
            }
        }
        val pathEffect =
            if (lineConfig.hasSmoothCurve) PathEffect.cornerPathEffect(strokeWidth) else null
        drawPath(
            path = path,
            brush = brush,
            style = Stroke(width = strokeWidth, pathEffect = pathEffect),
        )
    }
}

private fun DrawScope.drawXLabel(
    data: LineData,
    centerOffset: Offset,
    radius: Float,
    count: Int,
    labelColor: Color = Color.Black
) {
    val divisibleFactor = if (count > 10) count else 1
    val textSizeFactor = if (count > 10) 3 else 30
    drawIntoCanvas {
        it.nativeCanvas.apply {
            drawText(
                data.xValue.toString(),
                centerOffset.x,
                size.height.plus(radius.times(4)),
                Paint().apply {
                    textSize = size.width.div(textSizeFactor).div(divisibleFactor)
                    textAlign = Paint.Align.CENTER
                    color = labelColor.toArgb()
                }
            )
        }
    }
}

private fun dataToOffSet(
    index: Int,
    bound: Float,
    size: Size,
    data: LineData,
    yScaleFactor: Float
): Offset {
    val startX = index.times(bound.times(1.2F))
    val endX = index.plus(1).times(bound.times(1.2F))
    val y = size.height.minus(data.yValue.times(yScaleFactor))
    return Offset(((startX.plus(endX)).div(2F)), y)
}
