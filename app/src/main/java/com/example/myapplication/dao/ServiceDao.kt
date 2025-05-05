package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.models.Service

@Dao
interface ServiceDao {
    @Insert
    suspend fun insertAll(services: List<Service>)

    @Query("SELECT COUNT(*) FROM services")
    suspend fun count(): Int
}
