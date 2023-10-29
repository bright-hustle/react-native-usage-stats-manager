package com.usagestatsmanager.utils

/**
 * Created by BarryAllen
 *
 * @TheBotBox boxforbot@gmail.com
 */
enum class SortOrder(var sort: Int) {
  TODAY(0), YESTERDAY(1), THIS_WEEK(2), MONTH(3), THIS_YEAR(4);

  companion object {
    fun getSortEnum(sort: Int): SortOrder {
      when (sort) {
        0 -> return TODAY
        1 -> return YESTERDAY
        2 -> return THIS_WEEK
        3 -> return MONTH
        4 -> return THIS_YEAR
      }
      return TODAY
    }
  }
}
