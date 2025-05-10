package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.models.Role
import kotlinx.coroutines.launch

class DoctorCardActivity : AppCompatActivity() {
    private lateinit var db : AppDatabase
    private var doctorId: Int = -1
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

        if(doctorId == -1){
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
            val reason = EditText(this).apply {
                hint = "Введите причину"
            }

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Причина")
                .setView(reason)
                .setPositiveButton("OK") { dialog, _ ->
                    
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
        }
    }

    private fun loadDoctorData(){
        lifecycleScope.launch {
            try {
                val doctorWithServices = db.doctorDao().getDoctorWithServices(doctorId)

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