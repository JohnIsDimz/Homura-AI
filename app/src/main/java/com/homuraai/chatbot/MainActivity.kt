package com.homuraai.chatbot

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var normalMode: RadioButton
    private lateinit var thinkingMode: RadioButton
    private lateinit var adapter: MessageAdapter

    private val api: ClaudeApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.synoxcloud.xyz/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClaudeApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        progressBar = findViewById(R.id.progressBar)
        normalMode = findViewById(R.id.normalMode)
        thinkingMode = findViewById(R.id.thinkingMode)

        adapter = MessageAdapter(mutableListOf())
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = adapter

        sendButton.setOnClickListener {
            val text = messageInput.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
                messageInput.text.clear()
            }
        }
    }

    private fun sendMessage(text: String) {
        val userMessage = Message("user", text)
        adapter.addMessage(userMessage)
        chatRecyclerView.scrollToPosition(adapter.itemCount - 1)

        progressBar.visibility = View.VISIBLE
        sendButton.isEnabled = false

        val mode = if (thinkingMode.isChecked) "thinking" else "normal"
        val request = ClaudeRequest(
            messages = listOf(userMessage),
            model = "claude-sonnet-4.6",
            mode = mode
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = api.sendMessage(request)
                withContext(Dispatchers.Main) {
                    adapter.addMessage(Message("assistant", response.reply))
                    chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    sendButton.isEnabled = true
                }
            }
        }
    }
}
