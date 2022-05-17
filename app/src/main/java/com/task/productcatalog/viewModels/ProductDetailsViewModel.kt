package com.task.productcatalog.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.productcatalog.data.ProductRepository
import com.task.productcatalog.data.models.ProductDetails
import com.task.productcatalog.ui.ProductDetailsActivity
import com.task.productcatalog.ui.ProductState
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
class ProductDetailsViewModel @Inject constructor(
    private val repository: ProductRepository,
    dispatchers: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _errorChannel = Channel<Exception>(Channel.UNLIMITED)
    val errorChannel = _errorChannel.receiveAsFlow()

    val productState = requestUntilSuccessFlow(
        dispatchers.io,
        _errorChannel,
        { repository.getProduct(savedStateHandle[ProductDetailsActivity.PRODUCT_URL_EXTRA]!!) }
    )
        .map { model -> model.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(STOP_FLOW_DELAY),
            ProductState.EmptyState
        )

    private fun ProductDetails.toUiState(): ProductState.ProductDetailsUi {
        return ProductState.ProductDetailsUi(fullName, minSalePrice, description, primaryImage)
    }

}