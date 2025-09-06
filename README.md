# Health App – Injury Detection

An Android application that helps users identify potential injuries based on symptoms. The app leverages the **Google Custom Search API** and **web scraping (jsoup)** to pull information from public health sources, then ranks possible injuries by keyword frequency.

## Features
- **Symptom Lookup** – Select a body region (Head, Arm, Leg, Torso) and enter symptoms  
- **Google Custom Search API** – Queries health related resources dynamically  
- **Web Scraping with jsoup** – Parses search results and counts injury-related keywords  
- **Ranked Injury List** – Displays possible conditions ordered by relevance  
- **Asynchronous Requests** – Uses `AsyncTask` for background network and parsing operations  

## Tech Stack
- **Language:** Java  
- **Libraries:** [jsoup](https://jsoup.org/) for HTML parsing  
- **APIs:** Google Custom Search API  
- **Tools:** Android Studio, Gradle  
- **UI:** Android Support Libraries (AppCompat, ConstraintLayout)  

## Future Improvements
- Migrate codebase from Java to **Kotlin** for cleaner syntax and null safety  
- Adopt **Jetpack components** (ViewModel, LiveData, WorkManager)  
- Replace `AsyncTask` with **Retrofit/OkHttp + coroutines**  
- Add **Firebase** for logging queries and storing user history  
- Implement **Espresso/UI test suite** for automated validation  
- Upgrade UI with **Material 3** for a modern look  
