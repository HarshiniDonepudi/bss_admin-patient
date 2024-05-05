package com.example.lion_nav_barhomepage.patientdashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import coil.load
import com.example.lion_nav_barhomepage.IntroActivity
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.FragmentPatientProfileBinding
import com.example.lion_nav_barhomepage.patient_main_data
import com.example.lion_nav_barhomepage.patientdashboard.appointment.PatientAppointmentsFragment
import com.example.lion_nav_barhomepage.patientdashboard.diagnosis.DiagnosisFragment
import com.example.lion_nav_barhomepage.patientdashboard.reports.ReportsFragment
import com.example.lion_nav_barhomepage.patientdashboard.vaccines.VaccinesFragment
import com.example.lion_nav_barhomepage.sharedPreferences

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PatientProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PatientProfileFragment : Fragment() {
    private var _binding: FragmentPatientProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPatientProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Patient Profile"

        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            app.setOnClickListener { replaceFragment(PatientAppointmentsFragment()) }
            vaccine.setOnClickListener { replaceFragment(VaccinesFragment()) }
            results.setOnClickListener { replaceFragment(ReportsFragment()) }
            diagnosis.setOnClickListener { replaceFragment(DiagnosisFragment()) }
            editButton.setOnClickListener { replaceFragment(EditProfileFragment()) }
            logoutButton.setOnClickListener { logout() }
            deactivateButton.setOnClickListener { deactivateAccount() }

            pname.text = patient_main_data.name
            pemail.text = patient_main_data.email
            pid.text = patient_main_data.id
            val img_url = patient_main_data.img_url
            if (img_url.isNullOrEmpty()) {
                pimg.setImageResource(R.drawable.user_icon)
            } else {
                pimg.load(img_url.toUri()) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }
        }
    }

    private fun deactivateAccount() {
        val userId = patient_main_data.email
        if (userId.isNullOrEmpty()) {
            Toast.makeText(context, "Invalid user ID", Toast.LENGTH_LONG).show()
            return
        }

        val db = Firebase.firestore
        db.collection("USERS").document(userId).update("isActive", false)
            .addOnSuccessListener {
                Toast.makeText(context, "Account deactivated", Toast.LENGTH_SHORT).show()
                logout()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to deactivate account: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun logout() {
        sharedPreferences = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("logged", false).apply()
        Toast.makeText(context, "Logging out from account", Toast.LENGTH_SHORT).show()
        startActivity(Intent(context, IntroActivity::class.java))
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.framelayout, fragment)
            .commit()
    }
}
