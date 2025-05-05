package com.example.myapplication.services

import androidx.room.Embedded
import androidx.room.Relation
import com.example.myapplication.models.Appointment
import com.example.myapplication.models.Doctor
import com.example.myapplication.models.Patient
import com.example.myapplication.models.Service

data class AppointmentFullInfo(
    @Embedded val appointment: Appointment,
    @Relation(parentColumn = "patientId", entityColumn = "id")
    val patient: Patient,
    @Relation(parentColumn = "doctorId", entityColumn = "id")
    val doctor: Doctor,
    @Relation(parentColumn = "serviceId", entityColumn = "id")
    val service: Service
)