
package com.gary.httpstuff.ui.profile

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.gary.httpstuff.App
import com.gary.httpstuff.R
import com.gary.httpstuff.model.Success
import com.gary.httpstuff.networking.NetworkStatusChecker
import com.gary.httpstuff.networking.RemoteApi
import com.gary.httpstuff.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * Displays the user profile information.
 */
@RequiresApi(Build.VERSION_CODES.M)
class ProfileFragment : Fragment() {

  private val remoteApi = App.remoteApi

  private val networkStatusChecker by lazy {
    NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()

    networkStatusChecker.performIfConnectedToInternet {
      GlobalScope.launch(Dispatchers.Main) {
        val result = remoteApi.getUserProfile()

        if (result is Success) {
          userName.text = result.data.name
          userEmail.text = result.data.email
          numberOfNotes.text = result.data.numberOfNotes.toString()
        }
      }

    }
  }

  private fun initUi() {
    logOut.setOnClickListener {
      App.saveToken("")
      startActivity(Intent(activity, LoginActivity::class.java))
      activity?.finish()
    }
  }
}