package io.didomi.sampleappcompose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import io.didomi.sampleappcompose.ui.component.Home
import io.didomi.sampleappcompose.ui.component.HomeCallback
import io.didomi.sampleappcompose.ui.theme.SampleAppJetpackComposeTheme
import io.didomi.sdk.Didomi
import io.didomi.sdk.events.ConsentChangedEvent
import io.didomi.sdk.events.EventListener

// Extends `FragmentActivity` instead of `ComponentActivity`
class MainActivity : FragmentActivity() {

    companion object {
        private const val TAG = "Didomi - Sample App"
    }

    private val didomi = Didomi.getInstance()

    private val callback = object : HomeCallback {

        override fun fetchConsent() {
            didomi.onReady {
                this@MainActivity.fetchConsent()
            }
        }

        override fun showVendorsPreferences() {
            didomi.onReady {
                didomi.showPreferences(this@MainActivity, "vendors")
            }
        }

        override fun showWebView() {
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            startActivity(intent)
        }

        override fun showAd() {
            if (interstitialAd != null) {
                interstitialAd?.show(this@MainActivity)
            } else {
                Log.d(TAG, "The interstitial ad wasn't ready yet.")
                loadAd()
            }
        }
    }

    private fun fetchConsent() {
        val purpose = didomi.requiredPurposes
        val vendors = didomi.requiredPurposes
        val currentUserStatus = didomi.currentUserStatus
        Log.d(
            TAG,
            "fetchConsent: \nPurpose: $purpose,\nVendors: $vendors,\n currentUserStatus:$currentUserStatus \nUserStatus: ${currentUserStatus.purposes.entries}"
        )


    }

    private var interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SampleAppJetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Home(callback = callback)
                }
            }
        }

        didomi.setupUI(this)
        prepareAd()
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    interstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    this@MainActivity.interstitialAd = interstitialAd

                    interstitialAd.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad was dismissed.")
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.d(TAG, "Ad failed to show.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Ad showed fullscreen content.")
                                this@MainActivity.interstitialAd = null
                                loadAd()
                            }
                        }
                }
            })
    }

    /**
     * Will reset / preload Ad after each consent change
     * Consent allows Ads: the ad cache will be prepared (ad is displayed on first click after reject -> accept)
     * Consent rejects Ads: the ad cache will be purged (no ad on first click after reject)
     */
    private fun prepareAd() {
        didomi.onReady {
            loadAd()

            val didomiEventListener = object : EventListener() {
                override fun consentChanged(event: ConsentChangedEvent) {
                    // The consent status of the user has changed
                    loadAd()
                }
            }
            didomi.addEventListener(didomiEventListener as EventListener)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SampleAppJetpackComposeTheme {
        Home()
    }
}
