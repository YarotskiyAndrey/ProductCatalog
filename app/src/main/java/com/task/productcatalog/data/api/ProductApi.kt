package com.task.productcatalog.data.api

import com.task.productcatalog.data.models.Category
import com.task.productcatalog.data.models.ProductDetails
import com.task.productcatalog.data.models.Products
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("api/0.2/categories")
    suspend fun getAllCategories(): List<Category>

    @GET("iv-api/0.3/catalog/{identifier}/products?_iv_include=gridProducts")
    suspend fun getProductsByCategory(@Path("identifier") categoryUrl: String): Products

    @GET("api/0.3/products/{identifier}")
    suspend fun getProduct(@Path("identifier") productUrl: String): ProductDetails
}