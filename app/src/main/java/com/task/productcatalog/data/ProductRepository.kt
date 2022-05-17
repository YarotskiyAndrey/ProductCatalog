package com.task.productcatalog.data

import com.task.productcatalog.data.models.Category
import com.task.productcatalog.data.models.ProductDetails
import com.task.productcatalog.data.models.Products.GridProducts.Product
import com.task.productcatalog.util.Resource

interface ProductRepository {
    suspend fun getCategories(): Resource<List<Category>>
    suspend fun getProducts(categoryUrl: String): Resource<List<Product>>
    suspend fun getProduct(productUrl: String): Resource<ProductDetails>
}