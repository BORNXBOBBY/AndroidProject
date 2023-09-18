package com.example.yati.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.example.yati.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : YatiActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        setWebview()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setWebview() {
        webview.webViewClient = WebViewClient()
        webview.apply {
            val webURL = intent.getStringExtra("webURL")
            loadUrl(webURL.toString())
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
        }
    }

    override fun onBackPressed() {
        this.finish()
    }
}