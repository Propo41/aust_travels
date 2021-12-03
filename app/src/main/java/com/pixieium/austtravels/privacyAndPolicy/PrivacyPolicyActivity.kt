package com.pixieium.austtravels.privacyAndPolicy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.ActivityHomeBinding
import com.pixieium.austtravels.databinding.ActivityPrivacyPolicyBinding
import android.view.View

import android.webkit.WebView
import android.webkit.WebViewClient


class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        val url = "https://docs.google.com/document/d/e/2PACX-1vS5wewwH80RD_aSnROlwgscRDkVB8kJSArf23JHzGqmHKL9V1fR1AnFEQ1IzIdC1ectabtbwChxxE8l/pub?embedded=true"
        val myWebView: WebView = binding.privacyPolicy
        myWebView.loadUrl(url)
        myWebView.webViewClient = WebViewClient()


    }


}