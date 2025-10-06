package com.bowleu.foodentify.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavController
import com.bowleu.foodentify.R
import com.bowleu.foodentify.ui.theme.nunitoFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultScreenScaffold(shouldShowBackButton: Boolean = false,
                       shouldShowTitle: Boolean = true,
                       navController: NavController? = null,
                       content:  @Composable ((PaddingValues) -> Unit)) {
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                if (shouldShowTitle) {
                    AppBranding()
                }
            },
            navigationIcon = {
                if (shouldShowBackButton) {
                    IconButton(
                        onClick = {
                            navController?.popBackStack()
                        },
                        modifier = Modifier.size(48.dp) // больше стандартных 40dp
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = stringResource(R.string.back_content_description),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        )
    }
    ) {
            innerPadding -> content(innerPadding)
    }
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
fun DefaultScreenScaffoldPreview() {
    DefaultScreenScaffold(
        shouldShowBackButton = true,
        shouldShowTitle = true
    ) {  }
}

@Preview
@Composable
fun AppBrandingPreview() {
    AppBranding()
}