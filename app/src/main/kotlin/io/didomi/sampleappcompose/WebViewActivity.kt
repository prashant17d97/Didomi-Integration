package io.didomi.sampleappcompose

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.didomi.sampleappcompose.ui.theme.SampleAppJetpackComposeTheme
import io.didomi.sdk.Didomi

class WebViewActivity : ComponentActivity() {

    companion object {
        private const val URL = "https://didomi.github.io/webpage-for-sample-app-webview/?didomiConfig.notice.enable=false"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleAppJetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AndroidView(factory = {
                        WebView(it)
                            .apply {
                                /** The notice should automatically get hidden in the web view as consent is passed from the
                                 *  mobile app to the website. However, it might happen that the notice gets displayed for a
                                 *  very short time before being hidden. You can disable the notice in your web view to make
                                 *  sure that it never shows by appending didomiConfig.notice.enable=false to the query
                                 *  string of the URL that you are loading **/
                                /** The notice should automatically get hidden in the web view as consent is passed from the
                                 *  mobile app to the website. However, it might happen that the notice gets displayed for a
                                 *  very short time before being hidden. You can disable the notice in your web view to make
                                 *  sure that it never shows by appending didomiConfig.notice.enable=false to the query
                                 *  string of the URL that you are loading **/
                                settings.javaScriptEnabled = true
                                settings.useWideViewPort = true

                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        /** Inject consent information into the WebView **/
                                        val didomi = Didomi.getInstance()
                                        didomi.onReady {
                                            val didomiJavaScriptCode = didomi.getJavaScriptForWebView()
                                            evaluateJavascript(didomiJavaScriptCode) {}
                                        }
                                    }
                                }

                                loadUrl(URL)
                            }
                    })
                }
            }
        }
    }
}
