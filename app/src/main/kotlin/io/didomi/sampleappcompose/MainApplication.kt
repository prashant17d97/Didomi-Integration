package io.didomi.sampleappcompose

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import io.didomi.sampleappcompose.model.CustomVendor
import io.didomi.sdk.Didomi
import io.didomi.sdk.DidomiInitializeParameters
import io.didomi.sdk.events.ConsentChangedEvent
import io.didomi.sdk.events.EventListener

class MainApplication : Application() {

    companion object {
        private const val TAG = "Didomi - Sample App"
    }

    private var didomiEventListener: EventListener? = null

    override fun onCreate() {
        super.onCreate()

        Didomi.getInstance().setLogLevel(Log.VERBOSE)

        try {
            val didomiInitializeParameters = DidomiInitializeParameters(
                "7dd8ec4e-746c-455e-a610-99121b4148df", // Replace with your API key
                null,
                null,
                null,
                false,
                null,
                "DVLP9Qtd" // Replace with your notice ID, or remove if using domain targeting
            )

            // Initialize the SDK
            Didomi.getInstance().initialize(
                this,
                didomiInitializeParameters
            )

            // Do not use the Didomi.getInstance() object here for anything else than registering your ready listener
            // The SDK might not be ready yet
            Didomi.getInstance().onReady {

                // Load your custom vendors in the onReady callback.
                // These vendors need to be conditioned manually to the consent status of the user.
                loadVendor()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while initializing the Didomi SDK", e)
        }

        // Load the IAB vendors; the consent status will be shared automatically with them.
        // Regarding the Google Mobile Ads SDK, we also delay App Measurement as described here:
        // https://developers.google.com/admob/ump/android/quick-start#delay_app_measurement_optional
        MobileAds.initialize(this) {
            Log.d(TAG, "MobileAds initialized.")
        }
    }

    private fun loadVendor() {
        val vendorId = "c:customven-gPVkJxXD"
        val didomi = Didomi.getInstance()
        val status = didomi.userStatus
        val isVendorEnabled = status.vendors.global.enabled.contains(vendorId)

        // Remove any existing event listener
        didomiEventListener?.let {
            didomi.removeEventListener(it)
        }

        if (isVendorEnabled) {
            // We have consent for the vendor
            // Initialize the vendor SDK
            CustomVendor.initialize()
        } else {
            // We do not have consent information yet
            // Wait until we get the user information
            didomiEventListener = object : EventListener() {
                override fun consentChanged(event: ConsentChangedEvent) {
                    loadVendor()
                }
            }
            didomi.addEventListener(didomiEventListener as EventListener)
        }
    }
}
