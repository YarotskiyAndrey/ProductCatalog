package com.task.productcatalog.data.models


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("short_name")
    val shortName: String,
    val url: String
)