package com.vocapop.android.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vocapop.android.ui.theme.BackgroundDark
import com.vocapop.android.ui.theme.Primary
import com.vocapop.android.ui.theme.SurfaceDark
import com.vocapop.android.ui.theme.SurfaceLightDark

data class LanguageOption(val id: String, val name: String, val flag: String)

val languages = listOf(
    LanguageOption("es", "Spanish", "🇪🇸"),
    LanguageOption("fr", "French", "🇫🇷"),
    LanguageOption("de", "German", "🇩🇪"),
    LanguageOption("ja", "Japanese", "🇯🇵"),
    LanguageOption("it", "Italian", "🇮🇹"),
    LanguageOption("pt", "Portuguese", "🇧🇷"),
    LanguageOption("ru", "Russian", "🇷🇺"),
    LanguageOption("zh", "Chinese", "🇨🇳")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionScreen(
    onLanguageSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    var selectedId by remember { mutableStateOf<String?>(null) }

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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            // Progress Indicators
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.height(6.dp).width(32.dp).background(Primary, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.height(6.dp).width(32.dp).background(Primary, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.height(6.dp).width(32.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
                )
            }

            Text(
                text = "What do you want to learn?",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(languages) { language ->
                    LanguageCard(
                        language = language,
                        isSelected = language.id == selectedId,
                        onClick = { selectedId = language.id }
                    )
                }
            }

            Button(
                onClick = { selectedId?.let { onLanguageSelected(it) } },
                enabled = selectedId != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    disabledContainerColor = SurfaceLightDark
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (selectedId != null) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun LanguageCard(
    language: LanguageOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Primary.copy(alpha = 0.1f) else SurfaceDark
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Primary) else null,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = language.flag,
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = language.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
