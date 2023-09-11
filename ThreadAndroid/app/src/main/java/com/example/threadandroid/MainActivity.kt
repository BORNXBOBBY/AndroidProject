package com.example.threadandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val tag = "coroutines"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helloWorldText = findViewById<Button>(R.id.helloworld)

        helloWorldText.text.split("")

        CoroutineScope(Dispatchers.IO).launch {
            showUsers()
            showAdmin()
        }

        helloWorldText.setOnClickListener {
            /* thread(start = true) {
                Log.d("coroutines", "running on" + Thread.currentThread().name)
                for (i in 1..10000000000L) {

                }

                Log.d("coroutines", "running on " + Thread.currentThread().name)
            }
        }
        */

        }

    }
    suspend fun showUsers() {
        Log.d(tag, "first work is done")
        yield()
        Log.d(tag, "second work is started")
    }

    suspend fun showAdmin() {
        Log.d(tag, "admin work is done")
        yield()
        Log.d(tag, "second admin work is done")
    }

    fun coroutinesFun() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("coroutines", "running on" + Thread.currentThread().name)

        }

        GlobalScope.launch(Dispatchers.Main) {
            Log.d("coroutines", "running on" + Thread.currentThread().name)
        }

        GlobalScope.launch(Dispatchers.Default) {
            Log.d("coroutines", "running on" + Thread.currentThread().name)
        }
    }
}
