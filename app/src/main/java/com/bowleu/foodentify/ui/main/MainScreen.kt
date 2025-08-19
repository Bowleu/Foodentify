package com.bowleu.foodentify.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bowleu.foodentify.R
import com.bowleu.foodentify.ui.common.AppBranding
import com.bowleu.foodentify.ui.common.DefaultScreenScaffold
import com.bowleu.foodentify.ui.common.Screen
import com.bowleu.foodentify.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel) {
    DefaultScreenScaffold { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            contentAlignment = Alignment.Center
            ) {
            Column(modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                val fontSize = 20.sp
                Button(onClick = {
                    navController.navigate(Screen.Scanner.route)
                }, modifier = Modifier.padding(bottom = 8.dp)
                    .fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp)) {
                    Text(text = stringResource(R.string.scan_button),
                        fontSize = fontSize,
                        fontFamily = nunitoFontFamily
                    )
                }
                Text(text = stringResource(R.string.or_text),
                    fontSize = fontSize,
                    modifier = Modifier.padding(bottom = 8.dp))
                TextField(
                    placeholder = @Composable{Text(text = stringResource(R.string.barcode_placeholder))},
                    label = @Composable{ Text(text = stringResource(R.string.input_manually_text)) },
                    value = viewModel.barcodeText,
                    onValueChange = viewModel::onBarcodeTextChanged,
                    textStyle = TextStyle(fontSize = fontSize),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun MainScreenPreview() {
    MainScreen(navController = NavController(LocalContext.current), viewModel = MainViewModel())
}
