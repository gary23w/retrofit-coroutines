
package com.gary.httpstuff.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gary.httpstuff.App
import com.gary.httpstuff.R
import com.gary.httpstuff.model.Success
import com.gary.httpstuff.networking.RemoteApi
import com.gary.httpstuff.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * Displays the user profile information.
 */
class ProfileFragment : Fragment() {

  private val remoteApi = App.remoteApi

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()

    remoteApi.getUserProfile { result ->
      if (result is Success) {
        userEmail.text = result.data.email
        userName.text = getString(R.string.user_name_text, result.data.name)
        numberOfNotes.text = getString(R.string.number_of_notes_text, result.data.numberOfNotes)
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