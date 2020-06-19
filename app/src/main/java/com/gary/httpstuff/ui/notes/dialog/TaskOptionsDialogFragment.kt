
package com.gary.httpstuff.ui.notes.dialog

import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.gary.httpstuff.App
import com.gary.httpstuff.R
import com.gary.httpstuff.model.Success
import com.gary.httpstuff.networking.NetworkStatusChecker
import com.gary.httpstuff.networking.RemoteApi
import kotlinx.android.synthetic.main.fragment_dialog_task_options.*


@RequiresApi(Build.VERSION_CODES.M)
class TaskOptionsDialogFragment : DialogFragment() {

  private var taskOptionSelectedListener: TaskOptionSelectedListener? = null

  private val remoteApi = App.remoteApi

  private val networkStatusChecker by lazy {
    NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
  }

  companion object {
    private const val KEY_TASK_ID = "task_id"

    fun newInstance(taskId: String): TaskOptionsDialogFragment = TaskOptionsDialogFragment().apply {
      arguments = Bundle().apply {
        putString(KEY_TASK_ID, taskId)
      }
    }
  }

  interface TaskOptionSelectedListener {
    fun onTaskDeleted(taskId: String)

    fun onTaskCompleted(taskId: String)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_dialog_task_options, container)
  }

  override fun onStart() {
    super.onStart()
    dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()
  }

  private fun initUi() {
    val taskId = arguments?.getString(KEY_TASK_ID) ?: ""
    if (taskId.isEmpty()) dismissAllowingStateLoss()

    deleteTask.setOnClickListener {
      remoteApi.deleteTask(taskId) { result ->

          if (result is Success) {
            taskOptionSelectedListener?.onTaskDeleted(taskId)
          }
          dismissAllowingStateLoss()
        }
    }

    completeTask.setOnClickListener {
      networkStatusChecker.performIfConnectedToInternet {

        remoteApi.completeTask(taskId) { error ->

          if (error == null) {
            taskOptionSelectedListener?.onTaskCompleted(taskId)
          }
          dismissAllowingStateLoss()
        }
      }
    }
  }

  fun setTaskOptionSelectedListener(taskOptionSelectedListener: TaskOptionSelectedListener) {
    this.taskOptionSelectedListener = taskOptionSelectedListener
  }
}