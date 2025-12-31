import SwiftUI

struct LanguageOption: Identifiable {
    let id: String
    let name: String
    let flag: String
}

let languages = [
    LanguageOption(id: "es", name: "Spanish", flag: "🇪🇸"),
    LanguageOption(id: "fr", name: "French", flag: "🇫🇷"),
    LanguageOption(id: "de", name: "German", flag: "🇩🇪"),
    LanguageOption(id: "ja", name: "Japanese", flag: "🇯🇵")
]

struct LanguageSelectionScreen: View {
    var onLanguageSelected: (String) -> Void
    var onBack: () -> Void
    
    @State private var selectedId: String? = nil
    
    let columns = [
        GridItem(.flexible()),
        GridItem(.flexible())
    ]

    var body: some View {
        ZStack {
            Theme.backgroundDark.ignoresSafeArea()
            
            VStack {
                // Top Bar
                HStack {
                    Button(action: onBack) {
                        Image(systemName: "arrow.left")
                            .foregroundColor(.white)
                    }
                    Spacer()
                }
                .padding()
                
                Text("What do you want to learn?")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                    .padding(.bottom, 32)
                
                ScrollView {
                    LazyVGrid(columns: columns, spacing: 16) {
                        ForEach(languages) { language in
                            LanguageCard(
                                language: language,
                                isSelected: selectedId == language.id,
                                action: { selectedId = language.id }
                            )
                        }
                    }
                    .padding(.horizontal)
                }
                
                Button(action: {
                    if let id = selectedId {
                        onLanguageSelected(id)
                    }
                }) {
                    Text("Continue")
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 56)
                        .background(selectedId != nil ? Theme.primary : Color.gray.opacity(0.3))
                        .cornerRadius(16)
                }
                .disabled(selectedId == nil)
                .padding()
            }
        }
        .navigationBarHidden(true)
    }
}

struct LanguageCard: View {
    let language: LanguageOption
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack {
                Text(language.flag)
                    .font(.system(size: 50))
                Text(language.name)
                    .fontWeight(.bold)
                    .foregroundColor(isSelected ? Theme.primary : .white)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 140)
            .background(isSelected ? Theme.primary.opacity(0.1) : Theme.surfaceDark)
            .cornerRadius(20)
            .overlay(
                RoundedRectangle(cornerRadius: 20)
                    .stroke(isSelected ? Theme.primary : Color.clear, lineWidth: 2)
            )
        }
    }
}
