package com.example.myapplication

import android.content.Context
import android.content.Intent
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
import com.example.myapplication.services.DoctorService
import com.example.myapplication.services.DoctorWithServices
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
            val allDoctorsServices = doctorDao.getAllDoctorsWithServices()
            adapter.submitList(allDoctorsServices)
        }
    }

    inner class DoctorsAdapter(context: Context) : BaseAdapter(){
        private val inflater = LayoutInflater.from(context)
        private var doctorServices = emptyList<DoctorWithServices>()
        fun submitList(newList: List<DoctorWithServices>){
            doctorServices = newList
            notifyDataSetChanged()
        }
        override fun getCount(): Int = doctorServices.size

        override fun getItem(p0: Int): DoctorWithServices = doctorServices[p0]

        override fun getItemId(p0: Int): Long = doctorServices[p0].doctor.id.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
            val view = convertView ?: inflater.inflate(R.layout.doctor_preview, parent, false)
            val item = getItem(position)

            view.findViewById<ImageView>(R.id.doctor_photo).setImageResource(item.doctor.photoUri)
            view.findViewById<TextView>(R.id.doctor_name).text = item.doctor.name
            val servicesText = item.services.joinToString(", ") { it.name }
            view.findViewById<TextView>(R.id.doctor_sevices).text = servicesText
            val intent = Intent(applicationContext, DoctorCardActivity::class.java).apply {
                putExtra("DOCTOR_ID", item.doctor.id)
            }
            view.findViewById<Button>(R.id.more_about_button).setOnClickListener {
                startActivity(intent)
            }
            return view
        }
    }
}