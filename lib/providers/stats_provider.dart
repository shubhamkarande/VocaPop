import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/user_stats.dart';
import '../services/storage_service.dart';

final statsProvider = StateNotifierProvider<StatsNotifier, UserStats>((ref) {
  return StatsNotifier();
});

class StatsNotifier extends StateNotifier<UserStats> {
  StatsNotifier() : super(UserStats()) {
    loadStats();
  }

  void loadStats() {
    state = StorageService.getUserStats();
  }

  Future<void> addXP(int xp) async {
    state.addXP(xp);
    await StorageService.saveUserStats(state);
    // Trigger rebuild
    state = UserStats(
      totalXP: state.totalXP,
      currentStreak: state.currentStreak,
      longestStreak: state.longestStreak,
      lastStudyDate: state.lastStudyDate,
      dailyXP: Map.from(state.dailyXP),
      cardsReviewed: state.cardsReviewed,
      correctAnswers: state.correctAnswers,
    );
  }

  Future<void> recordAnswer(bool correct) async {
    state.cardsReviewed++;
    if (correct) {
      state.correctAnswers++;
      await addXP(10); // 10 XP for correct answer
    } else {
      await addXP(2); // 2 XP for attempt
    }
    await StorageService.saveUserStats(state);
  }

  List<MapEntry<String, int>> getWeeklyXP() {
    final now = DateTime.now();
    final weeklyXP = <String, int>{};

    for (int i = 6; i >= 0; i--) {
      final date = now.subtract(Duration(days: i));
      final key = '${date.year}-${date.month}-${date.day}';
      final dayName = [
        'Mon',
        'Tue',
        'Wed',
        'Thu',
        'Fri',
        'Sat',
        'Sun',
      ][date.weekday - 1];
      weeklyXP[dayName] = state.dailyXP[key] ?? 0;
    }

    return weeklyXP.entries.toList();
  }
}
