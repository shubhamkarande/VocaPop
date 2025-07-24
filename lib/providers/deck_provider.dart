import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/deck.dart';
import '../models/flashcard.dart';
import '../services/storage_service.dart';

final deckProvider = StateNotifierProvider<DeckNotifier, List<Deck>>((ref) {
  return DeckNotifier();
});

final flashcardProvider =
    StateNotifierProvider.family<FlashcardNotifier, List<Flashcard>, String>((
      ref,
      deckId,
    ) {
      return FlashcardNotifier(deckId);
    });

class DeckNotifier extends StateNotifier<List<Deck>> {
  DeckNotifier() : super([]) {
    loadDecks();
  }

  void loadDecks() {
    state = StorageService.getAllDecks();
  }

  Future<void> addDeck(Deck deck) async {
    await StorageService.saveDeck(deck);
    state = [...state, deck];
  }

  Future<void> updateDeck(Deck deck) async {
    await StorageService.saveDeck(deck);
    state = [
      for (final d in state)
        if (d.id == deck.id) deck else d,
    ];
  }

  Future<void> deleteDeck(String id) async {
    await StorageService.deleteDeck(id);
    state = state.where((deck) => deck.id != id).toList();
  }

  void updateDeckStats(String deckId) {
    final flashcards = StorageService.getFlashcardsByDeck(deckId);
    final deck = state.firstWhere((d) => d.id == deckId);

    deck.totalCards = flashcards.length;
    deck.learnedCards = flashcards.where((card) => card.repetitions > 0).length;

    updateDeck(deck);
  }
}

class FlashcardNotifier extends StateNotifier<List<Flashcard>> {
  final String deckId;

  FlashcardNotifier(this.deckId) : super([]) {
    loadFlashcards();
  }

  void loadFlashcards() {
    state = StorageService.getFlashcardsByDeck(deckId);
  }

  Future<void> addFlashcard(Flashcard flashcard) async {
    await StorageService.saveFlashcard(flashcard);
    state = [...state, flashcard];
  }

  Future<void> updateFlashcard(Flashcard flashcard) async {
    await StorageService.saveFlashcard(flashcard);
    state = [
      for (final card in state)
        if (card.id == flashcard.id) flashcard else card,
    ];
  }

  Future<void> deleteFlashcard(String id) async {
    await StorageService.deleteFlashcard(id);
    state = state.where((card) => card.id != id).toList();
  }

  List<Flashcard> getDueCards() {
    return state.where((card) => card.isDue).toList();
  }
}
