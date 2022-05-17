package com.task.productcatalog.data

import com.task.productcatalog.data.models.Category
import com.task.productcatalog.data.models.ProductDetails
import com.task.productcatalog.data.models.Products
import com.task.productcatalog.util.Resource

class FakeRepository(
    var getCategoriesMock: () -> Resource<List<Category>> = {
        Resource.Failure(Exception())
    },
    var getProductsMock: (categoryUrl: String) -> Resource<List<Products.GridProducts.Product>> = {
        Resource.Failure(Exception())
    },
    var getProductMock: (productUrl: String) -> Resource<ProductDetails> = {
        Resource.Failure(Exception())
    }
) : ProductRepository {
    override suspend fun getCategories(): Resource<List<Category>> {
        return getCategoriesMock()
    }

    override suspend fun getProducts(categoryUrl: String): Resource<List<Products.GridProducts.Product>> {
        return getProductsMock(categoryUrl)
    }

    override suspend fun getProduct(productUrl: String): Resource<ProductDetails> {
        return getProductMock(productUrl)
    }

}
