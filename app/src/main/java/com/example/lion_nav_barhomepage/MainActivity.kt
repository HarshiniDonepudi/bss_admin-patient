package com.example.lion_nav_barhomepage

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import coil.load
import com.example.lion_nav_barhomepage.Appointment.AppointmentFragment
import com.example.lion_nav_barhomepage.Contact.ContactFragment

import com.example.lion_nav_barhomepage.Facilities.FacilitiesFragment
import com.example.lion_nav_barhomepage.Home.HomeFragment

import com.example.lion_nav_barhomepage.Vaccines.BookVaccineFragment
import com.example.lion_nav_barhomepage.Vaccines.NewVaccinesFragment
import com.example.lion_nav_barhomepage.Vaccines.VaccineInfoFragment
import com.example.lion_nav_barhomepage.about.AboutFragment
import com.example.lion_nav_barhomepage.about.AboutUsFragment
import com.example.lion_nav_barhomepage.about.MandVFragment
import com.example.lion_nav_barhomepage.about.OurTeamFragment
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.example.lion_nav_barhomepage.doctors.DoctorsProfileFragment
import com.example.lion_nav_barhomepage.doctors.DoctorsViewModel

import com.example.lion_nav_barhomepage.patientdashboard.*
import com.example.lion_nav_barhomepage.patientdashboard.appointment.PatientAppointmentsFragment
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.DiagnosisFragment
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.PresFragment
import com.example.lion_nav_barhomepage.patientdashboard.reports.AddReportsFragment
import com.example.lion_nav_barhomepage.patientdashboard.reports.ReportsFragment
import com.example.lion_nav_barhomepage.patientdashboard.reports.pdfactivity
import com.example.lion_nav_barhomepage.patientdashboard.vaccines.AddVaccineFragment
import com.example.lion_nav_barhomepage.patientdashboard.vaccines.VaccinesFragment
import com.google.android.material.navigation.NavigationView
var flag=0
class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var username: TextView
    lateinit var useremail: TextView
    lateinit var userimg: ImageView
    lateinit var sharedPreferences: SharedPreferences
    private val viewModel: DoctorsViewModel by viewModels()
    var img_url = patient_main_data.img_url
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sharedPreferences = this.getSharedPreferences(
            "login",
            MODE_PRIVATE
        )

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerLayout = navView.getHeaderView(0)

        val nav_drawer_header = headerLayout.findViewById(R.id.profile) as LinearLayout
        username = headerLayout.findViewById(R.id.user_name) as TextView
        useremail = headerLayout.findViewById(R.id.user_email) as TextView
        userimg = headerLayout.findViewById(R.id.userimg) as ImageView


//----------------
        val checklogin = sharedPreferences.getBoolean("logged", false)
//----------------

        if (checklogin == false) {
            username.setText("Guest User")
            userimg.setImageResource(R.drawable.user_icon)
            useremail.setText("")
            nav_drawer_header.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Please Register")
                    .setCancelable(true)
                dialogBuilder.setPositiveButton("Cancel ") { dialog, id ->
                }
                dialogBuilder.setNegativeButton("Register") { dialog, id ->
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                }

                val alert = dialogBuilder.create()
                alert.show()
            }


        } else {


            if (img_url == "") {
                userimg.setImageResource(R.drawable.user_icon)
            } else {
                userimg.load(img_url?.toUri()) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }

            Log.e("In main viewmodel ::", "${patient_main_data}")
            username.setText(patient_main_data.name.toString())
            useremail.setText(patient_main_data.email.toString())
            nav_drawer_header.setOnClickListener {
                replaceFragment(PatientProfileFragment(), "Patient Profile")
            }

        }


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragment(HomeFragment(), "Home")
        drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                //Log.i(TAG, "onDrawerSlide");
            }

            override fun onDrawerOpened(drawerView: View) {
                Log.e("TAG", "onDrawerOpened")
                if (img_url != patient_main_data.img_url) {
                    datachanged()
                    Log.e("TAG", "changed")
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                Log.e("TAG", "onDrawerClosed")
            }

            override fun onDrawerStateChanged(newState: Int) {
                //Log.i(TAG, "onDrawerStateChanged");
            }
        })

        navView.setNavigationItemSelectedListener {
            Log.e("open", "drawer")
            setcheckbutton(it)
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment(), it.title.toString())
                R.id.nav_Doctors -> replaceFragment(DoctorsFragment(), it.title.toString())
                R.id.nav_appointments -> replaceFragment(AppointmentFragment(), it.title.toString())
                R.id.nav_vaccines -> replaceFragment(NewVaccinesFragment(), it.title.toString())
                R.id.nav_facilities -> replaceFragment(FacilitiesFragment(), it.title.toString())
                R.id.nav_Contact -> replaceFragment(ContactFragment(), it.title.toString())


                R.id.nav_logout -> logout()

                R.id.nav_about -> replaceFragment(AboutFragment(), it.title.toString())

            }
            true
        }
    }

    fun datachanged() {

        img_url = patient_main_data.img_url
        if (img_url == "") {
            userimg.setImageResource(R.drawable.user_icon)
        } else {
            userimg.load(img_url?.toUri()) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
        }

        Log.e("In main viewmodel ::", "${patient_main_data}")
        username.setText(patient_main_data.name.toString())
        useremail.setText(patient_main_data.email.toString())

    }


    private fun logout() {

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("logged", false).apply()
        Toast.makeText(
            applicationContext,
            "logging out from account",
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
    }

    private fun setcheckbutton(menuItem: MenuItem) {
        menuItem.isChecked = false
        val id = supportFragmentManager.findFragmentById(R.id.framelayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        when (id) {
//            is PatientProfileFragment -> menuItem.isChecked=false
            is HomeFragment -> navView.setCheckedItem(R.id.nav_home)
            is DoctorsFragment -> navView.setCheckedItem(R.id.nav_Doctors)
            is DoctorsProfileFragment -> navView.setCheckedItem(R.id.nav_Doctors)
            is FacilitiesFragment -> navView.setCheckedItem(R.id.nav_facilities)
            is AboutFragment -> navView.setCheckedItem(R.id.nav_about)
            is ContactFragment -> navView.setCheckedItem(R.id.nav_Contact)
            is AppointmentFragment -> navView.setCheckedItem(R.id.nav_appointments)

            else -> menuItem.isChecked = false
        }

    }

    fun replaceFragment(fragment: Fragment, title: String) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        //fragmentTransaction.addToBackStack("home")
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
//        setTitle(title)
//
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return true
    }

    override fun onBackPressed() {
        val id = supportFragmentManager.findFragmentById(R.id.framelayout)
        if (id is PatientAppointmentsFragment ||
            id is VaccinesFragment ||
            id is ReportsFragment ||
            id is DiagnosisFragment ||
            id is MedicinesFragment ||
            id is EditProfileFragment
        ) {
            replaceFragment(PatientProfileFragment(), "Patient Profile")
        }else if (id is AboutUsFragment ||
                id is OurTeamFragment ||
                id is MandVFragment
            ) {
                replaceFragment(AboutFragment(), "About Us")

            } else if (id is DoctorsProfileFragment) {
                viewModel.setposition(-1)
                replaceFragment(DoctorsFragment(), "Doctors")

            } else if (id is PresFragment) {

                replaceFragment(DiagnosisFragment(), "Diagnosis")

            } else if (id is pdfactivity) {
                Log.e("back", "${pdfactivity().getflag()}")
                if (pdfactivity().getflag() == 1) {
                    replaceFragment(ReportsFragment(), "Reports")
                } else if (PdfDialogFragment().getflag() == 2) {
                    replaceFragment(VaccinesFragment(), "Vaccines")
                }

            } else if (id is AddVaccineFragment) {
                replaceFragment(VaccinesFragment(), "Vaccines")
            } else if (id is AddReportsFragment) {
                replaceFragment(ReportsFragment(), "Reports")

        }
        else if (id is BookVaccineFragment) {
            replaceFragment(VaccineInfoFragment(), "Vaccine")
        }
        else if (id is VaccineInfoFragment) {
            replaceFragment(NewVaccinesFragment(), "Vaccine")
        }
        else if (id !is HomeFragment) {
                replaceFragment(HomeFragment(), "Home")
            }
        else if ( id is HomeFragment){
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }
        else {
                super.onBackPressed()
            }

//        override fun onBackPressed() {
//            val count = supportFragmentManager.backStackEntryCount
//            if (count == 0) {
//                super.onBackPressed()
//                //additional code
//            } else {
//                supportFragmentManager.popBackStack()
//            }
        }

    }






