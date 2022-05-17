package com.task.productcatalog.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.task.productcatalog.databinding.ItemCategoryBinding
import com.task.productcatalog.ui.CategoryUi

class CategoriesAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private val _list = ArrayList<CategoryUi>()

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        ViewHolder(binding.root) {

        fun setCategory(categoryUi: CategoryUi) {
            binding.tvCategoryName.text = categoryUi.name
            binding.root.setOnClickListener { onItemClickListener.onItemClick(
                categoryUi.url,
                categoryUi.name
            ) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.setCategory(_list[position])
    }

    override fun getItemCount() = _list.size

    fun updateList(list: List<CategoryUi>) {
        val diffUtilCallback = DefaultDiffUtilCallback(_list, list) { old, new ->
            old.url == new.url
        }
        DiffUtil.calculateDiff(diffUtilCallback).dispatchUpdatesTo(this)

        _list.clear()
        _list.addAll(list)
    }
}