package com.spectrum.moviedbapp.data.database

import androidx.room.*
import com.spectrum.moviedbapp.data.network.model.Results

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<Results>)

    @Query("SELECT * FROM Results")
    suspend fun getAllDbCurrencies(): List<Results>
}

@Database(entities = [Results::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}
