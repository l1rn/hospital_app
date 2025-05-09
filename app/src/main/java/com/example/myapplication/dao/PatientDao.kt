package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.models.Patient

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients")
    suspend fun getAllPatients(): List<Patient>

    @Query("SELECT COUNT(*) FROM patients")
    suspend fun count():Int

    @Insert
    suspend fun insert(patient: Patient)

    @Insert
    suspend fun insertAll(patients: List<Patient>)

    @Query("SELECT EXISTS(SELECT * FROM patients WHERE phone = :phone)")
    suspend fun isPatientExists(phone: String): Boolean
}