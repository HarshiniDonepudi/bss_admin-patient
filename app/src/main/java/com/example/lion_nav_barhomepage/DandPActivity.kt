package com.example.lion_nav_barhomepage

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.lion_nav_barhomepage.AdminData
import com.example.lion_nav_barhomepage.IntroActivity
import com.example.lion_nav_barhomepage.LoginActivity
import com.example.lion_nav_barhomepage.PatientProfileActivity
import com.example.lion_nav_barhomepage.R


class DandPActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dand_pactivity)
        val doctors: Button = findViewById(R.id.doctors)
        val patients: Button = findViewById(R.id.patients)

        doctors.setOnClickListener {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }
        patients.setOnClickListener {
            val intent = Intent(this, PatientProfileActivity::class.java)
            startActivity(intent)
        }





    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)

        sharedPreferences=this.getSharedPreferences("login",
            AppCompatActivity.MODE_PRIVATE
        )

        val admin_info = AdminData()
        val loggedemail = sharedPreferences
            .getString("logged_email", admin_info.email)
        Log.e("logged","${loggedemail}")

        if(loggedemail=="lionscancerhospital@gmail.com")
            menu?.findItem(R.id.adduser)?.setEnabled(true)
        else
            menu?.findItem(R.id.adduser)?.setVisible(false)
        return super.onCreateOptionsMenu(menu)


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.adduser -> {
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                return true

            }
            R.id.logout->{

                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Do you want to logout?")
                    .setCancelable(true)
                dialogBuilder.setPositiveButton("Cancel"){
                        dialog,id->
                }
                dialogBuilder.setNegativeButton("logout"){
                        dialog,id->
                    sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean("logged",false).apply()
                    Toast.makeText(
                        applicationContext,
                        "logging out from account",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

                val alert = dialogBuilder.create()
                alert.show()
                return true
            }
            else -> {super.onOptionsItemSelected(item)}
        }
    }
}