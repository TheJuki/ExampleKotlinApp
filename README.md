# Example Kotlin Android App

> Get started using Kotlin with Android

## Features
- KFormMaster to build the forms
- Drawer to open different activities
- Settings with a font size and dark theme
- Login page
- Splash screen
- Retrofit to make API calls to the Example API
- GSON for handling JSON data
- RxJava and RxAndroid to get the result or error from Retrofit calls
- Firebase integration for Notification and Crashlytics support
- secure-preferences for secure user preferences
- MPAndroidChart for displaying pie charts
- richeditor-android for displaying a HTML editor
- barcodescanner:zxing for displaying a QR code scanner
- PermissionsDispatcher for asking for the QR camera permission
- LolliPin for securing the app with a passcode
- dokka for documentation

## Screenshots
| [Login](https://github.com/TheJuki/ExampleKotlinApp/blob/master/screenshots/Login.png) | [Drawer](https://github.com/TheJuki/ExampleKotlinApp/blob/master/screenshots/Drawer.png) | [Search Contacts](https://github.com/TheJuki/ExampleKotlinApp/blob/master/screenshots/Search%20Contacts.png) |
| --- | --- | --- |
![Login](https://github.com/TheJuki/ExampleKotlinApp/blob/master/screenshots/Login.png) | ![Drawer](https://github.com/TheJuki/ExampleKotlinApp/blob/master/screenshots/Drawer.png) | ![Search Contacts](https://github.com/TheJuki/ExampleKotlinApp/blob/master/screenshots/Search%20Contacts.png) |

## Get Started
- Clone this repository and open it in Android Studio
- Integrate with Firebase to get a google-services.json file 
    - In Android Studio, open the Tools menu and then Firebase
    - Click "Cloud Messaging" and then "Set up Cloud Messaging"
    - Click "Connect to Firebase" and then Create a new Firebase Project
    - Don't click "Add FCM to your app" as that is already setup 
- After starting the app, login using the Example API below
- Start by searching for Contacts with the name "User"
- Add a Contact, edit an existing one, or add a HTML Note to a Contact
- Open the Queue to view a refreshable, API-driven Chart fragment within a programmatically added tab
- Open Settings to change the font size or theme. Enable the Passcode to setup a passcode required when opening the app from the background.
- NOTE: The server will stop accepting requests after 24 hours of being logged in. Re-login to get a new session.

## Example API
- Use the Example API to login [Example API](https://example-api-thejuki.glitch.me)

## Contributing
You can submit pull requests or issues to this project to make this example Android app even better!

## Libraries
* [Example API](https://github.com/TheJuki/ExampleAPI)
* [KFormMaster](https://github.com/TheJuki/KFormMaster)
* [Retrofit](https://github.com/square/retrofit)
* [GSON](https://github.com/google/gson)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [Firebase](https://github.com/firebase/quickstart-android)
* [secure-preferences](https://github.com/scottyab/secure-preferences)
* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
* [richeditor-android](https://github.com/wasabeef/richeditor-android)
* [barcodescanner:zxing](https://github.com/dm77/barcodescanner)
* [PermissionsDispatcher](https://github.com/permissions-dispatcher/PermissionsDispatcher)
* [LolliPin](https://github.com/omadahealth/LolliPin)
* [dokka](https://github.com/Kotlin/dokka)

## References
* [Refactoring utility classes with Kotlin : Shared Preferences](https://medium.com/@krupalshah55/manipulating-shared-prefs-with-kotlin-just-two-lines-of-code-29af62440285)
* [Splash Screens the Right Way](https://www.bignerdranch.com/blog/splash-screens-the-right-way)
* [materialistic](https://github.com/hidroh/materialistic)

License
-----------------
This Android app is available as open source under the terms of the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
