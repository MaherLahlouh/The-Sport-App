# 365 Sport

365 Sport is a comprehensive Android application designed for sports enthusiasts to stay updated with live scores, news, and league information for Soccer and Basketball.

## 🚀 Features

- **Live Scores**: Real-time updates for soccer and basketball matches with date navigation.
- **Sports News**: Stay informed with the latest headlines and sports articles.
- **League Coverage**: Browse various leagues and view detailed information including standings and fixtures.
- **Personalized Following**: Follow your favorite teams to get a custom feed of their upcoming matches and recent results.
- **Interactive UI**: Modern Material 3 design with dark mode support, pull-to-refresh, and intuitive navigation.

## 🛠 Tech Stack

- **Language**: Java
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 15)
- **Architecture**: Fragment-based UI with Activity-Fragment communication.
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) for API consumption.
- **JSON Parsing**: [Gson](https://github.com/google/gson)
- **Image Loading**: [Glide](https://github.com/bumptech/glide)
- **UI Components**: 
    - Material Design 3
    - ConstraintLayout
    - RecyclerView for efficient lists
    - SwipeRefreshLayout

## 📁 Project Structure

```text
app/src/main/java/com/example/thesportapp/
├── ui/         # Adapters and UI helpers
├── data/       # Local data storage and repositories
├── model/      # Data models/POJOs
├── network/    # Retrofit client and API interfaces
├── util/       # Common utilities
└── *.java      # Main Activity and Fragments (News, Leagues, LiveScores, etc.)
```

## ⚙️ Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/TheSportApp.git
   ```

2. **Open in Android Studio**:
   - Select "Open an Existing Project".
   - Navigate to the `TheSportApp` directory and click OK.

3. **Build the project**:
   - Let Gradle sync and download dependencies.
   - Go to `Build > Make Project`.

4. **Run the app**:
   - Connect an Android device or start an emulator.
   - Click the "Run" button in the toolbar.

## 📝 Dependencies

Key libraries used in this project:
- `androidx.appcompat:appcompat`
- `com.google.android.material:material`
- `com.squareup.retrofit2:retrofit`
- `com.github.bumptech.glide:glide`
- `androidx.swiperefreshlayout:swiperefreshlayout`

---