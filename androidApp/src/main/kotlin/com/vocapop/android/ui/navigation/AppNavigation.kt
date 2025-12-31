package com.vocapop.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vocapop.android.ui.onboarding.DailyGoalScreen
import com.vocapop.android.ui.onboarding.LanguageSelectionScreen
import com.vocapop.android.ui.onboarding.StarterDeckScreen
import com.vocapop.android.ui.onboarding.WelcomeScreen
import androidx.compose.material3.Text

import com.vocapop.android.ui.home.HomeScreen
import com.vocapop.android.ui.learning.FlashcardScreen
import com.vocapop.android.ui.learning.SessionCompletionScreen
import com.vocapop.android.ui.stats.ProgressScreen
import com.vocapop.android.ui.decks.DeckManagementScreen
import com.vocapop.android.ui.settings.SettingsScreen
import com.vocapop.android.data.SessionManager

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onStartLearning = {
                    navController.navigate(Screen.LanguageSelection.route)
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        
        composable(Screen.LanguageSelection.route) {
            LanguageSelectionScreen(
                onLanguageSelected = { languageId ->
                    navController.navigate(Screen.DailyGoal.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.DailyGoal.route) {
            DailyGoalScreen(
                onGoalSelected = { goal ->
                    navController.navigate(Screen.StarterDeck.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.StarterDeck.route) {
            StarterDeckScreen(
                onContinue = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onStartSession = {
                    navController.navigate(Screen.Session.route)
                },
                onDeckClick = { deckId ->
                    navController.navigate(Screen.Decks.route)
                },
                onNavClick = { route ->
                   navController.navigate(route) {
                       popUpTo(Screen.Home.route) { saveState = true }
                       launchSingleTop = true
                       restoreState = true
                   }
                }
            )
        }
        
        composable(Screen.Session.route) {
            FlashcardScreen(
                onSessionComplete = {
                    navController.navigate(Screen.SessionComplete.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.SessionComplete.route) {
            SessionCompletionScreen(
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Progress.route) {
            ProgressScreen()
        }
        
        composable(Screen.Decks.route) {
            DeckManagementScreen(onDeckClick = {}, onAddDeck = {})
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onLogout = {
                    SessionManager.logout()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            com.vocapop.android.ui.auth.LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
