package com.task.productcatalog.data

import com.task.productcatalog.data.api.ProductApi
import com.task.productcatalog.data.models.Category
import com.task.productcatalog.data.models.ProductDetails
import com.task.productcatalog.data.models.Products.GridProducts.Product
import com.task.productcatalog.util.Resource
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val productApi: ProductApi,
) : ProductRepository {

    override suspend fun getCategories(): Resource<List<Category>> {
        return try {
            val result = productApi.getAllCategories()
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getProducts(categoryUrl: String): Resource<List<Product>> {
        return try {
            val result = productApi.getProductsByCategory(categoryUrl)
            val products = result.gridProducts.elements
            Resource.Success(products)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getProduct(productUrl: String): Resource<ProductDetails> {
        return try {
            val result = productApi.getProduct(productUrl)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}