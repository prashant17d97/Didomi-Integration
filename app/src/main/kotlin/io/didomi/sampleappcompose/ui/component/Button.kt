package io.didomi.sampleappcompose.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.didomi.sampleappcompose.R
import io.didomi.sampleappcompose.ui.theme.DidomiElectricBlue50
import io.didomi.sampleappcompose.ui.theme.SampleAppJetpackComposeTheme

@Composable
fun Button(label: String, action: () -> Unit) {
    androidx.compose.material.Button(
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(backgroundColor = DidomiElectricBlue50),
        onClick = { action.invoke() },
    ) {
        Text(
            modifier = Modifier
                .padding(start = 14.dp, end = 14.dp, top = 21.dp, bottom = 21.dp)
                .weight(1f),
            color = Color.White,
            text = label,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(resId = R.font.montserrat_medium)),
            letterSpacing = 0.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    SampleAppJetpackComposeTheme {
        Button("Dummy") {}
    }
}
