import 'package:hive_flutter/hive_flutter.dart';
import '../models/deck.dart';
import '../models/flashcard.dart';
import '../models/user_stats.dart';

class StorageService {
  static const String _decksBox = 'decks';
  static const String _flashcardsBox = 'flashcards';
  static const String _statsBox = 'stats';

  static Future<void> init() async {
    await Hive.initFlutter();

    // Register adapters
    Hive.registerAdapter(DeckAdapter());
    Hive.registerAdapter(FlashcardAdapter());
    Hive.registerAdapter(UserStatsAdapter());

    // Open boxes
    await Hive.openBox<Deck>(_decksBox);
    await Hive.openBox<Flashcard>(_flashcardsBox);
    await Hive.openBox<UserStats>(_statsBox);
  }

  static Box<Deck> get decksBox => Hive.box<Deck>(_decksBox);
  static Box<Flashcard> get flashcardsBox =>
      Hive.box<Flashcard>(_flashcardsBox);
  static Box<UserStats> get statsBox => Hive.box<UserStats>(_statsBox);

  // Deck operations
  static Future<void> saveDeck(Deck deck) async {
    await decksBox.put(deck.id, deck);
  }

  static List<Deck> getAllDecks() {
    return decksBox.values.toList();
  }

  static Deck? getDeck(String id) {
    return decksBox.get(id);
  }

  static Future<void> deleteDeck(String id) async {
    await decksBox.delete(id);
    // Also delete all flashcards in this deck
    final flashcards = getFlashcardsByDeck(id);
    for (final card in flashcards) {
      await flashcardsBox.delete(card.id);
    }
  }

  // Flashcard operations
  static Future<void> saveFlashcard(Flashcard flashcard) async {
    await flashcardsBox.put(flashcard.id, flashcard);
  }

  static List<Flashcard> getFlashcardsByDeck(String deckId) {
    return flashcardsBox.values.where((card) => card.deckId == deckId).toList();
  }

  static List<Flashcard> getDueFlashcards(String deckId) {
    return getFlashcardsByDeck(deckId).where((card) => card.isDue).toList();
  }

  static Future<void> deleteFlashcard(String id) async {
    await flashcardsBox.delete(id);
  }

  // Stats operations
  static UserStats getUserStats() {
    return statsBox.get('user_stats') ?? UserStats();
  }

  static Future<void> saveUserStats(UserStats stats) async {
    await statsBox.put('user_stats', stats);
  }
}
