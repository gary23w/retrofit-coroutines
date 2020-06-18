
package com.gary.httpstuff.ui.notes

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gary.httpstuff.R
import com.gary.httpstuff.model.Task
import com.gary.httpstuff.networking.NetworkStatusChecker
import com.gary.httpstuff.networking.RemoteApi
import com.gary.httpstuff.ui.notes.dialog.AddTaskDialogFragment
import com.gary.httpstuff.ui.notes.dialog.TaskOptionsDialogFragment
import com.gary.httpstuff.utils.gone
import com.gary.httpstuff.utils.toast
import com.gary.httpstuff.utils.visible
import kotlinx.android.synthetic.main.fragment_notes.*

/**
 * Fetches and displays notes from the API.
 */
class NotesFragment : Fragment(), AddTaskDialogFragment.TaskAddedListener,
    TaskOptionsDialogFragment.TaskOptionSelectedListener {

  private val adapter by lazy { TaskAdapter(::onItemSelected) }
  private val remoteApi = RemoteApi()

  private val networkStatusChecker by lazy {
    NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_notes, container, false)
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()
    initListeners()
  }

  private fun initUi() {
    progress.visible()
    noData.visible()
    tasksRecyclerView.layoutManager = LinearLayoutManager(context)
    tasksRecyclerView.adapter = adapter
    getAllTasks()
  }

  private fun initListeners() {
    addTask.setOnClickListener { addTask() }
  }

  private fun onItemSelected(taskId: String) {
    val dialog = TaskOptionsDialogFragment.newInstance(taskId)
    dialog.setTaskOptionSelectedListener(this)
    dialog.show(childFragmentManager, dialog.tag)
  }

  override fun onTaskAdded(task: Task) {
    adapter.addData(task)
  }

  private fun addTask() {
    val dialog = AddTaskDialogFragment()
    dialog.setTaskAddedListener(this)
    dialog.show(childFragmentManager, dialog.tag)
  }

  private fun getAllTasks() {
    progress.visible()

    networkStatusChecker.performIfConnectedToInternet {

      remoteApi.getTasks { tasks, error ->
        activity?.runOnUiThread {
          if (tasks.isNotEmpty()) {
            onTaskListReceived(tasks)
          } else if (error != null) {
            onGetTasksFailed()
          }
        }
      }
    }
  }

  private fun checkList(notes: List<Task>) {
    if (notes.isEmpty()) noData.visible() else noData.gone()
  }

  private fun onTasksReceived(tasks: List<Task>) = adapter.setData(tasks)

  private fun onTaskListReceived(tasks: List<Task>) {
    progress.gone()
    checkList(tasks)
    onTasksReceived(tasks)
  }

  private fun onGetTasksFailed() {
    progress.gone()
    activity?.toast("Failed to fetch tasks!")
  }

  override fun onTaskDeleted(taskId: String) {
    adapter.removeTask(taskId)
    activity?.toast("Task deleted!")
  }

  override fun onTaskCompleted(taskId: String) {
    adapter.removeTask(taskId)
    activity?.toast("Task completed!")
  }
}