package com.homuraai.chatbot

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class Message(val role: String, val content: String)
data class ClaudeRequest(val messages: List<Message>, val model: String, val mode: String = "normal")
data class ClaudeResponse(val reply: String) // Adjust based on actual API response structure

interface ClaudeApi {
    @Headers("Content-Type: application/json")
    @POST("ai-chat/claude-sonnet-4.6")
    suspend fun sendMessage(@Body request: ClaudeRequest): ClaudeResponse
}
