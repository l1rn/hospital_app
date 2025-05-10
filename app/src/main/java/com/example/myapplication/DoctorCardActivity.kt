package com.example.myapplication

import SessionManager
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.models.Appointment
import com.example.myapplication.models.Role
import com.example.myapplication.services.DoctorWithServices
import kotlinx.coroutines.launch

class DoctorCardActivity : AppCompatActivity() {
    private lateinit var db : AppDatabase
    private var doctorId: Int = -1
    private var patientId: Int = -1
    private lateinit var doctorWithServices: DoctorWithServices
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctor_card)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = AppDatabase.getDatabase(applicationContext)

        doctorId = intent.getIntExtra("DOCTOR_ID", -1)
        patientId = SessionManager.getPatientId(this)

        if(doctorId == -1){
            finish()
            return
        }

        if(patientId == -1){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        loadDoctorData()
        backButton()
        confirmButton()
    }
    private fun backButton(){
        findViewById<Button>(R.id.back_button_from_doctor_card).setOnClickListener{
            finish()
        }
    }

    private fun confirmButton(){
        findViewById<Button>(R.id.confirm_appointment).setOnClickListener {
            val serviceNames = doctorWithServices.services.map { it.name }.toTypedArray()
            val selectedServices = BooleanArray(serviceNames.size)

            val container = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 16, 32, 16)
            }

            val reasonEditText = EditText(this).apply {
                hint = "Введите причину"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            val dateEditText = EditText(this).apply {
                hint = "Введите дату"
                isFocusable = false
                setOnClickListener{
                    showDatePickerDialog(this)
                }
            }

            container.addView(dateEditText)
            container.addView(reasonEditText)

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Причина / Процедура")
                .setView(container)
                .setMultiChoiceItems(serviceNames, selectedServices) {_, which, isChecked ->
                    selectedServices[which] = isChecked
                }
                .setPositiveButton("Подтвердить") { dialog, _ ->
                    val selectedServiceIds = selectedServices
                        .mapIndexed { index, selected -> if (selected) index else -1 }
                        .filter{ it != -1 }
                        .map { doctorWithServices.services[it].id }
                    if (selectedServiceIds.isEmpty())
                    {
                        Toast.makeText(this, "Выберите хотя бы одну услугу", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val reason = reasonEditText.text.toString()
                    val date = dateEditText.text.toString()

                    selectedServiceIds.forEach { serviceId ->
                        val appointment = Appointment(
                            patientId = patientId,
                            doctorId = doctorWithServices.doctor.id,
                            serviceId = serviceId,
                            dateTime = date,
                            reason = reason
                        )
                        lifecycleScope.launch {
                            db.appointmentDao().insert(appointment)
                        }
                    }

                    Toast.makeText(this, "Запись была успешно создана", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
        }
    }

    private fun showDatePickerDialog(editText: EditText){
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            {
                _, year, month, day ->
                val selectedDate = "$day.${month+1}.$year"
                editText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun loadDoctorData(){
        lifecycleScope.launch {
            try {
                doctorWithServices = db.doctorDao().getDoctorWithServices(doctorId)

                with(doctorWithServices){
                    findViewById<ImageView>(R.id.cardDoctorPhoto).setImageResource(doctor.photoUri)
                    findViewById<TextView>(R.id.cardDoctorName).text = doctor.name
                    findViewById<TextView>(R.id.cardDoctorExp).text = "Стаж: ${doctor.experience} лет"

                    val doctorRole: String = if(doctor.role == Role.DOCTOR){
                        "Доктор"
                    } else{
                        "Нету"
                    }
                    findViewById<TextView>(R.id.cardDoctorRole).text = "Должность: ${doctorRole}"

                    val servicesText = services.joinToString("\n") { "• ${it.name} (${it.price}₽)"}
                    findViewById<TextView>(R.id.cardDoctorServices).text = servicesText

                }
            }
            catch (e: Exception){
                e.printStackTrace()
                finish()
            }
        }
    }
}