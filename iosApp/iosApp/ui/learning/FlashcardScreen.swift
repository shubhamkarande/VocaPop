import SwiftUI
import shared

struct FlashcardScreen: View {
    var onSessionComplete: () -> Void
    var onBack: () -> Void
    
    @StateObject private var viewModel = iOSSessionViewModel()
    @State private var isFlipped = false
    @State private var rotation: Double = 0
    
    var body: some View {
        ZStack {
            Theme.backgroundDark.ignoresSafeArea()
            
            if viewModel.state.isLoading {
                ProgressView()
            } else if let card = viewModel.currentCard {
                VStack {
                    // Header
                    HStack {
                        Button(action: onBack) {
                            Image(systemName: "xmark")
                                .foregroundColor(.white)
                        }
                        
                        Spacer()
                        
                        ProgressView(value: Double(viewModel.state.currentCardIndex), total: Double(viewModel.state.cards.count))
                            .tint(Theme.primary)
                        
                        Spacer()
                        
                        Button(action: {}) {
                            Image(systemName: "ellipsis")
                                .foregroundColor(.white)
                        }
                    }
                    .padding()
                    
                    // Card
                    ZStack {
                        if rotation < 90 {
                            FrontCardView(card: card)
                        } else {
                            BackCardView(card: card)
                                .rotation3DEffect(.degrees(180), axis: (x: 0, y: 1, z: 0))
                        }
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Theme.softOrange)
                    .cornerRadius(32)
                    .padding()
                    .rotation3DEffect(.degrees(rotation), axis: (x: 0, y: 1, z: 0))
                    .onTapGesture {
                        withAnimation(.easeInOut(duration: 0.5)) {
                            if !isFlipped {
                                rotation += 180
                                isFlipped = true
                            }
                        }
                    }
                    
                    // Controls
                    VStack {
                        if !isFlipped {
                            Button(action: {
                                withAnimation(.easeInOut(duration: 0.5)) {
                                    rotation += 180
                                    isFlipped = true
                                }
                            }) {
                                Text("Show Answer")
                                    .fontWeight(.bold)
                                    .foregroundColor(.white)
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 56)
                                    .background(Theme.primary)
                                    .cornerRadius(16)
                            }
                        } else {
                            HStack(spacing: 8) {
                                GradingButton(label: "Again", sub: "1m", color: Theme.accentRed) {
                                    viewModel.submitGrade(grade: .again)
                                    nextCard()
                                }
                                GradingButton(label: "Hard", sub: "10m", color: Theme.accentOrange) {
                                    viewModel.submitGrade(grade: .hard)
                                    nextCard()
                                }
                                GradingButton(label: "Good", sub: "1d", color: Theme.secondary) {
                                    viewModel.submitGrade(grade: .good)
                                    nextCard()
                                }
                                GradingButton(label: "Easy", sub: "4d", color: Theme.accentTeal) {
                                    viewModel.submitGrade(grade: .easy)
                                    nextCard()
                                }
                            }
                            .frame(height: 56)
                        }
                    }
                    .padding()
                }
            }
        }
        .onAppear {
            viewModel.loadSession()
        }
        .onChange(of: viewModel.state.isSessionComplete) { complete in
            if complete {
                onSessionComplete()
            }
        }
        .navigationBarHidden(true)
    }
    
    func nextCard() {
        rotation = 0
        isFlipped = false
        // Animation reset? 
        // In SwiftUI immediate change of rotation to 0 is fine if we change content
    }
}

struct FrontCardView: View {
    let card: CardWithProgress
    
    var body: some View {
        VStack {
            HStack {
                Text(card.card.wordType ?? "Word")
                    .font(.caption)
                    .italic()
                    .padding(8)
                    .background(Color.black.opacity(0.05))
                    .cornerRadius(8)
                Spacer()
                Image(systemName: "speaker.wave.2.circle.fill")
                    .font(.title)
                    .foregroundColor(Color.black.opacity(0.5))
            }
            .padding(24)
            
            Spacer()
            
            Text(card.card.front)
                .font(.system(size: 38, weight: .bold))
                .foregroundColor(Color(hex: "101022"))
            
            if let example = card.card.exampleSentence {
                 Text(example)
                    .italic()
                    .foregroundColor(Color(hex: "5A483A"))
                    .padding(.top, 16)
            }
            
            Spacer()
            
            Image(systemName: "hand.tap")
                .foregroundColor(Color.black.opacity(0.3))
                .padding(.bottom)
        }
    }
}

struct BackCardView: View {
    let card: CardWithProgress
    
    var body: some View {
        VStack {
            Text(card.card.front)
                .foregroundColor(Color.black.opacity(0.5))
                .padding(.top, 32)
                
            Text(card.card.back)
                .font(.system(size: 38, weight: .bold))
                .foregroundColor(Theme.primary)
                .padding(.top, 8)
                
            Divider().padding(32)
            
            if let example = card.card.exampleSentence {
                 Text(example)
                    .foregroundColor(Color(hex: "5A483A"))
            }

            Spacer()
        }
    }
}

struct GradingButton: View {
    let label: String
    let sub: String
    let color: Color
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack {
                Text(label).font(.caption).bold()
                Text(sub).font(.caption2)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(color.opacity(0.1))
            .foregroundColor(color)
            .cornerRadius(12)
        }
    }
}

// Simple wrapper for ViewModel since we don't have Koin injection in View structs easily yet
class iOSSessionViewModel: ObservableObject {
    @Published var state = SessionState(isLoading: true, cards: [], currentCardIndex: 0, isCardFlipped: false, isSessionComplete: false, sessionStats: SessionStats(cardsReviewed: 0, correctCount: 0, newLearned: 0))
    
    // In real app, inject via Koin helper
    // For now, mock
    
    var currentCard: CardWithProgress? {
        if state.cards.indices.contains(state.currentCardIndex) {
            return state.cards[state.currentCardIndex]
        }
        return nil
    }
    
    func loadSession() {
        // Mock
        let mockCards = [
            CardWithProgress(
                card: Card(id: "1", deckId: "1", front: "Le Chat", back: "The Cat", exampleSentence: "Le chat dort.", wordType: "Noun", imageUrl: nil, audioUrl: nil),
                progress: nil
            ),
             CardWithProgress(
                card: Card(id: "2", deckId: "1", front: "Chien", back: "Dog", exampleSentence: "J'ai un chien.", wordType: "Noun", imageUrl: nil, audioUrl: nil),
                progress: nil
            )
        ]
        
        self.state = SessionState(isLoading: false, cards: mockCards, currentCardIndex: 0, isCardFlipped: false, isSessionComplete: false, sessionStats: state.sessionStats)
    }
    
    func submitGrade(grade: ReviewGrade) {
        let nextIndex = state.currentCardIndex + 1
        if nextIndex >= state.cards.count {
            state = SessionState(isLoading: false, cards: state.cards, currentCardIndex: state.currentCardIndex, isCardFlipped: false, isSessionComplete: true, sessionStats: state.sessionStats)
        } else {
            state = SessionState(isLoading: false, cards: state.cards, currentCardIndex: nextIndex, isCardFlipped: false, isSessionComplete: false, sessionStats: state.sessionStats)
        }
    }
}
