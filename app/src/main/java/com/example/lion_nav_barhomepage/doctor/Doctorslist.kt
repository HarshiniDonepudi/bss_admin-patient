package com.example.lion_nav_barhomepage.doctor

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.lion_nav_barhomepage.databinding.ActivityDoctorslistBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

var position : Int = -1
  lateinit var doctor : ArrayList<Data>
lateinit var DoctorList : ArrayList<Data>
class Doctorslist : AppCompatActivity(), DoctorAdapter.Click  {
    private lateinit var dbref : DatabaseReference
    var slots : MutableList<MutableList<Int>> = mutableListOf(mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0),mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0))
    var avl: MutableList<Int> = mutableListOf(0)
    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: ActivityDoctorslistBinding
    private lateinit var VIEW: View

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDoctorslistBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        recyclerView = binding.doctorList
        VIEW=view
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        DoctorList = arrayListOf<Data>()
        getUserData()

        binding.add.setOnClickListener{
            dbref = FirebaseDatabase.getInstance().getReference("Doctors")

////            Log.e("data", "$id ,$name , $about , $exp ,$lan")
//            dbref.child(data.id.toString()).setValue(data)
            val intent = Intent(this, AddDoctorActivity::class.java)
            startActivity(intent)
        }
    }

     fun getUserData() {
         val fakedata= Data(1254646,"name", "spec",
             "about",
             "not done yet",
             "exp",
             avl as ArrayList<Int>,
             slots,
             "lan",
             "200"
         )
        dbref = FirebaseDatabase.getInstance().getReference("Doctors")
         if (!isConnected(applicationContext)) {
             Toast.makeText(applicationContext, "No Internet ", Toast.LENGTH_SHORT).show();
             Log.e("network--->","if-block")

         }
        dbref.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.exists()){
                   DoctorList.clear()
                   for (Doctorsnap in snapshot.children){
                       val doctor= Doctorsnap.getValue(Data::class.java)
                       DoctorList.add(doctor!!)
                       senddoctor(DoctorList)
                   }
                    DoctorList.remove(fakedata)
                   recyclerView.adapter= DoctorAdapter(DoctorList, this@Doctorslist,this@Doctorslist)
               }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun senddoctor(doctorList: ArrayList<Data>) {
            doctor =doctorList
    }
//    fun AlertDialog(view: View){
//        val dialogBuilder = AlertDialog.Builder(view.context)
//        dialogBuilder.setMessage("select appropriate data")
//            .setCancelable(true)
//        dialogBuilder.setPositiveButton("Cancel"){
//            dialog,id->
//        }
//        dialogBuilder.setNegativeButton("Delete"){
//            dialog,id-> Toast.makeText(this,"Deleting entry",Toast.LENGTH_SHORT).show()
//        }
//
//        val alert = dialogBuilder.create()
//        alert.show()
//    }

    override fun go_to_main_edit(R_id: Int) {
        position =R_id
        val intent = Intent(this, EditDoctor::class.java)
        startActivity(intent)
    }
    override fun go_to_main_delete(R_id: Int) {
        position =R_id
//        AlertDialog()
        val dialogBuilder = AlertDialog.Builder(VIEW.context)
        dialogBuilder.setMessage("select appropriate data")
            .setCancelable(true)
        dialogBuilder.setPositiveButton("Cancel"){
                dialog,id->
        }
        dialogBuilder.setNegativeButton("Delete"){
                dialog,id->
                val Id= doctor[position].id
            val url = doctor[position].img_url
            val ref = FirebaseStorage.getInstance().getReference("/img/$Id")
            ref.delete().addOnSuccessListener { Log.e("Deleting","---> img Deleted") }
                .addOnFailureListener{Log.e("Deleting","---> img not deleted")}
                dbref.child(Id.toString()).removeValue().addOnSuccessListener {
                    Toast.makeText(this,"Deleting entry",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this,"" +
                            "Faild to delete",Toast.LENGTH_SHORT).show()
                }

        }

        val alert = dialogBuilder.create()
        alert.show()
    }
    fun isConnected(context: Context): Boolean {
        Log.e("network--->","isconnect called")
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }

    }

}
