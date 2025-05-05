package com.example.myapplication.services

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.myapplication.models.Doctor
import com.example.myapplication.models.Service

@Entity(
    tableName = "doctor_service",
    primaryKeys = ["doctorId", "serviceId"],
    foreignKeys = [
        ForeignKey(
            entity = Doctor::class,
            parentColumns = ["id"],
            childColumns = ["doctorId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Service::class,
            parentColumns = ["id"],
            childColumns = ["serviceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DoctorService(
    val doctorId: Int,
    val serviceId: Int
)