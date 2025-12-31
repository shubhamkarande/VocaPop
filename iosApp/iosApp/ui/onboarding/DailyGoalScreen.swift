import SwiftUI

struct DailyGoalScreen: View {
    var onGoalSelected: (Int) -> Void
    var onBack: () -> Void
    
    @State private var selectedGoal: Int? = 10
    
    let goals = [
        (label: "Casual", count: 5, desc: "5 words / day"),
        (label: "Regular", count: 10, desc: "10 words / day"),
        (label: "Serious", count: 20, desc: "20 words / day"),
        (label: "Insane", count: 50, desc: "50 words / day"),
    ]

    var body: some View {
        ZStack {
            Theme.backgroundDark.ignoresSafeArea()
            
            VStack(alignment: .leading) {
                Button(action: onBack) {
                    Image(systemName: "arrow.left")
                        .foregroundColor(.white)
                }
                .padding()
                
                Text("Pick a daily goal.")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                    .padding(.horizontal)
                
                Text("You can always change this later.")
                    .foregroundColor(.gray)
                    .padding(.horizontal)
                    .padding(.bottom, 32)
                
                VStack(spacing: 16) {
                    ForEach(goals, id: \.count) { goal in
                        Button(action: { selectedGoal = goal.count }) {
                            HStack {
                                Text(goal.label)
                                    .fontWeight(.bold)
                                    .foregroundColor(.white)
                                Spacer()
                                Text(goal.desc)
                                    .foregroundColor(selectedGoal == goal.count ? Theme.primary : .gray)
                            }
                            .padding()
                            .frame(height: 70)
                            .background(selectedGoal == goal.count ? Theme.primary.opacity(0.1) : Theme.surfaceDark)
                            .cornerRadius(16)
                            .overlay(
                                RoundedRectangle(cornerRadius: 16)
                                    .stroke(selectedGoal == goal.count ? Theme.primary : Color.clear, lineWidth: 2)
                            )
                        }
                    }
                }
                .padding(.horizontal)
                
                Spacer()
                
                Button(action: {
                    if let goal = selectedGoal {
                        onGoalSelected(goal)
                    }
                }) {
                    Text("Continue")
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
