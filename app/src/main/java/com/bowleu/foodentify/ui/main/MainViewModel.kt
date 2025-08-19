package com.bowleu.foodentify.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    var barcodeText by mutableStateOf("")
        private set

    fun onBarcodeTextChanged(text: String) {
        barcodeText = text
    }
}