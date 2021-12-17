package com.example.TentwentAssignment.ui.movie

import android.content.Context
import android.util.Log
import com.example.TentwentAssignment.data.local.entity.*
import com.example.TentwentAssignment.data.local.room.AppDao
import com.example.TentwentAssignment.data.remote.endpoint.ApiService
import com.example.TentwentAssignment.util.Resource
import com.example.TentwentAssignment.util.Utility
import com.example.TentwentAssignment.util.Utility.networkBoundResource
import com.google.gson.Gson
import kotlinx.coroutines.flow.*

class MovieRepository(
    private val apiService: ApiService,
    private val appDao: AppDao,
    private val gson: Gson,
    private val context: Context
) {


    fun getMovies(
        apiKey: String,
        language: String,
        page: String
    ): Flow<Resource<MovieEntity>> {
        return networkBoundResource(
            query = { appDao.queryMovieResponse(page.toInt()) },
            fetch = { apiService.onMoviesList(apiKey, language, page) },
            saveFetchResult = { items ->
                appDao.insertMovieResponse(
                    MovieEntity(
                        page.toInt(),
                        gson.toJson(items)
                    )
                )
            },
            onFetchFailed = { throwable -> Log.e("ERROR", throwable.message.toString()) },
            shouldFetch = { (Utility.checkConnection(context)) }
        )
    }

    fun getMovieDetail(
        apiKey: String,
        movieId: Int
    ): Flow<Resource<MovieDetailEntity>> {
        return networkBoundResource(
            query = { appDao.queryMovieDetailResponse(movieId) },
            fetch = { apiService.onMovieDetail(movieId, apiKey) },
            saveFetchResult = { items ->
                appDao.insertMovieDetailResponse(
                    MovieDetailEntity(
                        movieId,
                        gson.toJson(items)
                    )
                )
            },
            onFetchFailed = { throwable -> Log.e("ERROR", throwable.message.toString()) },
            shouldFetch = { (Utility.checkConnection(context)) }
        )
    }

    fun getVideo(
        apiKey: String,
        movieId: Int
    ): Flow<Resource<VideoEntity>> {
        return networkBoundResource(
            query = { appDao.queryVideoResponse(movieId) },
            fetch = { apiService.onVideo(movieId, apiKey) },
            saveFetchResult = { items ->
                appDao.insertVideoResponse(
                    VideoEntity(
                        movieId,
                        gson.toJson(items)
                    )
                )
            },
            onFetchFailed = { throwable -> Log.e("ERROR", throwable.message.toString()) },
            shouldFetch = { (Utility.checkConnection(context)) }
        )
    }

    fun getImage(
        apiKey: String,
        movieId: Int
    ): Flow<Resource<ImageEntity>> {
        return networkBoundResource(
            query = { appDao.queryImageResponse(movieId) },
            fetch = { apiService.onImages(movieId, apiKey) },
            saveFetchResult = { items ->
                appDao.insertImageResponse(
                    ImageEntity(
                        movieId,
                        gson.toJson(items)
                    )
                )
            },
            onFetchFailed = { throwable -> Log.e("ERROR", throwable.message.toString()) },
            shouldFetch = { (Utility.checkConnection(context)) }
        )
    }

    fun getCategoriesList(
        apiKey: String
    ): Flow<Resource<CategoriesEntity>> {

        return networkBoundResource(
            query = { appDao.queryMoviesCategoriesResponse() },
            fetch = { apiService.getCategoriesList(apiKey) },
            saveFetchResult = { items ->
                appDao.insertMoviesCategoriesResponse(
                    CategoriesEntity(
                        response = gson.toJson(items)
                    )
                )
            },
            onFetchFailed = { throwable -> Log.e("ERROR", throwable.message.toString()) },
            shouldFetch = { (Utility.checkConnection(context)) }
        )
    }

    suspend fun getFilteredCategoriesList(apiKey: String, category: String) =
        flow { emit(apiService.onMovieCategoriesFilterList(apiKey, category)) }

}