package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.dao.AppointmentDao
import com.example.myapplication.dao.DoctorDao
import com.example.myapplication.dao.PatientDao
import com.example.myapplication.dao.ServiceDao
import com.example.myapplication.models.Appointment
import com.example.myapplication.models.Doctor
import com.example.myapplication.models.Patient
import com.example.myapplication.models.Service
import com.example.myapplication.services.DoctorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Service::class,
        Patient::class,
        Doctor::class,
        Appointment::class,
        DoctorService::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
    abstract fun doctorDao(): DoctorDao
    abstract fun patientDao(): PatientDao
    abstract fun serviceDao(): ServiceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medical_db"
                )
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                initializeDefaultData(database)
            }
        }
        private suspend fun initializeDefaultData(db: AppDatabase) {

            if (db.serviceDao().count() == 0) {
                val defaultServices = listOf(
                    Service(1, "Консультация врача", 500.0),
                    Service(2, "Рентген", 250.0),
                    Service(3, "Анализ крови", 150.0),
                    Service(4, "МРТ", 1200.0),
                    Service(5, "Прививка", 300.0),
                    Service(6, "Стоматология", 800.0),
                    Service(7, "УЗИ", 400.0),
                    Service(8, "Физиотерапия", 350.0),
                    Service(9, "ЭКГ", 200.0),
                    Service(10, "Хирургическая операция", 5000.0)
                )
                db.serviceDao().insertAll(defaultServices)
            }
        }

    }
}