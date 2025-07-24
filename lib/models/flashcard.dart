import 'package:hive/hive.dart';
import 'package:uuid/uuid.dart';

part 'flashcard.g.dart';

@HiveType(typeId: 0)
class Flashcard extends HiveObject {
  @HiveField(0)
  String id;

  @HiveField(1)
  String word;

  @HiveField(2)
  String translation;

  @HiveField(3)
  String? exampleSentence;

  @HiveField(4)
  String? imageUrl;

  @HiveField(5)
  DateTime createdAt;

  @HiveField(6)
  DateTime nextReview;

  @HiveField(7)
  int interval;

  @HiveField(8)
  double easeFactor;

  @HiveField(9)
  int repetitions;

  @HiveField(10)
  String deckId;

  Flashcard({
    String? id,
    required this.word,
    required this.translation,
    this.exampleSentence,
    this.imageUrl,
    required this.deckId,
    DateTime? createdAt,
    DateTime? nextReview,
    this.interval = 1,
    this.easeFactor = 2.5,
    this.repetitions = 0,
  }) : id = id ?? const Uuid().v4(),
       createdAt = createdAt ?? DateTime.now(),
       nextReview = nextReview ?? DateTime.now();

  bool get isDue => DateTime.now().isAfter(nextReview);

  void updateSRS(int quality) {
    if (quality >= 3) {
      if (repetitions == 0) {
        interval = 1;
      } else if (repetitions == 1) {
        interval = 6;
      } else {
        interval = (interval * easeFactor).round();
      }
      repetitions++;
    } else {
      repetitions = 0;
      interval = 1;
    }

    easeFactor =
        easeFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
    if (easeFactor < 1.3) easeFactor = 1.3;

    nextReview = DateTime.now().add(Duration(days: interval));
    save();
  }
}
