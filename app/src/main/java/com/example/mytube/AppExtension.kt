package com.example.mytube

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toolbar
import java.text.SimpleDateFormat

fun isOnline(context:Context): Boolean {
    try {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?: return false
        return when {
            networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } catch (e: Exception) {
        Log.d("isOnlineException", e.toString())
        return false
    }
}
@SuppressLint("SimpleDateFormat")
fun getFormattedDate(eventDate: String): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy:'IST'")
    val date = java.util.Date(eventDate.toLong()*1000L)
    return sdf.format(date)
}
