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
import com.example.myapplication.models.Role
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
    version = 1
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
            if(db.doctorDao().countDoctors() == 0){
                val defaultDoctors = listOf(
                    Doctor(id = 1, name = "Попов Владимир Петрович", email = "popov@mail.ru", phone = "7999999111", role = Role.DOCTOR, photoUri =  R.drawable.doctor1, experience = 10),
                    Doctor(
                        id = 2,
                        name = "Иванова Мария Сергеевна",
                        email = "ivanova@mail.ru",
                        phone = "7999888222",
                        role = Role.DOCTOR,
                        photoUri = R.drawable.doctor2,
                        experience = 8
                    ),
                    Doctor(
                        id = 3,
                        name = "Сидоров Алексей Дмитриевич",
                        email = "sidorov@mail.ru",
                        phone = "7999777333",
                        role = Role.DOCTOR,
                        photoUri = R.drawable.doctor3,
                        experience = 15
                    ),
                    Doctor(
                        id = 4,
                        name = "Кузнецова Елена Викторовна",
                        email = "kuznetsova@mail.ru",
                        phone = "7999666444",
                        role = Role.DOCTOR,
                        photoUri = R.drawable.doctor4,
                        experience = 5
                    ),
                    Doctor(
                        id = 5,
                        name = "Петров Денис Олегович",
                        email = "petrov@mail.ru",
                        phone = "7999555555",
                        role = Role.DOCTOR,
                        photoUri = R.drawable.doctor6,
                        experience = 12
                    )
                )
                db.doctorDao().insertAll(defaultDoctors)
                val selectedServiceIds1 = listOf(1, 2, 3)
                selectedServiceIds1.forEach { serviceId ->
                    db.doctorDao().insertDoctorService(DoctorService(defaultDoctors[1].id, serviceId))
                }
                val selectedServiceIds2 = listOf(2, 3, 4)
                selectedServiceIds2.forEach {serviceId ->
                    db.doctorDao().insertDoctorService(DoctorService(defaultDoctors[2].id, serviceId))
                }
                val selectedServiceIds3 = listOf(5, 6, 7)
                selectedServiceIds3.forEach {
                    serviceId -> db.doctorDao().insertDoctorService(DoctorService(defaultDoctors[3].id, serviceId))
                }
                val selectedServiceIds4 = listOf(8, 9, 10)
                selectedServiceIds4.forEach {
                    serviceId -> db.doctorDao().insertDoctorService(DoctorService(defaultDoctors[4].id, serviceId))
                }
                val selectedServiceIds5 = listOf(2, 5, 8)
                selectedServiceIds5.forEach {
                    serviceId -> db.doctorDao().insertDoctorService(DoctorService(defaultDoctors[5].id, serviceId))
                }
            }
            if(db.patientDao().count() == 0){
                val defaultPatients = listOf(
                    Patient(id = 1, fullName = "Абдула Гангрена Романович", birthDate = "11.11.2000", email = "abdula@gmail.com", phone = "79998887771", role = Role.PATIENT)
                )
                db.patientDao().insertAll(defaultPatients)
            }
        }

    }
}