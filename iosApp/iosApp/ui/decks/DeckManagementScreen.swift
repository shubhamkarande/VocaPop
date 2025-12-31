import SwiftUI

struct DeckManagementScreen: View {
    var onDeckClick: (String) -> Void
    var onAddDeck: () -> Void

    var body: some View {
        ZStack {
            Theme.backgroundDark.ignoresSafeArea()
            
            VStack {
                HStack {
                    Text("My Decks")
                        .font(.largeTitle)
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                    Spacer()
                    Button(action: onAddDeck) {
                        Image(systemName: "plus.circle.fill")
                            .font(.title)
                            .foregroundColor(Theme.primary)
                    }
                }
                .padding()
                
                ScrollView {
                    VStack(spacing: 16) {
                        ForEach(0..<5) { i in
                            DeckRowItem(name: "Deck \(i)", count: "20 cards") {
                                onDeckClick("deck_\(i)")
                            }
                        }
                    }
                    .padding()
                }
            }
        }
    }
}

struct DeckRowItem: View {
    let name: String
    let count: String
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack {
                Image(systemName: "folder.fill")
                    .foregroundColor(Theme.primary)
                VStack(alignment: .leading) {
                    Text(name)
                        .font(.headline)
                        .foregroundColor(.white)
                    Text(count)
                        .font(.subheadline)
                        .foregroundColor(.gray)
                }
                Spacer()
                Image(systemName: "chevron.right")
                    .foregroundColor(.gray)
            }
            .padding()
            .background(Theme.surfaceDark)
            .cornerRadius(12)
        }
    }
}
