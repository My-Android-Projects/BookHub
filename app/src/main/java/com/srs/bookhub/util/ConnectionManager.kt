package com.srs.bookhub.util

import android.content.Context
import android.net.ConnectivityManager

class ConnectionManager {
    fun checkConnectivity(context:Context):Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activenNetwork=connectivityManager.activeNetworkInfo
        if(activenNetwork?.isConnected!=null)
        {
            return activenNetwork.isConnected
        }
        else
        {
            return false
        }
    }
}