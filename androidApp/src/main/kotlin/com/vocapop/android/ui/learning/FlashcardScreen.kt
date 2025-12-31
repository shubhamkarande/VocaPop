package com.vocapop.android.ui.learning

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vocapop.android.ui.theme.*
import com.vocapop.domain.models.CardWithProgress
import com.vocapop.domain.srs.ReviewGrade
import org.koin.androidx.compose.koinViewModel

@Composable
fun FlashcardScreen(
    onSessionComplete: () -> Unit,
    onBack: () -> Unit,
    viewModel: SessionViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSessionComplete) {
        if (state.isSessionComplete) {
            onSessionComplete()
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentCard = state.cards.getOrNull(state.currentCardIndex) ?: return

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
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.Close, "Close", tint = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { (state.currentCardIndex.toFloat() / state.cards.size.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier.weight(1f).height(8.dp),
                    color = Primary,
                    trackColor = SurfaceDark
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { /* Settings */ }) {
                    Icon(Icons.Default.MoreVert, "Settings", tint = MaterialTheme.colorScheme.onSurface)
                }
            }

            // Card Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Flip Animation
                val rotation by animateFloatAsState(
                    targetValue = if (state.isCardFlipped) 180f else 0f,
                    animationSpec = tween(400)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight() // Fill available space
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        },
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftOrange)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (rotation <= 90f) {
                            // Front Content
                            FrontCardContent(
                                card = currentCard, 
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // Back Content (Rotated back to normal)
                            Box(modifier = Modifier.fillMaxSize().graphicsLayer { rotationY = 180f }) {
                                BackCardContent(
                                    card = currentCard,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }

            // Controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // Fixed height for controls
                contentAlignment = Alignment.Center
            ) {
                if (!state.isCardFlipped) {
                    Button(
                        onClick = { viewModel.flipCard() },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Show Answer", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GradingButton(
                            text = "Again",
                            subtext = "1m",
                            color = AccentRed,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.submitGrade(ReviewGrade.AGAIN) }
                        )
                        GradingButton(
                            text = "Hard",
                            subtext = "10m",
                            color = AccentOrange,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.submitGrade(ReviewGrade.HARD) }
                        )
                        GradingButton(
                            text = "Good",
                            subtext = "1d",
                            color = Secondary,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.submitGrade(ReviewGrade.GOOD) }
                        )
                        GradingButton(
                            text = "Easy",
                            subtext = "4d",
                            color = AccentTeal, 
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.submitGrade(ReviewGrade.EASY) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FrontCardContent(card: CardWithProgress, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
         // Language Tag
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                "French", // Hardcoded for now
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7C5E43)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(modifier = Modifier.size(4.dp).background(Color(0xFF7C5E43).copy(alpha = 0.4f), CircleShape))
            Spacer(modifier = Modifier.width(4.dp))
             Text(
                card.card.wordType ?: "Word",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF7C5E43),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = card.card.front,
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
            color = Color(0xFF101022),
            textAlign = TextAlign.Center,
            lineHeight = 44.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (card.card.exampleSentence != null) {
            Text(
                text = "\"${card.card.exampleSentence}\"",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF5A483A),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Icon(
            Icons.Default.TouchApp, 
            null, 
            tint = Color(0xFF7C5E43).copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
    
    // Audio Button Absolute Position
    Box(modifier = modifier.padding(24.dp)) {
        IconButton(
            onClick = { /* TTS */ },
            modifier = Modifier.align(Alignment.TopEnd).background(Color.Black.copy(alpha = 0.05f), CircleShape)
        ) {
            Icon(Icons.Default.VolumeUp, null, tint = Color(0xFF5A483A))
        }
    }
}

@Composable
fun BackCardContent(card: CardWithProgress, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
         Text(
            text = card.card.front,
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF101022).copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = card.card.back,
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
            color = Primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Divider(color = Color.Black.copy(alpha = 0.05f))
        
        Spacer(modifier = Modifier.height(32.dp))

        if (card.card.exampleSentence != null) {
            // Highlight word in sentence logic would go here
            Text(
                text = card.card.exampleSentence!!,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF5A483A),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GradingButton(
    text: String,
    subtext: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.1f),
            contentColor = color
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxHeight(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Text(subtext, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Normal, color = color.copy(alpha = 0.7f))
        }
    }
}
