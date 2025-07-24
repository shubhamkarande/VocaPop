import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/deck.dart';
import '../models/flashcard.dart';
import '../providers/deck_provider.dart';
import '../services/tts_service.dart';
import 'add_flashcard_screen.dart';
import 'quiz_screen.dart';

class DeckScreen extends ConsumerWidget {
  final Deck deck;

  const DeckScreen({super.key, required this.deck});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final flashcards = ref.watch(flashcardProvider(deck.id));
    final dueCards = flashcards.where((card) => card.isDue).toList();

    return Scaffold(
      appBar: AppBar(
        title: Text(deck.name),
        backgroundColor: Color(int.parse(deck.color.replaceFirst('#', '0xFF'))),
        foregroundColor: Colors.white,
      ),
      body: Column(
        children: [
          // Deck Info Header
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(16),
            color: Color(
              int.parse(deck.color.replaceFirst('#', '0xFF')),
            ).withValues(alpha: 0.1),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  deck.language,
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                const SizedBox(height: 8),
                Text('${flashcards.length} cards • ${dueCards.length} due'),
                const SizedBox(height: 8),
                LinearProgressIndicator(
                  value: deck.progress,
                  backgroundColor: Colors.grey[300],
                  valueColor: AlwaysStoppedAnimation(
                    Color(int.parse(deck.color.replaceFirst('#', '0xFF'))),
                  ),
                ),
              ],
            ),
          ),

          // Action Buttons
          Padding(
            padding: const EdgeInsets.all(16),
            child: Row(
              children: [
                Expanded(
                  child: ElevatedButton.icon(
                    onPressed: dueCards.isNotEmpty
                        ? () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => QuizScreen(
                                  deck: deck,
                                  flashcards: dueCards,
                                ),
                              ),
                            );
                          }
                        : null,
                    icon: const Icon(Icons.quiz),
                    label: Text('Study (${dueCards.length})'),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Color(
                        int.parse(deck.color.replaceFirst('#', '0xFF')),
                      ),
                      foregroundColor: Colors.white,
                    ),
                  ),
                ),
                const SizedBox(width: 12),
                ElevatedButton.icon(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => AddFlashcardScreen(deck: deck),
                      ),
                    );
                  },
                  icon: const Icon(Icons.add),
                  label: const Text('Add Card'),
                ),
              ],
            ),
          ),

          // Flashcards List
          Expanded(
            child: flashcards.isEmpty
                ? const Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.style, size: 64, color: Colors.grey),
                        SizedBox(height: 16),
                        Text(
                          'No cards yet',
                          style: TextStyle(fontSize: 18, color: Colors.grey),
                        ),
                        SizedBox(height: 8),
                        Text(
                          'Add your first flashcard',
                          style: TextStyle(color: Colors.grey),
                        ),
                      ],
                    ),
                  )
                : ListView.builder(
                    itemCount: flashcards.length,
                    itemBuilder: (context, index) {
                      final card = flashcards[index];
                      return _FlashcardTile(card: card, deck: deck);
                    },
                  ),
          ),
        ],
      ),
    );
  }
}

class _FlashcardTile extends ConsumerWidget {
  final Flashcard card;
  final Deck deck;

  const _FlashcardTile({required this.card, required this.deck});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
      child: ListTile(
        title: Text(card.word),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(card.translation),
            if (card.exampleSentence != null) ...[
              const SizedBox(height: 4),
              Text(
                card.exampleSentence!,
                style: TextStyle(
                  color: Colors.grey[600],
                  fontStyle: FontStyle.italic,
                ),
              ),
            ],
          ],
        ),
        leading: card.isDue
            ? const Icon(Icons.schedule, color: Colors.orange)
            : const Icon(Icons.check_circle, color: Colors.green),
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            IconButton(
              icon: const Icon(Icons.volume_up),
              onPressed: () {
                TTSService.speak(
                  card.word,
                  language: TTSService.getLanguageCode(deck.language),
                );
              },
            ),
            PopupMenuButton(
              itemBuilder: (context) => [
                const PopupMenuItem(
                  value: 'delete',
                  child: Row(
                    children: [
                      Icon(Icons.delete, color: Colors.red),
                      SizedBox(width: 8),
                      Text('Delete'),
                    ],
                  ),
                ),
              ],
              onSelected: (value) {
                if (value == 'delete') {
                  _deleteCard(context, ref);
                }
              },
            ),
          ],
        ),
      ),
    );
  }

  void _deleteCard(BuildContext context, WidgetRef ref) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Delete Card'),
        content: Text('Are you sure you want to delete "${card.word}"?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          TextButton(
            onPressed: () {
              ref
                  .read(flashcardProvider(deck.id).notifier)
                  .deleteFlashcard(card.id);
              ref.read(deckProvider.notifier).updateDeckStats(deck.id);
              Navigator.pop(context);
            },
            child: const Text('Delete', style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }
}
