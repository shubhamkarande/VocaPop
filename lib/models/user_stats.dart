import 'package:hive/hive.dart';

part 'user_stats.g.dart';

@HiveType(typeId: 2)
class UserStats extends HiveObject {
  @HiveField(0)
  int totalXP;

  @HiveField(1)
  int currentStreak;

  @HiveField(2)
  int longestStreak;

  @HiveField(3)
  DateTime? lastStudyDate;

  @HiveField(4)
  Map<String, int> dailyXP;

  @HiveField(5)
  int cardsReviewed;

  @HiveField(6)
  int correctAnswers;

  UserStats({
    this.totalXP = 0,
    this.currentStreak = 0,
    this.longestStreak = 0,
    this.lastStudyDate,
    Map<String, int>? dailyXP,
    this.cardsReviewed = 0,
    this.correctAnswers = 0,
  }) : dailyXP = dailyXP ?? {};

  double get accuracy =>
      cardsReviewed > 0 ? correctAnswers / cardsReviewed : 0.0;

  void addXP(int xp) {
    totalXP += xp;
    final today = DateTime.now();
    final todayKey = '${today.year}-${today.month}-${today.day}';
    dailyXP[todayKey] = (dailyXP[todayKey] ?? 0) + xp;

    updateStreak();
    save();
  }

  void updateStreak() {
    final today = DateTime.now();
    final yesterday = today.subtract(const Duration(days: 1));

    if (lastStudyDate == null) {
      currentStreak = 1;
    } else if (isSameDay(lastStudyDate!, today)) {
      // Already studied today, don't change streak
      return;
    } else if (isSameDay(lastStudyDate!, yesterday)) {
      currentStreak++;
    } else {
      currentStreak = 1;
    }

    if (currentStreak > longestStreak) {
      longestStreak = currentStreak;
    }

    lastStudyDate = today;
  }

  bool isSameDay(DateTime date1, DateTime date2) {
    return date1.year == date2.year &&
        date1.month == date2.month &&
        date1.day == date2.day;
  }
}
