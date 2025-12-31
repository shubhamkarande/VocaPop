import SwiftUI

struct SettingsScreen: View {
    var body: some View {
        ZStack {
            Theme.backgroundDark.ignoresSafeArea()
            
            VStack {
                Text("Settings")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                    .padding()
                
                VStack(spacing: 0) {
                    SettingsRow(label: "Account", value: "subham@vocapop.com")
                    SettingsRow(label: "Target Language", value: "French")
                    SettingsRow(label: "Daily Goal", value: "10 words")
                    SettingsRow(label: "Notifications", value: "Enabled")
                    SettingsRow(label: "App Version", value: "1.0.0")
                }
                .background(Theme.surfaceDark)
                .cornerRadius(12)
                .padding()
                
                Spacer()
                
                Button(action: {}) {
                    Text("Log Out")
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.red.opacity(0.8))
                        .cornerRadius(12)
                }
                .padding()
            }
        }
    }
}

struct SettingsRow: View {
    let label: String
    let value: String
    
    var body: some View {
        VStack {
            HStack {
                Text(label).foregroundColor(.white)
                Spacer()
                Text(value).foregroundColor(Theme.primary)
            }
            .padding()
            Divider().background(Color.gray.opacity(0.2))
        }
    }
}
