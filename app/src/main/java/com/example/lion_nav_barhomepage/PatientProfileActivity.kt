package com.example.lion_nav_barhomepage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lion_nav_barhomepage.databinding.ActivityPatientProfileBinding


class PatientProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.app.setOnClickListener {
//       val intent = Intent(this, AppointmentActivity::class.java)
//           startActivity(intent)
        }
        binding.vaccine.setOnClickListener{
//            val intent = Intent(this, VaccineActivity::class.java)
//            startActivity(intent)
        }
        binding.results.setOnClickListener{
//            val intent = Intent(this, ReportsActivity::class.java)
//            startActivity(intent)
        }

        binding.diagnosis.setOnClickListener{
//            val intent = Intent(this, DiagnosisActivity::class.java)
//            startActivity(intent)
        }


    }
    override fun onBackPressed() {
        val intent= Intent(this, DandPActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}