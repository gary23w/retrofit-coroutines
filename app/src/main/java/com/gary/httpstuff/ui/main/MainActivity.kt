
package com.gary.httpstuff.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gary.httpstuff.R
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Main Screen, represents different fragments for each section of the app.
 */
class MainActivity : AppCompatActivity() {

  private val pagerAdapter by lazy { MainPagerAdapter(supportFragmentManager) }

  companion object {
    fun getIntent(context: Context): Intent {
      val intent = Intent(context, MainActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initUi()
  }

  private fun initUi() {
    tabs.setupWithViewPager(fragmentPager)
    fragmentPager.adapter = pagerAdapter
  }
}
