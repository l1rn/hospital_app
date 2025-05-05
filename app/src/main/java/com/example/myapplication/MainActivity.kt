package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.enter_to_app_button).setOnClickListener {
            startActivity(Intent(this, LobbyActivity::class.java))
        }
        findViewById<Button>(R.id.database_button).setOnClickListener{
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val services = db.serviceDao()
            Log.d("DB init", "Total services: $services")
            db.serviceDao().count()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}