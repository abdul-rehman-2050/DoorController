package com.asta.door.controller.ui.browse

import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.asta.door.controller.AppSettings
import com.asta.door.controller.base.BaseActivity
import com.asta.door.controller.databinding.ActivityBrowseBinding

class BrowseActivity : BaseActivity<ActivityBrowseBinding>(
    ActivityBrowseBinding::inflate
) {

    override fun initializeControls() {
        binding.webView.apply {
            isVerticalScrollBarEnabled = false
            settings.javaScriptEnabled = true
            settings.setJavaScriptEnabled(true)

            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            settings.setPluginState(WebSettings.PluginState.ON);
            val newUA =
                "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0"
            settings.setUserAgentString(newUA)




            setLayerType(View.LAYER_TYPE_HARDWARE, null)


            webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    )
                    return true
                }
            }
            loadUrl(AppSettings.localHostURL)
        }
    }

    override fun attachListeners() {
        binding.ivBack.setOnClickListener { onBackPressed() }
    }
}