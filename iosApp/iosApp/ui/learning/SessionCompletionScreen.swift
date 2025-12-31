import SwiftUI

struct SessionCompletionScreen: View {
    var onHomeClick: () -> Void

    var body: some View {
        ZStack {
             Theme.backgroundDark.ignoresSafeArea()
             
             VStack {
                 Text("Session Complete!")
                    .font(.largeTitle)
                    .foregroundColor(.white)
                 
                 Button(action: onHomeClick) {
                     Text("Back to Home")
                        .foregroundColor(.white)
                        .padding()
                        .background(Theme.primary)
                        .cornerRadius(10)
                 }
                 .padding()
             }
        }
        .navigationBarHidden(true)
    }
}
