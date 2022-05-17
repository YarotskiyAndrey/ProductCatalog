package com.task.productcatalog.ui


data class CategoryUi(val url: String, val name: String)

data class ProductUi(val url: String, val name: String, val image: String)

sealed class ProductState {
    object EmptyState : ProductState()
    data class ProductDetailsUi(
        val name: String,
        val price: Double,
        val description: String,
        val image: String,
    ) : ProductState()
}
