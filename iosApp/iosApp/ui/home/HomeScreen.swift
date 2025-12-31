import SwiftUI

struct HomeScreen: View {
    var onStartSession: () -> Void
    var onNavClick: (NavigationRoutes.Screen) -> Void
    
    var body: some View {
        ZStack {
            Theme.backgroundDark.ignoresSafeArea()
            
            VStack {
                // Header, Progress, CTA, Decks (Same as before)
                // ... (Truncated for brevity in replacement, but I must provide content?)
                // Actually I need to recreate the VStack content or partial replace.
                // It's safer to overwrite the file or use precise match.
                // The previous Home content was large.
                // I'll rewrite the whole file to ensure correctness with the new tabBar logic.
                // Wait, SwiftUI has built-in TabView, but the design implies a custom bar or just buttons at bottom?
                // Android used a custom bar.
                // I'll simulate a custom bar at the bottom of the ZStack.
                
                ScrollView {
                    VStack {
                        // Header
                        HStack {
                           // ... Header Content
                           HStack {
                                Circle()
                                    .fill(Theme.primary)
                                    .frame(width: 40, height: 40)
                                    .overlay(Text("S").bold().foregroundColor(.white))
                                
                                VStack(alignment: .leading) {
                                    Text("Good Morning,").font(.caption).foregroundColor(.gray)
                                    Text("Subham").font(.headline).fontWeight(.bold).foregroundColor(.white)
                                }
                            }
                            Spacer()
                            HStack {
                                Image(systemName: "bolt.fill")
                                    .foregroundColor(Theme.accentOrange)
                                Text("12")
                                    .fontWeight(.bold)
                                    .foregroundColor(Theme.accentOrange)
                            }
                            .padding(.horizontal, 12)
                            .padding(.vertical, 6)
                            .background(Theme.accentOrange.opacity(0.1))
                            .cornerRadius(20)
                        }
                        .padding()
                        
                        // Progress
                        VStack {
                            ZStack {
                                Circle()
                                    .stroke(Color.gray.opacity(0.3), lineWidth: 12)
                                    .frame(width: 140, height: 140)
                                
                                Circle()
                                    .trim(from: 0, to: 0.7)
                                    .stroke(Theme.primary, style: StrokeStyle(lineWidth: 12, lineCap: .round))
                                    .frame(width: 140, height: 140)
                                    .rotationEffect(.degrees(-90))
                                
                                VStack {
                                    Text("7/10")
                                        .font(.system(size: 32, weight: .bold))
                                        .foregroundColor(.white)
                                    Text("Daily Goal")
                                        .font(.caption)
                                        .foregroundColor(.gray)
                                }
                            }
                            .padding(.top, 24)
                            
                            Text("Ready for review?")
                                .font(.title2)
                                .fontWeight(.bold)
                                .foregroundColor(.white)
                                .padding(.top, 16)
                        }
                        .frame(maxWidth: .infinity)
                        .padding(.bottom, 32)
                        .background(Theme.surfaceDark)
                        .cornerRadius(24)
                        .padding()
                        
                        // CTA
                        Button(action: onStartSession) {
                            HStack {
                                Image(systemName: "play.fill")
                                Text("Start Review Session")
                                    .fontWeight(.bold)
                            }
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .frame(height: 56)
                            .background(Theme.primary)
                            .cornerRadius(16)
                        }
                        .padding(.horizontal)
                        
                        // Decks
                        HStack {
                            Text("Your Decks").font(.headline).foregroundColor(.white)
                            Spacer()
                        }
                        .padding(.horizontal)
                        .padding(.top, 24)
                        
                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 16) {
                                ForEach(0..<3) { i in
                                    DeckCardView(index: i)
                                }
                            }
                            .padding(.horizontal)
                        }
                        
                        // Padding for bottom bar
                        Spacer().frame(height: 80)
                    }
                }
                
            }
            
            // Custom Bottom Bar
            VStack {
                Spacer()
                HStack {
                    NavBarItem(icon: "house.fill", label: "Home", isSelected: true) { onNavClick(.home) }
                    Spacer()
                    NavBarItem(icon: "chart.bar.fill", label: "Progress", isSelected: false) { onNavClick(.progress) }
                    Spacer()
                    NavBarItem(icon: "greetingcard.fill", label: "Decks", isSelected: false) { onNavClick(.decks) }
                    Spacer()
                    NavBarItem(icon: "gearshape.fill", label: "Settings", isSelected: false) { onNavClick(.settings) }
                }
                .padding()
                .background(Theme.surfaceDark)
            }
        }
        .navigationBarHidden(true)
    }
}

struct NavBarItem: View {
    let icon: String
    let label: String
    let isSelected: Bool
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack {
                Image(systemName: icon)
                    .font(.system(size: 24))
                Text(label)
                    .font(.caption2)
            }
            .foregroundColor(isSelected ? Theme.primary : .gray)
        }
    }
}

struct DeckCardView: View {
    let index: Int
    let languages = ["French", "Spanish", "German"]
    let colors = [Theme.secondary, Theme.accentOrange, Theme.accentTeal]
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Text(languages[index % languages.count])
                    .font(.caption)
                    .foregroundColor(.gray)
                Spacer()
                Circle()
                    .fill(colors[index % colors.count])
                    .frame(width: 8, height: 8)
            }
            Spacer()
            Text("Starter Deck")
                .font(.headline)
                .foregroundColor(.white)
            Text("15 due")
                .font(.caption)
                .foregroundColor(colors[index % colors.count])
        }
        .padding()
        .frame(width: 140, height: 100)
        .background(Theme.surfaceDark)
        .cornerRadius(16)
    }
}
