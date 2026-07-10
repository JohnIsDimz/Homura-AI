# Homura AI Chatbot

A simple Android Chatbot application named "Homura AI" using the `claude-sonnet-4.6` model via the API `https://api.synoxcloud.xyz/ai-chat/claude-sonnet-4.6`.

## Features
- Two modes: Normal Mode and Thinking Mode.
- Built with Kotlin and Java support.
- Compatible with Code on the Go (https://www.appdevforall.org/code-on-the-go).
- Uses Retrofit for API calls and Coroutines for asynchronous operations.

## How to build with Code on the Go
1. Install **Code on the Go** from https://www.appdevforall.org/code-on-the-go.
2. Open the app and create a new project or import this source code.
3. Tap on the "Build" or "Run" button to compile and test the app directly on your Android device.

## Note
The actual response structure from `https://api.synoxcloud.xyz/ai-chat/claude-sonnet-4.6` might differ. Please adjust `ClaudeResponse` in `ClaudeApi.kt` if necessary based on the exact JSON structure returned by the API.
