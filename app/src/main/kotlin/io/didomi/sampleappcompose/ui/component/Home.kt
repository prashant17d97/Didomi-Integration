package io.didomi.sampleappcompose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.didomi.sampleappcompose.R
import io.didomi.sampleappcompose.ui.theme.DidomiGrey50
import io.didomi.sampleappcompose.ui.theme.SampleAppJetpackComposeTheme

interface HomeCallback {
    fun fetchConsent()
    fun showVendorsPreferences()
    fun showWebView()
    fun showAd()
}

@Composable
fun Home(callback: HomeCallback? = null) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(DidomiGrey50)) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(label = stringResource(id = R.string.button_show_purposes_preferences)) {
                callback?.fetchConsent()
            }

            Button(label = stringResource(id = R.string.button_show_vendors_preferences)) {
                callback?.showVendorsPreferences()
            }

            Button(label = stringResource(id = R.string.button_show_web_view)) {
                callback?.showWebView()
            }

            Button(label = stringResource(id = R.string.button_show_ad)) {
                callback?.showAd()
            }
        }

        Image(
            modifier = Modifier
                .size(120.dp, 64.dp)
                .align(Alignment.BottomCenter),
            contentDescription = stringResource(id = R.string.app_name),
            painter = painterResource(id = R.drawable.didomi_logo),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    SampleAppJetpackComposeTheme {
        Home()
    }
}
