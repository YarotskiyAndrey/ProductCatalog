package com.task.productcatalog.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.task.productcatalog.databinding.ItemProductBinding
import com.task.productcatalog.ui.ProductUi
import com.task.productcatalog.util.ImageVisibilityRequestListener
import com.task.productcatalog.util.getImageUrl
import com.task.productcatalog.util.loadImage

class ProductsAdapter(private val context: Context) :
    Adapter<ProductsAdapter.ProductViewHolder>() {

    private val _list = ArrayList<ProductUi>()

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        ViewHolder(binding.root) {

        fun setProduct(productState: ProductUi) {
            binding.tvProductName.text = productState.name
            val imageUrl = context.getImageUrl(productState.image)

            loadImage(
                context = context,
                image = imageUrl,
                imageView = binding.ivProductImage,
                requestListener = ImageVisibilityRequestListener(binding.ivProductImage)
            )

            binding.root.setOnClickListener {
                (context as? OnItemClickListener)?.onItemClick(productState.url, productState.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.setProduct(_list[position])
    }

    override fun getItemCount() = _list.size

    fun updateList(list: List<ProductUi>) {
        val diffUtilCallback = DefaultDiffUtilCallback(_list, list) { old, new ->
            old.url == new.url
        }
        DiffUtil.calculateDiff(diffUtilCallback).dispatchUpdatesTo(this)

        _list.clear()
        _list.addAll(list)
    }
}

