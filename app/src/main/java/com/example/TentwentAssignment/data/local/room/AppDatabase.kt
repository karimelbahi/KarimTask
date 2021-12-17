package com.example.TentwentAssignment.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.TentwentAssignment.data.local.entity.*

@Database(entities = [MovieEntity::class, MovieDetailEntity::class, VideoEntity::class, ImageEntity::class, CategoriesEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase(){

    abstract fun appDao(): AppDao

}