package com.vocapop.android.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object LanguageSelection : Screen("language_selection")
    object DailyGoal : Screen("daily_goal")
    object StarterDeck : Screen("starter_deck")
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register")
    object Session : Screen("session")
    object SessionComplete : Screen("session_complete")
    object Progress : Screen("progress")
    object Decks : Screen("decks")
    object Settings : Screen("settings")
}
