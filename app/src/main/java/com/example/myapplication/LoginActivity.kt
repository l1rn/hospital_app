package com.example.myapplication

import SessionManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var phone: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        db = AppDatabase.getDatabase(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        phone = findViewById(R.id.phone_nubmer)
        findViewById<Button>(R.id.login_button).setOnClickListener{
            checkUserRole(phone.text.toString(), applicationContext)
        }
    }
    private fun checkUserRole(phone:String, context: Context){
        lifecycleScope.launch {
            val isDoctor = db.doctorDao().isDoctorExists(phone)
            val isPatient = db.patientDao().isPatientExists(phone)
            if(isPatient){
                SessionManager.savePatientId(context, db.patientDao().getIdByPhone(phone))
            }
            if(isDoctor){
                SessionManager.saveDoctorId(context, db.doctorDao().getIdByPhone(phone))
            }
            when {
                isDoctor -> startActivity(Intent(this@LoginActivity, DoctorProfileActivity::class.java))
                isPatient -> startActivity(Intent(this@LoginActivity, AllDoctorsActivity::class.java))

                else -> {
                    startActivity(Intent(this@LoginActivity, LobbyActivity::class.java))
                    Toast.makeText(applicationContext, "Такого номера нету", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}