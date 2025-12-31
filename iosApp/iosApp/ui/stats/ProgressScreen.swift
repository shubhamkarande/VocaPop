import SwiftUI

struct ProgressScreen: View {
    var body: some View {
        ZStack {
            Theme.backgroundDark.ignoresSafeArea()
            
            VStack {
                Text("Your Progress")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                    .padding()
                
                Spacer()
                
                RoundedRectangle(cornerRadius: 16)
                    .fill(Theme.surfaceDark)
                    .frame(height: 200)
                    .overlay(Text("Activity Chart Placeholder").foregroundColor(.gray))
                    .padding()
                
                HStack(spacing: 20) {
                    StatBox(label: "Total XP", value: "1,240")
                    StatBox(label: "Time", value: "4.2h")
                    StatBox(label: "Words", value: "143")
                }
                .padding()
                
                Spacer()
            }
        }
    }
}

struct StatBox: View {
    let label: String
    let value: String
    
    var body: some View {
        VStack {
            Text(value)
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(Theme.primary)
            Text(label)
                .font(.caption)
                .foregroundColor(.gray)
        }
        .frame(maxWidth: .infinity)
        .padding()
        .background(Theme.surfaceDark)
        .cornerRadius(12)
    }
}
