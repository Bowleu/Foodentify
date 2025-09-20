package com.bowleu.foodentify.di

import android.app.Application
import androidx.room.Room
import com.bowleu.foodentify.data.local.ProductDao
import com.bowleu.foodentify.data.local.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProductDatabase(app: Application): ProductDatabase =
        Room.databaseBuilder(app, ProductDatabase::class.java, "app.db").build()

    @Provides
    @Singleton
    fun provideProductDao(db: ProductDatabase): ProductDao = db.getProductDao()

}