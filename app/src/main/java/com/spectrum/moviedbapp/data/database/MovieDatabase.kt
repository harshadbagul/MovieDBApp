package com.spectrum.moviedbapp.data.database

import androidx.room.*
import com.spectrum.moviedbapp.data.network.model.Genres
import com.spectrum.moviedbapp.data.network.model.Results

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genres>)

    @Query("SELECT * FROM Genres")
    suspend fun getAllGenres(): List<Genres>
}

@Database(entities = [Genres::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}
