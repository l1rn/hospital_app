package com.example.myapplication.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String,
    val birthDate: String,
    val role: Role = Role.PATIENT,
    val email: String,
    val phone: String,
)