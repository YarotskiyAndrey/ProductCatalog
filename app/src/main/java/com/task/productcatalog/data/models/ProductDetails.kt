package com.task.productcatalog.data.models

import com.google.gson.annotations.SerializedName

data class ProductDetails(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("min_sale_price")
    val minSalePrice: Double,
    val description: String,
    @SerializedName("primary_image")
    val primaryImage: String
)