package com.vocapop.android.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vocapop.android.ui.theme.*

data class DayActivity(
    val day: String,
    val cardsReviewed: Int,
    val isToday: Boolean = false
)

@Composable
fun ActivityChart(
    modifier: Modifier = Modifier,
    weekData: List<DayActivity> = getDefaultWeekData()
) {
    val maxCards = weekData.maxOfOrNull { it.cardsReviewed }?.coerceAtLeast(1) ?: 1

    Column(
        modifier = modifier
            .background(SurfaceDark, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Text(
            text = "This Week",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            weekData.forEach { day ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f)
                ) {
                    // Bar
                    val barHeight = if (day.cardsReviewed > 0) {
                        (day.cardsReviewed.toFloat() / maxCards * 80).dp
                    } else {
                        4.dp
                    }

                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(barHeight)
                            .background(
                                brush = if (day.isToday) {
                                    Brush.verticalGradient(
                                        colors = listOf(Primary, Secondary)
                                    )
                                } else if (day.cardsReviewed > 0) {
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Primary.copy(alpha = 0.6f),
                                            Primary.copy(alpha = 0.3f)
                                        )
                                    )
                                } else {
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            SurfaceDark.copy(alpha = 0.5f),
                                            SurfaceDark.copy(alpha = 0.5f)
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Day label
                    Text(
                        text = day.day,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (day.isToday) Primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Primary, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Cards Reviewed",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun getDefaultWeekData(): List<DayActivity> {
    return listOf(
        DayActivity("Mon", 15),
        DayActivity("Tue", 22),
        DayActivity("Wed", 8),
        DayActivity("Thu", 30),
        DayActivity("Fri", 18),
        DayActivity("Sat", 25),
        DayActivity("Sun", 12, isToday = true)
    )
}
