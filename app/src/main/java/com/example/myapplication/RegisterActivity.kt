package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.models.Patient
import com.example.myapplication.models.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var birthday: EditText
    private lateinit var phone: EditText
    private lateinit var reason: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        val db = AppDatabase.getDatabase(applicationContext)
        val patients = db.patientDao()

        name = findViewById(R.id.register_name)
        email = findViewById(R.id.register_email)
        birthday = findViewById(R.id.birhday_register)
        phone = findViewById(R.id.register_phone)

        findViewById<Button>(R.id.register_account_button).setOnClickListener {
            if(name.text.isEmpty() || email.text.isEmpty() || birthday.text.isEmpty() || phone.text.isEmpty()){
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val patient = Patient(
                        fullName = name.text.toString(),
                        birthDate = birthday.text.toString(),
                        email = email.text.toString(),
                        role = Role.PATIENT,
                        phone = phone.text.toString()
                    )
                    patients.insert(patient)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Пациент зарегистрирован", Toast.LENGTH_SHORT).show()
                    }
                    name.setText("")
                    email.setText("")
                    phone.setText("")
                    birthday.setText("")
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}