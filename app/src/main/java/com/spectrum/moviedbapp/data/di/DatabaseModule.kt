package com.spectrum.moviedbapp.data.di

import android.content.Context
import androidx.room.Room
import com.spectrum.moviedbapp.data.database.MovieDao
import com.spectrum.moviedbapp.data.database.MovieDatabase
import com.spectrum.moviedbapp.data.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): MovieDatabase {
        return Room.databaseBuilder(
            appContext,
            MovieDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCurrencyDao(database: MovieDatabase): MovieDao {
        return database.movieDao
    }

}