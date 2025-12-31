import SwiftUI

struct NavigationRoutes {
    enum Screen: Hashable {
        case welcome
        case languageSelection
        case dailyGoal
        case starterDeck
        case home
        case session
        case sessionComplete
        case progress
        case decks
        case settings
    }
}

struct ContentView: View {
    @State private var navigationPath = NavigationPath()
    
    var body: some View {
        NavigationStack(path: $navigationPath) {
            WelcomeScreen(
                onStartLearning: { navigationPath.append(NavigationRoutes.Screen.languageSelection) },
                onLoginClick: { /* Navigate to login */ }
            )
            .navigationDestination(for: NavigationRoutes.Screen.self) { screen in
                switch screen {
                case .welcome:
                    WelcomeScreen(
                        onStartLearning: { navigationPath.append(NavigationRoutes.Screen.languageSelection) },
                        onLoginClick: {}
                    )
                case .languageSelection:
                    LanguageSelectionScreen(
                        onLanguageSelected: { _ in navigationPath.append(NavigationRoutes.Screen.dailyGoal) },
                        onBack: { navigationPath.removeLast() }
                    )
                case .dailyGoal:
                    DailyGoalScreen(
                        onGoalSelected: { _ in navigationPath.append(NavigationRoutes.Screen.starterDeck) },
                        onBack: { navigationPath.removeLast() }
                    )
                case .starterDeck:
                    StarterDeckScreen(
                        onContinue: {
                             // Clear stack then create new flow? 
                             // SwiftUI NavigationStack path binding reset is easiest.
                             navigationPath = NavigationPath([NavigationRoutes.Screen.home])
                        }
                    )
                case .home:
                    HomeScreen(
                        onStartSession: { navigationPath.append(NavigationRoutes.Screen.session) },
                        onNavClick: { route in navigationPath.append(route) }
                    )
                case .session:
                    FlashcardScreen(
                        onSessionComplete: { navigationPath.append(NavigationRoutes.Screen.sessionComplete) },
                        onBack: { navigationPath.removeLast() }
                    )
                case .sessionComplete:
                    SessionCompletionScreen(
                        onHomeClick: { navigationPath = NavigationPath([NavigationRoutes.Screen.home]) }
                    )
                case .progress:
                    ProgressScreen()
                case .decks:
                    DeckManagementScreen(onDeckClick: { _ in }, onAddDeck: {})
                case .settings:
                    SettingsScreen()
                }
            }
        }
    }
}
