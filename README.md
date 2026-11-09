# Terrible Movie Search

Two-screen Android app (Kotlin, Volley, Picasso) that searches OMDb and shows the first result's poster.

## Build
1. Get a free key at https://www.omdbapi.com/apikey.aspx and activate it from the email link.
2. In the project root `local.properties` (git-ignored) add:
   ```
   OMDB_API_KEY=your_key_here
   ```
3. Open the project in Android Studio, sync Gradle, and run the `app` configuration.

The key is read in `app/build.gradle.kts` and exposed as `BuildConfig.OMDB_API_KEY`; it is not stored in source.

## Screenshots
TODO: add six screenshots (Screen 1 and Screen 2 on phone portrait, phone landscape, tablet).

## AI-generated images
None. The logo and placeholders are hand-written vector drawables (`ic_logo`, `ic_loading`, `ic_no_poster`).
The launcher icon is the Android Studio default.

## Known issues
None known. Not yet verified on all six device/orientation combinations.
