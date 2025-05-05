package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.models.Service

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services")
    suspend fun getAllServices(): List<Service>

    @Insert
    suspend fun insertAll(services: List<Service>)

    @Query("SELECT COUNT(*) FROM services")
    suspend fun count(): Int
}
