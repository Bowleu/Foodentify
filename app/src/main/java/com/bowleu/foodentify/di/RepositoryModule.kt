package com.bowleu.foodentify.di

import com.bowleu.foodentify.data.repository.RoomRetrofitProductRepository
import com.bowleu.foodentify.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: RoomRetrofitProductRepository
    ): ProductRepository
}