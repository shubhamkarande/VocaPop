package com.vocapop.android.ui.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vocapop.android.ui.theme.*

@Composable
fun WelcomeScreen(
    onStartLearning: () -> Unit,
    onLoginClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Elements using simplistic circles for blur effect
            // In Compose, blur is expensive on older API, we can use gradients or canvas
            BackgroundBlobs()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top: Progress Indicators (Static for Welcome)
                Row(
                    modifier = Modifier.padding(top = 24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(32.dp)
                            .background(Primary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(32.dp)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(32.dp)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
                    )
                }

                // Middle: Animation & Text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "VocaPop",
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "SCIENCE-BACKED LEARNING",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(48.dp))
                    
                    FloatingHeroImage()
                    
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = "Pop a Card.\nLearn a Word.",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Get Fluent.",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        color = Primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Short sessions. Long-term memory.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Bottom: Actions
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Button(
                        onClick = onStartLearning,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Start Learning",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onLoginClick) {
                        Text(
                            text = "I already have an account",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BackgroundBlobs() {
    Box(modifier = Modifier.fillMaxSize()) {
         Box(
            modifier = Modifier
                .offset(x = (-50).dp, y = (-50).dp)
                .size(200.dp)
                .background(Primary.copy(alpha = 0.2f), CircleShape)
                .blur(100.dp) // Requires API 31+ for real blur, fallback handles gracefully
        )
    }
}

@Composable
fun FloatingHeroImage() {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val dy by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dy"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(280.dp)
            .graphicsLayer { translationY = dy }
    ) {
        // Back cards
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier
                .size(width = 160.dp, height = 200.dp)
                .rotate(-12f)
                .offset(x = (-30).dp, y = 10.dp)
                .alpha(0.4f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {}
        
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier
                .size(width = 160.dp, height = 200.dp)
                .rotate(12f)
                .offset(x = 30.dp, y = 10.dp)
                .alpha(0.4f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {}

        // Main Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            modifier = Modifier
                .size(width = 180.dp, height = 240.dp)
                .graphicsLayer { 
                    shadowElevation = 20f
                    shape = RoundedCornerShape(24.dp)
                },
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(SurfaceDark, Primary.copy(alpha = 0.2f))
                        )
                    )
            ) {
                 // Dots
                 Box(
                     modifier = Modifier
                         .padding(16.dp)
                         .size(12.dp)
                         .background(AccentOrange, CircleShape)
                         .align(Alignment.TopEnd)
                 )
                 
                 Box(
                     modifier = Modifier
                         .padding(24.dp)
                         .size(8.dp)
                         .background(Secondary, CircleShape)
                         .align(Alignment.BottomStart)
                 )

                 Column(
                     modifier = Modifier.align(Alignment.Center),
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     Box(
                         modifier = Modifier
                             .size(80.dp)
                             .background(Primary.copy(alpha = 0.2f), CircleShape),
                         contentAlignment = Alignment.Center
                     ) {
                         Icon(
                             imageVector = Icons.Outlined.Psychology,
                             contentDescription = null,
                             tint = Color.White,
                             modifier = Modifier.size(48.dp)
                         )
                     }
                     Spacer(modifier = Modifier.height(16.dp))
                     Box(
                         modifier = Modifier
                             .width(60.dp)
                             .height(8.dp)
                             .background(Color.White.copy(alpha = 0.2f), CircleShape)
                     )
                     Spacer(modifier = Modifier.height(8.dp))
                     Box(
                         modifier = Modifier
                             .width(30.dp)
                             .height(8.dp)
                             .background(Color.White.copy(alpha = 0.1f), CircleShape)
                     )
                 }
                 
                 Icon(
                     imageVector = Icons.Outlined.AutoAwesome,
                     contentDescription = "sparkle",
                     tint = AccentOrange,
                     modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 10.dp, y = (-10).dp)
                        .size(24.dp)
                 )
            }
        }
    }
}
