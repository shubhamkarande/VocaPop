import SwiftUI

struct WelcomeScreen: View {
    var onStartLearning: () -> Void
    var onLoginClick: () -> Void
    
    @State private var animateBlob = false

    var body: some View {
        ZStack {
            Theme.backgroundDark.ignoresSafeArea()
            
            // Blobs
            Circle()
                .fill(Theme.primary.opacity(0.3))
                .frame(width: 300, height: 300)
                .offset(x: -100, y: -200)
                .blur(radius: 60)
            
            Circle()
                .fill(Theme.secondary.opacity(0.2))
                .frame(width: 250, height: 250)
                .offset(x: 100, y: 100)
                .blur(radius: 50)
                
            VStack {
                Spacer()
                
                // Hero Image Placeholder (3D Float)
                ZStack {
                    RoundedRectangle(cornerRadius: 30)
                        .fill(LinearGradient(
                            colors: [Theme.secondary, Theme.primary],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        ))
                        .frame(width: 200, height: 200)
                        .rotationEffect(.degrees(animateBlob ? 5 : -5))
                        .offset(y: animateBlob ? -10 : 10)
                        .animation(
                            Animation.easeInOut(duration: 3).repeatForever(autoreverses: true),
                            value: animateBlob
                        )
                    
                    Text("👋")
                        .font(.system(size: 80))
                }
                .padding(.bottom, 60)
                
                Text("VocaPop")
                    .font(.system(size: 40, weight: .bold))
                    .foregroundColor(.white)
                
                Text("Master languages\nwith space repetition.")
                    .font(.body)
                    .multilineTextAlignment(.center)
                    .foregroundColor(.gray)
                    .padding(.top, 8)
                
                Spacer()
                
                Button(action: onStartLearning) {
                    Text("Start Learning")
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 56)
                        .background(Theme.primary)
                        .cornerRadius(16)
                }
                .padding(.horizontal, 24)
                
                Button(action: onLoginClick) {
                    Text("I already have an account")
                        .font(.subheadline)
                        .foregroundColor(Theme.primary)
                }
                .padding(.top, 16)
                .padding(.bottom, 40)
            }
        }
        .onAppear {
            animateBlob = true
        }
    }
}
