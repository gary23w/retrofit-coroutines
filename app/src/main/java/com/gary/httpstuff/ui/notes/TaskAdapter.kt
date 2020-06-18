
package com.gary.httpstuff.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gary.httpstuff.R
import com.gary.httpstuff.model.Task


/**
 * Displays the tasks from the API, into a list of items.
 */
class TaskAdapter(
    private val onItemLongClick: (String) -> Unit
) : RecyclerView.Adapter<TaskHolder>() {

  private val data: MutableList<Task> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
    return TaskHolder(view)
  }

  override fun getItemCount() = data.size

  override fun onBindViewHolder(holder: TaskHolder, position: Int) {
    holder.bindData(data[position], onItemLongClick)
  }

  fun addData(item: Task) {
    data.add(item)
    notifyItemInserted(data.size)
  }

  fun setData(data: List<Task>) {
    this.data.clear()
    this.data.addAll(data)
    notifyDataSetChanged()
  }

  fun removeTask(taskId: String) {
    val taskIndex = data.indexOfFirst { it.id == taskId }

    if (taskIndex != -1) {
      data.removeAt(taskIndex)
      notifyItemRemoved(taskIndex)
    }
  }
}