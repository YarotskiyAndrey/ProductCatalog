package com.task.productcatalog.di

import com.task.productcatalog.data.DefaultProductRepository
import com.task.productcatalog.data.ProductRepository
import com.task.productcatalog.data.api.ApiConstants.PRODUCT_BASE_URL
import com.task.productcatalog.data.api.HeaderInterceptor
import com.task.productcatalog.data.api.ProductApi
import com.task.productcatalog.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClientApi(headerInterceptor: HeaderInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideProductApi(okHttpClient: OkHttpClient): ProductApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(PRODUCT_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductApi::class.java)

    @Singleton
    @Provides
    fun provideProductRepository(api: ProductApi): ProductRepository = DefaultProductRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}