package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.models.Patient

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients")
    suspend fun getAllPatients(): List<Patient>
    @Insert
    suspend fun insert(patient: Patient)
}