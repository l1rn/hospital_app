package com.example.myapplication.models

import android.graphics.Bitmap
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class Doctor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?,
    val email: String,
    val phone: String,
    val photoUri: Int,
    val role: Role = Role.DOCTOR,
    val experience: Int
)