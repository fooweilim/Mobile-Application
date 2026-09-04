package com.example.githubdemo.admin.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.ui.theme.HarvestGreen

import kotlin.math.max

@Composable
fun SalesChart(
    selectedMonth: String,
    months: List<String>,
    sales: List<Float>,
    onMonthChange: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val textMeasurer =
        rememberTextMeasurer()

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
    ) {
        OutlinedButton(
            onClick = {
                expanded = true
            },
            modifier =
                Modifier.height(38.dp)
        ) {
            Text(
                text = selectedMonth,
                fontSize = 12.sp
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            months.forEach { month ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = month,
                            fontSize = 12.sp
                        )
                    },
                    onClick = {
                        onMonthChange(month)
                        expanded = false
                    }
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        if (sales.isEmpty()) {
            Text(
                text =
                    "No sales data available."
            )
            return@Column
        }

        Canvas(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(230.dp)
        ) {
            val safeSales =
                if (sales.size == 1) {
                    listOf(
                        sales.first(),
                        sales.first()
                    )
                } else {
                    sales
                }

            val originalMaximum =
                safeSales.maxOrNull()
                    ?: 0f

            val maximum =
                max(
                    originalMaximum,
                    10f
                )

            val chartHeight =
                size.height - 50f

            val chartWidth =
                size.width - 110f

            for (index in 0..4) {
                val value =
                    maximum -
                            maximum / 4f *
                            index

                drawText(
                    textMeasurer =
                        textMeasurer,
                    text =
                        value
                            .toInt()
                            .toString(),
                    topLeft =
                        Offset(
                            x = 5f,
                            y =
                                chartHeight /
                                        4f *
                                        index
                        ),
                    style =
                        TextStyle(
                            fontSize = 8.sp,
                            color =
                                Color.Gray
                        )
                )
            }

            val points =
                safeSales.mapIndexed {
                        index,
                        value ->

                    val horizontalGap =
                        chartWidth /
                                (
                                        safeSales.size -
                                                1
                                        )

                    Offset(
                        x =
                            45f +
                                    horizontalGap *
                                    index,
                        y =
                            chartHeight -
                                    value /
                                    maximum *
                                    (
                                            chartHeight -
                                                    20f
                                            )
                    )
                }

            val path =
                Path()

            points.forEachIndexed {
                    index,
                    point ->

                if (index == 0) {
                    path.moveTo(
                        x = point.x,
                        y = point.y
                    )
                } else {
                    path.lineTo(
                        x = point.x,
                        y = point.y
                    )
                }
            }

            drawPath(
                path = path,
                color =
                    HarvestGreen,
                style =
                    Stroke(width = 3f)
            )

            points.forEach { point ->
                drawCircle(
                    color =
                        HarvestGreen,
                    radius = 5f,
                    center = point
                )
            }

            points.forEachIndexed {
                    index,
                    point ->

                drawText(
                    textMeasurer =
                        textMeasurer,
                    text =
                        "Week ${index + 1}",
                    topLeft =
                        Offset(
                            x =
                                point.x - 20f,
                            y =
                                size.height -
                                        20f
                        ),
                    style =
                        TextStyle(
                            fontSize = 8.sp,
                            color =
                                Color.Gray
                        )
                )
            }
        }
    }
}