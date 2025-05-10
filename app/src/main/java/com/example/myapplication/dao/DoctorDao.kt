package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.models.Doctor
import com.example.myapplication.models.Patient
import com.example.myapplication.services.DoctorService
import com.example.myapplication.services.DoctorWithServices

@Dao
interface DoctorDao {
    @Insert
    suspend fun insert(doctor: Doctor)

    @Insert
    suspend fun insertDoctorService(doctorService: DoctorService)

    @Insert
    suspend fun insertAll(doctors: List<Doctor>)

    @Query("SELECT * FROM doctors WHERE id = :doctorId")
    suspend fun getDoctorWithServices(doctorId: Int): DoctorWithServices

    @Query("SELECT * FROM doctors")
    suspend fun getAllDoctorsWithServices(): List<DoctorWithServices>

    @Query("SELECT COUNT(*) FROM doctors")
    suspend fun countDoctors(): Int

    @Query("SELECT EXISTS(SELECT * FROM doctors WHERE phone = :phone)")
    suspend fun isDoctorExists(phone: String): Boolean

}