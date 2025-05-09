package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.dao.DoctorDao
import com.example.myapplication.models.Doctor
import com.example.myapplication.models.Service
import kotlinx.coroutines.launch

class AllDoctorsActivity : AppCompatActivity() {
    private lateinit var adapter: DoctorsAdapter
    private lateinit var doctorDao: DoctorDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_doctors)
        findViewById<Button>(R.id.back_button_from_doctors).setOnClickListener {
            finish()
        }

        val db = AppDatabase.getDatabase(applicationContext)
        doctorDao = db.doctorDao()

        val listView = findViewById<ListView>(R.id.all_doctors)

        adapter = DoctorsAdapter(this)
        listView.adapter = adapter

        loadServices()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun loadServices(){
        lifecycleScope.launch {
            val doctors = doctorDao.getAllDoctors()
            adapter.submitList(doctors)
        }
    }

    inner class DoctorsAdapter(context: Context) : BaseAdapter(){
        private val inflater = LayoutInflater.from(context)
        private var doctors = emptyList<Doctor>()

        fun submitList(newList: List<Doctor>){
            doctors = newList
            notifyDataSetChanged()
        }

        override fun getCount(): Int = doctors.size

        override fun getItem(p0: Int): Doctor = doctors[p0]

        override fun getItemId(p0: Int): Long = doctors[p0].id.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
            val view = convertView ?: inflater.inflate(R.layout.doctor_preview, parent, false)
            val doctor = getItem(position)

            view.findViewById<ImageView>(R.id.doctor_photo).setImageResource(doctor.photoUri)
            view.findViewById<TextView>(R.id.doctor_name).text = doctor.name

            return view
        }

    }
}