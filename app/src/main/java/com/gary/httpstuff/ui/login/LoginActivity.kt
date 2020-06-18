
package com.gary.httpstuff.ui.login

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gary.httpstuff.App
import com.gary.httpstuff.R
import com.gary.httpstuff.model.request.UserDataRequest
import com.gary.httpstuff.networking.NetworkStatusChecker
import com.gary.httpstuff.networking.RemoteApi
import com.gary.httpstuff.ui.main.MainActivity
import com.gary.httpstuff.ui.register.RegisterActivity
import com.gary.httpstuff.utils.gone
import com.gary.httpstuff.utils.visible
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Displays the Login screen, with the options to head over to the Register screen.
 */
class LoginActivity : AppCompatActivity() {



  private val remoteApi = RemoteApi()


  private val networkStatusChecker by lazy {
    NetworkStatusChecker(getSystemService(ConnectivityManager::class.java))
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    initUi()

    if(App.getToken().isNotBlank()) {
      startActivity(MainActivity.getIntent(this))
    }
  }

  private fun initUi() {
    login.setOnClickListener {
      val email = emailInput.text.toString()
      val password = passwordInput.text.toString()

      if (email.isNotBlank() && password.isNotBlank()) {
        logUserIn(UserDataRequest(email, password))
      } else {
        showLoginError()
      }
    }
    register.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
  }

  private fun logUserIn(userDataRequest: UserDataRequest) {
    networkStatusChecker.performIfConnectedToInternet {
      remoteApi.loginUser(userDataRequest) { token: String?, throwable: Throwable? ->
        if (token != null && token.isNotBlank()) {
          onLoginSuccess(token)
        } else if (throwable != null) {
          showLoginError()
        }
      }
    }
  }

  private fun onLoginSuccess(token: String) {
    errorText.gone()
    App.saveToken(token)
    startActivity(MainActivity.getIntent(this))
  }

  private fun showLoginError() {
    errorText.visible()
  }
}