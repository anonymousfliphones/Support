# Techeaz Support - Android App

A native Android support application that fetches FAQ and support content from a GitHub-hosted JSON file.

## Features

- 📱 **Native Android Experience**: Built with Kotlin and Material Design 3
- 🔍 **Real-time Search**: Search through FAQs with highlighting
- 🏷️ **Category Filtering**: Filter FAQs by categories using chips
- 📢 **Dynamic Announcements**: Display important notices from JSON
- ⚡ **Quick Actions**: Configurable action buttons for common tasks
- 📞 **Contact Integration**: Direct email and phone call functionality
- 🔄 **Auto-refresh**: Fetches latest content from GitHub
- 🎨 **Modern UI**: Material Design 3 with smooth animations

## Architecture

- **MVVM Pattern**: ViewModel, LiveData, Repository pattern
- **Retrofit**: For HTTP networking
- **RecyclerView**: Efficient list display with ViewBinding
- **Material Components**: Modern Android UI components
- **Coroutines**: Asynchronous operations

## Setup

### Prerequisites

1. Android Studio Arctic Fox or later
2. Android SDK 21 (Android 5.0) or higher
3. Kotlin 1.9.0 or later

### Installation

1. Open Android Studio
2. File → Open → Select the `TecheazSupportApp` folder
3. Wait for Gradle sync to complete
4. Update the GitHub configuration in `SupportRepository.kt`:

```kotlin
// In SupportRepository.kt, line 74-77
val username = "YOUR_GITHUB_USERNAME"  // Replace with your username
val repository = "YOUR_REPOSITORY_NAME"  // Replace with your repo name
val branch = "main"  // Your branch name
val filePath = "content.json"  // Path to your JSON file
```

### Building the APK

1. **Debug Build** (for testing):
   ```bash
   ./gradlew assembleDebug
   ```
   APK location: `app/build/outputs/apk/debug/app-debug.apk`

2. **Release Build** (for distribution):
   ```bash
   ./gradlew assembleRelease
   ```
   APK location: `app/build/outputs/apk/release/app-release.apk`

## JSON Configuration

The app fetches content from your `content.json` file hosted on GitHub. The expected structure is:

```json
{
  "app": {
    "name": "Your App Name",
    "description": "App description",
    "version": "1.0.0"
  },
  "contact": {
    "email": "support@yourcompany.com",
    "phone": "+1-555-123-4567",
    "hours": "Business hours",
    "response_time": "Response time info"
  },
  "categories": [
    {
      "id": "category-id",
      "name": "Category Name",
      "description": "Description",
      "icon": "🔧"
    }
  ],
  "faqs": [
    {
      "id": 1,
      "category": "category-id",
      "question": "Your question?",
      "answer": "Your answer here.",
      "tags": ["tag1", "tag2"],
      "helpful": 0,
      "last_updated": "2024-01-15"
    }
  ],
  "announcements": [
    {
      "id": 1,
      "type": "info",
      "title": "Announcement Title",
      "message": "Announcement message",
      "date": "2024-01-15",
      "active": true
    }
  ],
  "quick_actions": [
    {
      "title": "Action Title",
      "description": "Action description",
      "action": "mailto:support@company.com",
      "icon": "📧"
    }
  ]
}
```

## How It Works

1. **App Launch**: Shows splash screen, then loads MainActivity
2. **Content Loading**: Fetches JSON from GitHub using Retrofit
3. **UI Population**: Displays content using RecyclerView adapters
4. **Search & Filter**: Real-time filtering of FAQ items
5. **Contact Actions**: Uses Android intents for email/phone
6. **Refresh**: Pull-to-refresh or manual refresh updates content

## Customization

### Changing Colors
Edit `app/src/main/res/values/colors.xml`:
```xml
<color name="primary">#2563EB</color>
<color name="primary_dark">#1D4ED8</color>
```

### Modifying GitHub URL
Update in `SupportRepository.kt`:
```kotlin
val githubUrl = SupportApiService.buildGitHubUrl(
    username = "your-username",
    repository = "your-repo",
    branch = "main",
    filePath = "content.json"
)
```

### Adding Features
- Create new activities in `java/com/techeaz/support/`
- Add new layouts in `res/layout/`
- Update `AndroidManifest.xml` for new activities

## Building for Production

1. **Generate Signing Key**:
   ```bash
   keytool -genkey -v -keystore my-release-key.keystore -alias alias_name -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Configure Signing** in `app/build.gradle`:
   ```gradle
   android {
       signingConfigs {
           release {
               storeFile file("my-release-key.keystore")
               storePassword "password"
               keyAlias "alias_name"
               keyPassword "password"
           }
       }
       buildTypes {
           release {
               signingConfig signingConfigs.release
               minifyEnabled true
               proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
           }
       }
   }
   ```

3. **Build Release APK**:
   ```bash
   ./gradlew assembleRelease
   ```

## Troubleshooting

### Common Issues

1. **Build Errors**: 
   - Ensure Android SDK is properly configured
   - Check Gradle version compatibility
   - Sync project with Gradle files

2. **Network Issues**:
   - Verify internet permission in AndroidManifest.xml
   - Check GitHub repository is public
   - Validate JSON syntax

3. **Content Not Loading**:
   - Check GitHub URL in SupportRepository.kt
   - Verify JSON file exists and is accessible
   - Review network logs in Android Studio

### Debug Logs

Enable network logging by checking the `HttpLoggingInterceptor` configuration in `SupportRepository.kt`.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly on different devices
5. Submit a pull request

## License

MIT License - feel free to use for personal or commercial projects.

## Support

For technical issues with the Android app:
1. Check the troubleshooting section above
2. Review Android Studio build logs
3. Test on different devices/Android versions
4. Check GitHub Issues for similar problems