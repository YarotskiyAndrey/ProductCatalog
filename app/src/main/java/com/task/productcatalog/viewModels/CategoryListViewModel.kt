package com.task.productcatalog.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.productcatalog.data.ProductRepository
import com.task.productcatalog.data.models.Category
import com.task.productcatalog.ui.CategoryUi
import com.task.productcatalog.util.DispatcherProvider
import com.task.productcatalog.util.STOP_FLOW_DELAY
import com.task.productcatalog.util.requestUntilSuccessFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val repository: ProductRepository,
    dispatchers: DispatcherProvider
) :
    ViewModel() {

    private val _errorChannel = Channel<Exception>(UNLIMITED)
    val errorChannel = _errorChannel.receiveAsFlow()

    val categoryListState = requestUntilSuccessFlow(
        dispatchers.io,
        _errorChannel,
        repository::getCategories
    )
        .map { list -> list.map { model -> model.toUiState() } }
        .stateIn(
            viewModelScope,
            WhileSubscribed(STOP_FLOW_DELAY),
            emptyList()
        )

    private fun Category.toUiState(): CategoryUi {
        return CategoryUi(url, shortName)
    }
}

