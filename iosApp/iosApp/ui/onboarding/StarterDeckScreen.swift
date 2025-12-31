import SwiftUI

struct StarterDeckScreen: View {
    var onContinue: () -> Void

    var body: some View {
        ZStack {
            Theme.backgroundDark.ignoresSafeArea()
            
            VStack {
                Spacer()
                
                Circle()
                    .fill(Theme.primary.opacity(0.1))
                    .frame(width: 120, height: 120)
                    .overlay(
                        Image(systemName: "sparkles")
                            .font(.system(size: 50))
                            .foregroundColor(Theme.primary)
                    )
                
                Text("Pack Unlocked!")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                    .padding(.top, 32)
                
                Text("Most Common 100 Words")
                    .font(.title2)
                    .foregroundColor(Theme.primary)
                    .padding(.top, 8)
                
                Text("We've added a starter deck to your collection to get you going.")
                    .multilineTextAlignment(.center)
                    .foregroundColor(.gray)
                    .padding(.horizontal, 40)
                    .padding(.top, 8)
                
                Spacer()
                
                Button(action: onContinue) {
                    Text("Let's Go!")
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 56)
                        .background(Theme.primary)
                        .cornerRadius(16)
                }
                .padding()
            }
        }
        .navigationBarHidden(true)
    }
}
