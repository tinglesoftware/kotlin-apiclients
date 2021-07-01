# Tingle Api Client: Android

This is an abstract API client library build on top of
[Okhttp](http://square.github.io/okhttp/) library to ease the process of
making various server request.

### Assumptions

  - Knowledge on development of android applications.
  - Knowledge on how to compile android libraries.

### Features

  - Making POST requests.
  - Making GET requests.
  - Making PUT requests.
  - Making DELETE requests.
  - Making PATCH requests.

## Installation

### Requirements 
* Android 5.0 (API level 21) and above
* [Android Gradle Plugin] (https://developer.android.com/studio/releases/gradle-plugin) 4.2.2

### Configuration 
Add `abstractions` to your `build.gradle` dependencies.

```gradle
implementation 'tingle.software.api:abstractions:VERSION_NUMBER'
```

# Examples

## Configuration

```kotlin
class SampleApiClient: AbstractApiClient(EmptyAuthenticationHeaderProvider()) {
    
    // Get a URL 
    @Throws(IOException::class)
    fun getFromRequest(): ResourceResponse<MyClass> {
        val builder = Request.Builder()
            .url(url)
            .get()

        return execute(builder, MyClass::class.java)
    }

    // Post a URL 
    @Throws(IOException::class)
    fun postToServer(request: MyClass): ResourceResponse<MyClass> {
        val builder = Request.Builder()
            .url(url)
            .post(makeJson(request).toRequestBody(MEDIA_TYPE_JSON))

        return execute(builder, MyClass::class.java)
    }
}

```