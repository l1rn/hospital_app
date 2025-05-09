package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.models.Doctor
import com.example.myapplication.models.Patient
import com.example.myapplication.services.DoctorWithServices

@Dao
interface DoctorDao {
    @Insert
    suspend fun insert(doctor: Doctor)

    @Query("SELECT * FROM doctors")
    suspend fun getAllDoctors(): List<Doctor>

    @Transaction
    @Query("SELECT * FROM doctors WHERE id = :doctorId")
    suspend fun getDoctorWithServices(doctorId: Int): DoctorWithServices
}