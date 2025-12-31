package com.vocapop.android.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vocapop.android.ui.theme.BackgroundDark
import com.vocapop.android.ui.theme.Primary
import com.vocapop.android.ui.theme.SurfaceDark

data class GoalOption(val label: String, val words: Int, val description: String)

val goalOptions = listOf(
    GoalOption("Casual", 5, "5 words / day"),
    GoalOption("Regular", 10, "10 words / day"),
    GoalOption("Serious", 20, "20 words / day"),
    GoalOption("Intense", 50, "50 words / day")
)

@Composable
fun DailyGoalScreen(
    onGoalSelected: (Int) -> Unit,
    onBack: () -> Unit
) {
    var selectedGoal by remember { mutableStateOf<Int?>(10) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .systemBarsPadding()
        ) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Progress Indicators
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.height(6.dp).width(32.dp).background(Primary, CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.height(6.dp).width(32.dp).background(Primary, CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.height(6.dp).width(32.dp).background(Primary, CircleShape))
            }

            Text(
                text = "Pick a daily goal.",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "You can always change this later.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                goalOptions.forEach { option ->
                    GoalOptionCard(
                        option = option,
                        isSelected = option.words == selectedGoal,
                        onClick = { selectedGoal = option.words }
                    )
                }
            }

            Button(
                onClick = { selectedGoal?.let { onGoalSelected(it) } },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun GoalOptionCard(
    option: GoalOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Primary.copy(alpha = 0.1f) else SurfaceDark
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Primary) else null,
        modifier = Modifier.fillMaxWidth().height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = option.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = option.description,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
