package com.task.productcatalog.ui.adapters

import androidx.recyclerview.widget.DiffUtil

class DefaultDiffUtilCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val areItemsTheSame: (T, T) -> Boolean
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}