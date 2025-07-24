import 'package:flutter_tts/flutter_tts.dart';

class TTSService {
  static final FlutterTts _flutterTts = FlutterTts();
  static bool _isInitialized = false;

  static Future<void> init() async {
    if (_isInitialized) return;

    await _flutterTts.setVolume(1.0);
    await _flutterTts.setSpeechRate(0.5);
    await _flutterTts.setPitch(1.0);

    _isInitialized = true;
  }

  static Future<void> speak(String text, {String? language}) async {
    await init();

    if (language != null) {
      await _flutterTts.setLanguage(language);
    }

    await _flutterTts.speak(text);
  }

  static Future<void> stop() async {
    await _flutterTts.stop();
  }

  static Future<List<dynamic>> getLanguages() async {
    await init();
    return await _flutterTts.getLanguages;
  }

  static String getLanguageCode(String language) {
    final languageCodes = {
      'English': 'en-US',
      'Spanish': 'es-ES',
      'French': 'fr-FR',
      'German': 'de-DE',
      'Italian': 'it-IT',
      'Portuguese': 'pt-BR',
      'Japanese': 'ja-JP',
      'Korean': 'ko-KR',
      'Chinese': 'zh-CN',
    };

    return languageCodes[language] ?? 'en-US';
  }
}
