package com.example.TentwentAssignment.data.local.room

import androidx.room.*
import com.example.TentwentAssignment.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    //Movies

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieResponse(movie: MovieEntity) : Long

    @Query("SELECT * FROM movie where id = :id")
    fun queryMovieResponse(id: Int): Flow<MovieEntity>

    //Movie Detail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetailResponse(movie: MovieDetailEntity) : Long

    @Query("SELECT * FROM movie_detail where id = :id")
    fun queryMovieDetailResponse(id: Int): Flow<MovieDetailEntity>


    //Video

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoResponse(movie: VideoEntity) : Long

    @Query("SELECT * FROM video where id = :id")
    fun queryVideoResponse(id: Int): Flow<VideoEntity>

    //Image

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageResponse(movie: ImageEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviesCategoriesResponse(categoriesEntity: CategoriesEntity) : Long

    @Query("SELECT * FROM image where id = :id")
    fun queryImageResponse(id: Int): Flow<ImageEntity>


    @Query("SELECT * FROM movies_categories")
    fun queryMoviesCategoriesResponse(): Flow<CategoriesEntity>

}