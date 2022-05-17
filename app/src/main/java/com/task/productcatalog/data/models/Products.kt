package com.task.productcatalog.data.models


data class Products(
    val gridProducts: GridProducts
) {
    data class GridProducts(
        val elements: List<Product>
    ) {
        data class Product(
            val fullName: String,
            val primaryImage: String,
            val url: String,
        )
    }
}