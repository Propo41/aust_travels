package com.pixieium.austtravels.settings

import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.pixieium.austtravels.databinding.ActivityPrivacyPolicyBinding


class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val url =
            "https://docs.google.com/document/d/e/2PACX-1vS5wewwH80RD_aSnROlwgscRDkVB8kJSArf23JHzGqmHKL9V1fR1AnFEQ1IzIdC1ectabtbwChxxE8l/pub?embedded=true"
        val myWebView: WebView = binding.privacyPolicy
        myWebView.loadUrl(url)
        myWebView.webViewClient = WebViewClient()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

}