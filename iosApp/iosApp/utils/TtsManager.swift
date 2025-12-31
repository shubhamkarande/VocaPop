import AVFoundation

class TtsManager: NSObject {
    let synthesizer = AVSpeechSynthesizer()
    
    func speak(_ text: String, language: String = "fr-FR") {
        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: language)
        utterance.rate = 0.5
        
        synthesizer.speak(utterance)
    }
    
    func stop() {
        synthesizer.stopSpeaking(at: .immediate)
    }
}
