package com.example.myapplication.services

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.myapplication.models.Doctor
import com.example.myapplication.models.Service

data class DoctorWithServices(
    @Embedded val doctor: Doctor,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value =  DoctorService::class,
            parentColumn = "doctorId",
            entityColumn = "serviceId"
        )
    )
    val services: List<Service>
)