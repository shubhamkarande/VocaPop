# VocaPop - Pop a Card. Learn a Word. Get Fluent.

A gamified, cross-platform flashcard app that helps users build vocabulary using Spaced Repetition System (SRS), generate quizzes, and utilize Text-to-Speech (TTS) for pronunciation practice.

## Features

### Core Features
- **Deck & Flashcard Management**: Create custom decks and add flashcards with words, translations, and example sentences
- **Spaced Repetition System (SRS)**: Smart algorithm schedules reviews based on memory strength using SM-2 algorithm
- **Daily Quizzes**: Interactive review sessions with difficulty rating (Again, Hard, Good, Easy)
- **Text-to-Speech (TTS)**: Hear word pronunciations in multiple languages
- **Progress Tracking**: Track XP, streaks, learned cards, and accuracy statistics
- **Gamification**: XP system, streak tracking, and achievement badges

### Tech Stack
- **Frontend**: Flutter (cross-platform)
- **State Management**: Riverpod
- **Local Storage**: Hive (NoSQL database)
- **TTS**: Flutter TTS plugin
- **Charts**: FL Chart for statistics visualization

## Getting Started

### Prerequisites
- Flutter SDK (3.8.1 or higher)
- Dart SDK
- Android Studio / VS Code with Flutter extensions

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd vocapop
```

2. Install dependencies:
```bash
flutter pub get
```

3. Generate Hive adapters:
```bash
flutter packages pub run build_runner build
```

4. Run the app:
```bash
flutter run
```

## Project Structure

```
lib/
├── main.dart                 # App entry point
├── models/                   # Data models
│   ├── deck.dart            # Deck model with Hive annotations
│   ├── flashcard.dart       # Flashcard model with SRS logic
│   └── user_stats.dart      # User statistics model
├── providers/               # Riverpod state management
│   ├── deck_provider.dart   # Deck and flashcard state
│   └── stats_provider.dart  # Statistics state
├── screens/                 # UI screens
│   ├── home_screen.dart     # Main dashboard
│   ├── deck_screen.dart     # Deck details and flashcard list
│   ├── add_deck_screen.dart # Create new deck
│   ├── add_flashcard_screen.dart # Create new flashcard
│   ├── quiz_screen.dart     # Review/quiz interface
│   └── stats_screen.dart    # Statistics and achievements
└── services/               # Business logic services
    ├── storage_service.dart # Hive database operations
    └── tts_service.dart    # Text-to-speech functionality
```

## Usage

1. **Create a Deck**: Tap the + button on the home screen to create your first deck
2. **Add Flashcards**: Open a deck and add flashcards with words and translations
3. **Study**: Use the Study button to review due cards with the SRS system
4. **Track Progress**: View your statistics, XP, streaks, and achievements
5. **Listen**: Use the speaker icon to hear pronunciations

## SRS Algorithm

The app uses a modified SM-2 (SuperMemo 2) algorithm:
- **Quality ratings**: Again (1), Hard (2), Good (3), Easy (4)
- **Intervals**: Start at 1 day, then 6 days, then calculated based on ease factor
- **Ease factor**: Adjusts based on performance (1.3 minimum, 2.5 default)

## Future Enhancements

- Laravel backend API integration
- User authentication and cloud sync
- Image support for flashcards
- Multiple quiz types (multiple choice, typing)
- Social features and deck sharing
- Advanced statistics and analytics

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
