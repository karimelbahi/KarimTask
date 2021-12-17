package com.example.TentwentAssignment.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
data class ImageEntity(
    @PrimaryKey()
    @ColumnInfo(name="id")
    val id : Int? = null,
    @ColumnInfo(name="response")
    val response : String
)