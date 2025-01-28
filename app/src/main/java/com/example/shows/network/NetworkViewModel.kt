package com.example.shows.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NetworkViewModel(
    networkObserver: NetworkObserver
) : ViewModel() {

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected

    init {
        viewModelScope.launch {
            networkObserver.isConnected.collectLatest { isConnected ->
                _isConnected.value = isConnected
            }
        }
    }
}
