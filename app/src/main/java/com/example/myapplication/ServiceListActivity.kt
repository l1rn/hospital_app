package com.example.myapplication

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
import com.example.myapplication.dao.ServiceDao
import com.example.myapplication.models.Service
import kotlinx.coroutines.launch

class ServiceListActivity : AppCompatActivity() {
    private lateinit var serviceDao: ServiceDao
    private lateinit var adapter: ServiceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_service_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = AppDatabase.getDatabase(applicationContext)
        serviceDao = db.serviceDao()

        val listView = findViewById<ListView>(R.id.service_items)

        adapter = ServiceAdapter(this)
        listView.adapter = adapter

        loadServices()

        findViewById<Button>(R.id.back_button_from_services).setOnClickListener {
            finish()
        }
    }
    private fun loadServices(){
        lifecycleScope.launch {
            val services = serviceDao.getAllServices()
            adapter.submitList(services)
        }
    }
    inner class ServiceAdapter(context: Context) : BaseAdapter() {
        private val inflater = LayoutInflater.from(context)
        private var services = emptyList<Service>()

        fun submitList(newList: List<Service>){
            services = newList
            notifyDataSetChanged()
        }

        override fun getCount(): Int = services.size

        override fun getItem(position: Int): Service = services[position]

        override fun getItemId(position: Int): Long = services[position].id.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.service_item, parent, false)
            val service = getItem(position)

            view.findViewById<TextView>(R.id.tvServiceName).text = service.name
            view.findViewById<TextView>(R.id.tvServicePrice).text = "Цена: ${service.price} руб."

            return view
        }

    }
}