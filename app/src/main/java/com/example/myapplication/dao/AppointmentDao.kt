package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.models.Appointment
import com.example.myapplication.services.AppointmentFullInfo

@Dao
interface AppointmentDao {
    @Insert
    suspend fun insert(appointment: Appointment)

    @Transaction
    @Query("SELECT * FROM appointments")
    fun getAllAppointmentsWithDetails(): List<AppointmentFullInfo>

    @Transaction
    @Query("SELECT * FROM appointments WHERE doctorId = :doctorId")
    fun getAppointmentsByDoctorId(doctorId: Int): List<AppointmentFullInfo>
}