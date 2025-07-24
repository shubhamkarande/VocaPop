import 'package:hive/hive.dart';
import 'package:uuid/uuid.dart';

part 'deck.g.dart';

@HiveType(typeId: 1)
class Deck extends HiveObject {
  @HiveField(0)
  String id;

  @HiveField(1)
  String name;

  @HiveField(2)
  String? description;

  @HiveField(3)
  String language;

  @HiveField(4)
  DateTime createdAt;

  @HiveField(5)
  int totalCards;

  @HiveField(6)
  int learnedCards;

  @HiveField(7)
  String color;

  Deck({
    String? id,
    required this.name,
    this.description,
    required this.language,
    DateTime? createdAt,
    this.totalCards = 0,
    this.learnedCards = 0,
    this.color = '#6366F1',
  }) : id = id ?? const Uuid().v4(),
       createdAt = createdAt ?? DateTime.now();

  double get progress => totalCards > 0 ? learnedCards / totalCards : 0.0;
}
