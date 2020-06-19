
package com.gary.httpstuff.ui.register

import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.gary.httpstuff.App
import com.gary.httpstuff.R
import com.gary.httpstuff.model.Success
import com.gary.httpstuff.model.request.UserDataRequest
import com.gary.httpstuff.networking.NetworkStatusChecker
import com.gary.httpstuff.networking.RemoteApi
import com.gary.httpstuff.utils.gone
import com.gary.httpstuff.utils.toast
import com.gary.httpstuff.utils.visible
import kotlinx.android.synthetic.main.activity_register.*


/**
 * Displays the Register screen, with the options to register, or head over to Login!
 */
@RequiresApi(Build.VERSION_CODES.M)
class RegisterActivity : AppCompatActivity() {

  private val networkStatusChecker by lazy {
    NetworkStatusChecker(getSystemService(ConnectivityManager::class.java))
  }
  private val remoteApi = App.remoteApi

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    initUi()
  }

  private fun initUi() {
    register.setOnClickListener {
      processData(nameInput.text.toString(), emailInput.text.toString(),
          passwordInput.text.toString())
    }
  }

  private fun processData(username: String, email: String, password: String) {
    if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
      networkStatusChecker.performIfConnectedToInternet {
        remoteApi.registerUser(UserDataRequest(email, password, username)) { result ->
            if (result is Success) {
              toast(result.data)
              onRegisterSuccess()
            } else  {
              onRegisterError()
            }
        }
      }
    } else {
      onRegisterError()
    }
  }

  private fun onRegisterSuccess() {
    errorText.gone()
    finish()
  }

  private fun onRegisterError() {
    errorText.visible()
  }
}