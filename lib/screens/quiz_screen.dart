import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/deck.dart';
import '../models/flashcard.dart';
import '../providers/stats_provider.dart';
import '../services/tts_service.dart';

class QuizScreen extends ConsumerStatefulWidget {
  final Deck deck;
  final List<Flashcard> flashcards;

  const QuizScreen({super.key, required this.deck, required this.flashcards});

  @override
  ConsumerState<QuizScreen> createState() => _QuizScreenState();
}

class _QuizScreenState extends ConsumerState<QuizScreen> {
  int currentIndex = 0;
  bool showAnswer = false;
  int correctAnswers = 0;

  @override
  Widget build(BuildContext context) {
    if (currentIndex >= widget.flashcards.length) {
      return _buildResultScreen();
    }

    final currentCard = widget.flashcards[currentIndex];

    return Scaffold(
      appBar: AppBar(
        title: Text('${currentIndex + 1}/${widget.flashcards.length}'),
        backgroundColor: Color(
          int.parse(widget.deck.color.replaceFirst('#', '0xFF')),
        ),
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            // Progress indicator
            LinearProgressIndicator(
              value: (currentIndex + 1) / widget.flashcards.length,
              backgroundColor: Colors.grey[300],
              valueColor: AlwaysStoppedAnimation(
                Color(int.parse(widget.deck.color.replaceFirst('#', '0xFF'))),
              ),
            ),
            const SizedBox(height: 32),

            // Flashcard
            Expanded(
              child: Center(
                child: GestureDetector(
                  onTap: () {
                    setState(() {
                      showAnswer = !showAnswer;
                    });
                  },
                  child: Card(
                    elevation: 8,
                    child: Container(
                      width: double.infinity,
                      height: 300,
                      padding: const EdgeInsets.all(24),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          if (!showAnswer) ...[
                            Text(
                              currentCard.word,
                              style: const TextStyle(
                                fontSize: 32,
                                fontWeight: FontWeight.bold,
                              ),
                              textAlign: TextAlign.center,
                            ),
                            const SizedBox(height: 16),
                            IconButton(
                              onPressed: () {
                                TTSService.speak(
                                  currentCard.word,
                                  language: TTSService.getLanguageCode(
                                    widget.deck.language,
                                  ),
                                );
                              },
                              icon: const Icon(Icons.volume_up, size: 32),
                            ),
                            const SizedBox(height: 16),
                            const Text(
                              'Tap to reveal answer',
                              style: TextStyle(color: Colors.grey),
                            ),
                          ] else ...[
                            Text(
                              currentCard.translation,
                              style: const TextStyle(
                                fontSize: 28,
                                fontWeight: FontWeight.bold,
                                color: Colors.green,
                              ),
                              textAlign: TextAlign.center,
                            ),
                            if (currentCard.exampleSentence != null) ...[
                              const SizedBox(height: 16),
                              Text(
                                currentCard.exampleSentence!,
                                style: const TextStyle(
                                  fontSize: 16,
                                  fontStyle: FontStyle.italic,
                                ),
                                textAlign: TextAlign.center,
                              ),
                            ],
                          ],
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ),

            // Answer buttons
            if (showAnswer) ...[
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () => _answerCard(1), // Again
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.red,
                        foregroundColor: Colors.white,
                      ),
                      child: const Text('Again'),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () => _answerCard(2), // Hard
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.orange,
                        foregroundColor: Colors.white,
                      ),
                      child: const Text('Hard'),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () => _answerCard(3), // Good
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.blue,
                        foregroundColor: Colors.white,
                      ),
                      child: const Text('Good'),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () => _answerCard(4), // Easy
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.green,
                        foregroundColor: Colors.white,
                      ),
                      child: const Text('Easy'),
                    ),
                  ),
                ],
              ),
            ],
          ],
        ),
      ),
    );
  }

  void _answerCard(int quality) {
    final currentCard = widget.flashcards[currentIndex];
    currentCard.updateSRS(quality);

    final isCorrect = quality >= 3;
    if (isCorrect) correctAnswers++;

    ref.read(statsProvider.notifier).recordAnswer(isCorrect);

    setState(() {
      currentIndex++;
      showAnswer = false;
    });
  }

  Widget _buildResultScreen() {
    final accuracy = (correctAnswers / widget.flashcards.length * 100).round();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Quiz Complete'),
        backgroundColor: Color(
          int.parse(widget.deck.color.replaceFirst('#', '0xFF')),
        ),
        foregroundColor: Colors.white,
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(32),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                accuracy >= 80 ? Icons.celebration : Icons.thumb_up,
                size: 80,
                color: accuracy >= 80 ? Colors.amber : Colors.blue,
              ),
              const SizedBox(height: 24),
              Text(
                accuracy >= 80 ? 'Excellent!' : 'Good Job!',
                style: const TextStyle(
                  fontSize: 32,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16),
              Text(
                '$correctAnswers/${widget.flashcards.length} correct',
                style: const TextStyle(fontSize: 24),
              ),
              Text(
                '$accuracy% accuracy',
                style: const TextStyle(fontSize: 18, color: Colors.grey),
              ),
              const SizedBox(height: 32),
              ElevatedButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: Color(
                    int.parse(widget.deck.color.replaceFirst('#', '0xFF')),
                  ),
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(
                    horizontal: 32,
                    vertical: 16,
                  ),
                ),
                child: const Text('Back to Deck'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
