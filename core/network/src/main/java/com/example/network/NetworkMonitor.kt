package com.example.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(private val context: Context) {

    private val _isConnected = MutableLiveData<Boolean>(false)
    val isConnected: LiveData<Boolean> get() = _isConnected

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isConnected.postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _isConnected.postValue(false)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val hasInternet =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            _isConnected.postValue(hasInternet)
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun register() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}