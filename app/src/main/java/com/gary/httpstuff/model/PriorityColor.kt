

package com.gary.httpstuff.model

import com.gary.httpstuff.R


/**
 * Describes the task priority levels, when coloring the task items.
 */
enum class PriorityColor {

  LOW, MEDIUM, HIGH;

  fun getColor() = when (this) {
    LOW -> R.color.priorityLow
    MEDIUM -> R.color.priorityMedium
    HIGH -> R.color.priorityHigh
  }
}