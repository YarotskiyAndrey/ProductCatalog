package com.task.productcatalog.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.task.productcatalog.data.FakeRepository
import com.task.productcatalog.data.ProductRepository
import com.task.productcatalog.data.models.Category
import com.task.productcatalog.ui.CategoryUi
import com.task.productcatalog.util.DispatcherProvider
import com.task.productcatalog.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CategoryListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val testDispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = testDispatcher
        override val io: CoroutineDispatcher
            get() = testDispatcher
        override val default: CoroutineDispatcher
            get() = testDispatcher
        override val unconfined: CoroutineDispatcher
            get() = testDispatcher
    }
    private lateinit var categoryListViewModel: CategoryListViewModel
    private lateinit var repository: ProductRepository

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun categoryListState_SuccessfulRequest_containsEmptyStateAndList() = runTest {
        val mockCategories = listOf(Category(shortName = "mockName", url = "mockUrl"))
        repository = FakeRepository(getCategoriesMock = { Resource.Success(mockCategories) })
        categoryListViewModel = CategoryListViewModel(repository, testDispatcherProvider)

        val states = categoryListViewModel.categoryListState.take(2).toList()

        val firstState = states[0]
        val secondState = states[1]

        val expectedSecondState = listOf(CategoryUi(url = "mockUrl", name = "mockName"))

        assertThat(firstState).isEmpty()
        assertThat(secondState).containsExactlyElementsIn(expectedSecondState)
    }

    @Test
    fun errorChannel_FailedRequest_containsException() = runTest {
        val exception = Exception()
        repository = FakeRepository(getCategoriesMock = { Resource.Failure(exception) })
        categoryListViewModel = CategoryListViewModel(repository, testDispatcherProvider)

        val job = launch {
            //start flow
            categoryListViewModel.categoryListState.collect()
        }
        val actual = categoryListViewModel.errorChannel.first()
        assertThat(actual).isEqualTo(exception)
        job.cancel()
    }
}