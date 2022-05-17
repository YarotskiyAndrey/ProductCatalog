package com.task.productcatalog.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.productcatalog.data.ProductRepository
import com.task.productcatalog.data.models.Products
import com.task.productcatalog.ui.ProductListActivity
import com.task.productcatalog.ui.ProductUi
import com.task.productcatalog.util.DispatcherProvider
import com.task.productcatalog.util.STOP_FLOW_DELAY
import com.task.productcatalog.util.requestUntilSuccessFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository,
    dispatchers: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _errorChannel = Channel<Exception>(Channel.UNLIMITED)
    val errorChannel = _errorChannel.receiveAsFlow()

    val productListState = requestUntilSuccessFlow(
        dispatchers.io,
        _errorChannel,
        { repository.getProducts(savedStateHandle[ProductListActivity.CATEGORY_URL_EXTRA]!!) }
    )
        .map { list -> list.map { model -> model.toUiState() } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(STOP_FLOW_DELAY),
            emptyList()
        )

    private fun Products.GridProducts.Product.toUiState(): ProductUi {
        return ProductUi(url, fullName, primaryImage)
    }
}