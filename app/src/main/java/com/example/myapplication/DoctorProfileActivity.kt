package com.example.myapplication

import SessionManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.services.AppointmentFullInfo
import kotlinx.coroutines.launch

class DoctorProfileActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var adapter: AppointmentAdapter
    private var doctorId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctor_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listView = findViewById<ListView>(R.id.appointmentsListView)

        doctorId = SessionManager.getDoctorId(this)
        findViewById<Button>(R.id.back_button_from_doctor_profile).setOnClickListener {
            finish()
        }
        if(doctorId == -1){
            finish()
            return
        }
        adapter = AppointmentAdapter(this)
        db = AppDatabase.getDatabase(this)
        loadAppointments()
        listView.adapter = adapter
    }

    private fun loadAppointments(){
        lifecycleScope.launch {
            val appointments = db.appointmentDao().getAppointmentsByDoctorId(doctorId)
            adapter.submitList(appointments)
        }
    }

    inner class AppointmentAdapter(context: Context): BaseAdapter(){
        private val inflater = LayoutInflater.from(context)
        private var appointments = emptyList<AppointmentFullInfo>()
        fun submitList(newList: List<AppointmentFullInfo>){
            appointments = newList
            notifyDataSetChanged()
        }

        override fun getCount(): Int = appointments.size

        override fun getItem(p0: Int): AppointmentFullInfo = appointments[p0]

        override fun getItemId(p0: Int): Long = appointments[p0].appointment.id.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
            val view = convertView ?: inflater.inflate(R.layout.patient_item, parent, false)
            val item = getItem(position)

            view.findViewById<TextView>(R.id.pFullName).text = item.patient.fullName
            view.findViewById<TextView>(R.id.pBirthDate).text = item.patient.birthDate
            view.findViewById<TextView>(R.id.pEmail).text = item.patient.email
            view.findViewById<TextView>(R.id.pPhone).text = item.patient.phone

            return view
        }

    }
}