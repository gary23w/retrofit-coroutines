
package com.gary.httpstuff.ui.notes.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.gary.httpstuff.R
import com.gary.httpstuff.model.PriorityColor
import com.gary.httpstuff.model.Task
import com.gary.httpstuff.model.request.AddTaskRequest
import com.gary.httpstuff.networking.RemoteApi
import com.gary.httpstuff.utils.toast
import kotlinx.android.synthetic.main.fragment_dialog_new_task.*


/**
 * Dialog fragment to create a new task.
 */
class AddTaskDialogFragment : DialogFragment() {

  private var taskAddedListener: TaskAddedListener? = null
  private val remoteApi = RemoteApi()

  interface TaskAddedListener {
    fun onTaskAdded(task: Task)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
  }

  fun setTaskAddedListener(listener: TaskAddedListener) {
    taskAddedListener = listener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_dialog_new_task, container)
  }

  override fun onStart() {
    super.onStart()
    dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()
    initListeners()
  }

  private fun initUi() {
    context?.let {
      prioritySelector.adapter =
          ArrayAdapter<PriorityColor>(it, android.R.layout.simple_spinner_dropdown_item,
              PriorityColor.values())
      prioritySelector.setSelection(0)
    }
  }

  private fun initListeners() = saveTaskAction.setOnClickListener { saveTask() }

  private fun saveTask() {
    if (isInputEmpty()) {
      context?.toast(getString(R.string.empty_fields))
      return
    }

    val title = newTaskTitleInput.text.toString()
    val content = newTaskDescriptionInput.text.toString()
    val priority = prioritySelector.selectedItemPosition + 1

    remoteApi.addTask(AddTaskRequest(title, content, priority)) { task, error ->
      if (task != null) {
        onTaskAdded(task)
      } else if (error != null) {
        onTaskAddFailed()
      }
    }
    clearUi()
  }


  private fun clearUi() {
    newTaskTitleInput.text.clear()
    newTaskDescriptionInput.text.clear()
    prioritySelector.setSelection(0)
  }

  private fun isInputEmpty(): Boolean = TextUtils.isEmpty(
      newTaskTitleInput.text) || TextUtils.isEmpty(newTaskDescriptionInput.text)

  private fun onTaskAdded(task: Task) {
    taskAddedListener?.onTaskAdded(task)
    dismiss()
  }

  private fun onTaskAddFailed() {
    this.activity?.toast("Something went wrong!")
  }
}