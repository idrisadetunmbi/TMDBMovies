# TMDBMovies

TMDBMovies is an Android Application based on the [The Movies Database API](https://www.themoviedb.org/documentation/api)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* [Android Studio](developer.android.com/studio/) (Preferably >= 3.4)
* A TMDB Developer Account. Specifically, the following keys from the account
  * [API Key](https://www.themoviedb.org/settings/api)
  * [session id](https://developers.themoviedb.org/3/authentication/validate-request-token) (required for marking movie as favorite feature)


### Installing

```
Clone Project in Android Studio and sync Gradle files
```
```
# Specify the Api key and Session Id in env.xml file
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="api_key">api_key_from_developer_account</string>
    <string name="session_id">session_id</string>
</resources>
```
```
# Run the app from Android Studio afterwards on an emulator or a connected device
```

## Deployment

The app is not currently deployed to any app store. Deploying should be done based on the docs for the Android guides

## Built With

* [Kotlin](https://kotlinlang.org)
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)

## Contributing

The project can be open for contribution. Please feel free raise a PR to suggest improvements.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* [The MovieDB](www.themoviedb.org/)
