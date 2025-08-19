package com.bowleu.foodentify.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bowleu.foodentify.R
import com.bowleu.foodentify.ui.theme.nunitoFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultScreenScaffold(content:  @Composable ((PaddingValues) -> Unit)) {
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                AppBranding()
            }
        )
    }
    ) {
        innerPadding -> content(innerPadding)
    }
}

@Preview
@Composable
fun DefaultScreenScaffoldPreview() {
    DefaultScreenScaffold {  }
}

@Composable
fun AppBranding(modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Image(painter = painterResource(R.drawable.icon),
            contentDescription = stringResource(R.string.icon_content_description),
            modifier = Modifier.size(27.dp))
        Text(text = stringResource(R.string.app_name),
            modifier = Modifier,
            fontSize = 23.sp,
            fontFamily = nunitoFontFamily,
            color = Color(0xFFA5D6A7)
        )
    }
}

@Preview
@Composable
fun AppBrandingPreview() {
    AppBranding()
}