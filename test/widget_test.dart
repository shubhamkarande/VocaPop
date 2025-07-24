import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:vocapop/main.dart';
import 'package:vocapop/services/storage_service.dart';

void main() {
  testWidgets('VocaPop app smoke test', (WidgetTester tester) async {
    // Initialize storage for testing
    await StorageService.init();

    // Build our app and trigger a frame.
    await tester.pumpWidget(const ProviderScope(child: VocaPopApp()));

    // Verify that the app starts with the home screen
    expect(find.text('VocaPop'), findsOneWidget);
  });
}
