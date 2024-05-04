package com.example.lion_nav_barhomepage

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lion_nav_barhomepage.databinding.FragmentLoginTabBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminTabFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var db_admin = FirebaseFirestore.getInstance()
    private var _binding: FragmentLoginTabBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        sharedPreferences = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        checkInitialLogin()

        binding.signup.setOnClickListener {
            if (validateInput()) {
                performLogin()
            } else {
                Toast.makeText(context, "Enter the Details", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkInitialLogin() {
        val checkLogin = sharedPreferences.getBoolean("logged", false)
        if (checkLogin) {
            navigateToDashboard()
        }
    }

    private fun performLogin() {
        val email = binding.email.text.toString()
        val password = binding.signPass.text.toString()
        showProgressDialog()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                fetchAdminData(email)
            } else {
                Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchAdminData(email: String) {
        val docRef = db_admin.collection("ADMINS").document(email)
        docRef.get().addOnSuccessListener { document ->
            val adminInfo = AdminData(
                document.get("id").toString(),
                document.get("name").toString(),
                document.get("email").toString()
            )
            saveAdminData(adminInfo)
            navigateToDashboard()
        }.addOnFailureListener {
            Toast.makeText(context, "Unable to fetch data", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveAdminData(adminInfo: AdminData) {
        sharedPreferences.edit().apply {
            putBoolean("logged", true)
            putString("logged_email", adminInfo.email)
            putString("logged_id", adminInfo.id)
            putString("logged_name", adminInfo.name)
            apply()
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(context, DandPActivity::class.java)
        startActivity(intent)
    }

    private fun showProgressDialog() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun validateInput(): Boolean = binding.email.text.toString().trim().isNotEmpty() &&
            binding.signPass.text.toString().trim().isNotEmpty()
}
