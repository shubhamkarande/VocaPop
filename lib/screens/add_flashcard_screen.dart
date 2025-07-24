import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/deck.dart';
import '../models/flashcard.dart';
import '../providers/deck_provider.dart';
import '../services/tts_service.dart';

class AddFlashcardScreen extends ConsumerStatefulWidget {
  final Deck deck;

  const AddFlashcardScreen({super.key, required this.deck});

  @override
  ConsumerState<AddFlashcardScreen> createState() => _AddFlashcardScreenState();
}

class _AddFlashcardScreenState extends ConsumerState<AddFlashcardScreen> {
  final _formKey = GlobalKey<FormState>();
  final _wordController = TextEditingController();
  final _translationController = TextEditingController();
  final _exampleController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('New Flashcard'),
        backgroundColor: Color(
          int.parse(widget.deck.color.replaceFirst('#', '0xFF')),
        ),
        foregroundColor: Colors.white,
        actions: [
          TextButton(
            onPressed: _saveFlashcard,
            child: const Text('Save', style: TextStyle(color: Colors.white)),
          ),
        ],
      ),
      body: Form(
        key: _formKey,
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            children: [
              TextFormField(
                controller: _wordController,
                decoration: InputDecoration(
                  labelText: 'Word (${widget.deck.language})',
                  border: const OutlineInputBorder(),
                  suffixIcon: IconButton(
                    icon: const Icon(Icons.volume_up),
                    onPressed: () {
                      if (_wordController.text.isNotEmpty) {
                        TTSService.speak(
                          _wordController.text,
                          language: TTSService.getLanguageCode(
                            widget.deck.language,
                          ),
                        );
                      }
                    },
                  ),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter a word';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),

              TextFormField(
                controller: _translationController,
                decoration: const InputDecoration(
                  labelText: 'Translation',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter a translation';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),

              TextFormField(
                controller: _exampleController,
                decoration: const InputDecoration(
                  labelText: 'Example Sentence (optional)',
                  border: OutlineInputBorder(),
                ),
                maxLines: 3,
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _saveFlashcard() {
    if (_formKey.currentState!.validate()) {
      final flashcard = Flashcard(
        word: _wordController.text,
        translation: _translationController.text,
        exampleSentence: _exampleController.text.isEmpty
            ? null
            : _exampleController.text,
        deckId: widget.deck.id,
      );

      ref
          .read(flashcardProvider(widget.deck.id).notifier)
          .addFlashcard(flashcard);
      ref.read(deckProvider.notifier).updateDeckStats(widget.deck.id);
      Navigator.pop(context);
    }
  }

  @override
  void dispose() {
    _wordController.dispose();
    _translationController.dispose();
    _exampleController.dispose();
    super.dispose();
  }
}
