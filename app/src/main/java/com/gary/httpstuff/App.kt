
package com.gary.httpstuff

import android.app.Application
import android.content.Context
import com.gary.httpstuff.networking.RemoteApi
import com.gary.httpstuff.networking.buildApiService

private const val KEY_PREFERENCES = "taskie_preferences"
private const val KEY_TOKEN = "token"

class App : Application() {

  companion object {
    private lateinit var instance: App

    private val preferences by lazy {
      instance.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
      preferences.edit()
          .putString(KEY_TOKEN, token)
          .apply()
    }

    fun getToken() = preferences.getString(KEY_TOKEN, "") ?: ""
    private val apiService by lazy {
      buildApiService()
    }
    val remoteApi by lazy { RemoteApi(apiService) }
  }

  override fun onCreate() {
    super.onCreate()
    instance = this
  }
}