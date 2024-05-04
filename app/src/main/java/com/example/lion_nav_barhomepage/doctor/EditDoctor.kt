package com.example.lion_nav_barhomepage.doctor

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import coil.load

import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.ActivityEditDoctorBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class EditDoctor : AppCompatActivity() {
    var check=0
    private lateinit var binding: ActivityEditDoctorBinding
    var slots : MutableList<MutableList<Int>> = mutableListOf(mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0),mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0))
    var avl: MutableList<Int> = mutableListOf(0)
    var img_url = ""
    private lateinit var dbref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditDoctorBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        val doctor_selected : Data = doctor[position]
        doctor.removeAt(position)
        val img_view = binding.img
        img_url=doctor_selected.img_url.toString()
        img_view.load(doctor_selected.img_url?.toUri()){
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        binding.editText1.setText(doctor_selected.name.toString())
        binding.editText2.setText(doctor_selected.id.toString())
        binding.editText3.setText(doctor_selected.spec.toString())
        binding.editText4.setText(doctor_selected.about.toString())
        binding.editText5.setText(doctor_selected.exp.toString())
        binding.editText6.setText(doctor_selected.lang.toString())
        binding.editText7.setText(doctor_selected.fee.toString())
        slots= doctor_selected.timeslots as MutableList<MutableList<Int>>
        avl= doctor_selected.avl as MutableList<Int>

        dbref = FirebaseDatabase.getInstance().getReference("Doctors")
        setavl()
        binding.sun.setOnClickListener{
            setslots(0)
            binding.day.setText(getString(R.string.Sun))
        }
        binding.mon.setOnClickListener{
            setslots(1)
            binding.day.setText(getString(R.string.Mon))

        }
        binding.tue.setOnClickListener{
            setslots(2)
            binding.day.setText(getString(R.string.Tue))

        }
        binding.wed.setOnClickListener{
            setslots(3)
            binding.day.setText(getString(R.string.Wed))

        }
        binding.thur.setOnClickListener{
            setslots(4)
            binding.day.setText(getString(R.string.Thur))

        }
        binding.fri.setOnClickListener{
            setslots(5)
            binding.day.setText(getString(R.string.Fri))

        }
        binding.sat.setOnClickListener{
            setslots(6)
            binding.day.setText(getString(R.string.Sat))

        }
        //------------------------------------
        binding.img.setOnClickListener{
            binding.img.setImageDrawable(null)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
            check=1
        }

        binding.button.setOnClickListener {

            val name = binding.editText1.text.toString()
            val id = binding.editText2.text.toString()
            val spec = binding.editText3.text.toString()
            val about = binding.editText4.text.toString()
//        val img_url = binding.editText5.editableText
            val exp = binding.editText5.text.toString()
            val lan= binding.editText6.text.toString()
            val fee= binding.editText7.text.toString()
            var flag : Int = 0
            if(name.isEmpty()){
                flag=2
                binding.editText1.error="Please enter name!"
                binding.editText1.requestFocus()
            }
            if(id.isEmpty()){
                flag=2
                binding.editText2.error="Please enter id!"
                binding.editText2.requestFocus()
            }
            if(spec.isEmpty()){
                flag=2
                binding.editText3.error="Please enter Spec!"
                binding.editText3.requestFocus()
            }
            if(about.isEmpty()){
                flag=2
                binding.editText4.error="Please enter about!"
                binding.editText4.requestFocus()
            }
            if(exp.isEmpty()){
                flag=2
                binding.editText5.error="Please enter exp!"
                binding.editText5.requestFocus()
            }
            if(lan.isEmpty()){
                flag=2
                binding.editText6.error="Please enter Language!"
                binding.editText6.requestFocus()
            }
            for (i in doctor){
                if(i.id.toString()==id.toString()){
                    flag=1
                    break
                }
            }
            if(flag==0){
//                uploadimg(id)
                if (check==1) {
                    if (selectedPhotoUri != null) {
                        upload()

                        val ref = FirebaseStorage.getInstance().getReference("/img/$id")
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                        val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

                        ref.putBytes(reducedImage)
//                    ref.putFile(selectedPhotoUri!!)
                            .addOnSuccessListener {

                                Log.d("Uploading", "sucessfully:${it.metadata?.path}")

                                ref.downloadUrl.addOnSuccessListener {
                                    img_url = it.toString()

                                    Log.e("img-inside", "url====>$avl")
                                    val data = Data(
                                        id.toInt(),
                                        name,
                                        spec,
                                        about,
                                        img_url,
                                        exp,
                                        avl as ArrayList<Int>,
                                        slots,
                                        lan,
                                        fee
                                    )
                                    Log.e("data", "$id ,$name , $about , $exp ,$lan")
                                    setavl()
                                    dbref.child(data.id.toString()).setValue(data)

                                    val toast = Toast.makeText(
                                        this,
                                        " Sucessfully added",
                                        Toast.LENGTH_SHORT
                                    )
                                    toast.show()
                                    val intent = Intent(this, Doctorslist::class.java)
                                    startActivity(intent)

                                }

                            }.addOnFailureListener {
                                val toast = Toast.makeText(this, "failed", Toast.LENGTH_SHORT)
                                toast.show()
                                Log.d("Uploading", "Failed ")
                            }
                    }
                }
                else if(check==0){
                    img_url = doctor_selected.img_url.toString()

                    Log.e("img-inside", "url====>$avl")
                    val data = Data(
                        id.toInt(),
                        name,
                        spec,
                        about,
                        img_url,
                        exp,
                        avl as ArrayList<Int>,
                        slots,
                        lan,
                        fee
                    )
                    Log.e("data", "$id ,$name , $about , $exp ,$lan")
                    setavl()
                    dbref.child(data.id.toString()).setValue(data)

                    val toast = Toast.makeText(
                        this,
                        " Sucessfully added",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    val intent = Intent(this, Doctorslist::class.java)
                    startActivity(intent)

                }
                Log.e("out","url====>$img_url")
//                val data = Data(
//                            id.toInt(),
//                            name,
//                            spec,
//                            about,
//                            img_url,
//                            exp,
//                            avl as ArrayList<Int>,
//                            slots,
//                            lan
//                        )
//                        Log.e("data", "$id ,$name , $about , $exp ,$lan")
//                        setavl()
//                        dbref.child(data.id.toString()).setValue(data)
//                val toast = Toast.makeText(this," Sucessfully added",Toast.LENGTH_SHORT)
//                toast.show()
//                val intent = Intent(this, Doctorslist::class.java)
//                startActivity(intent)

            }
            else if(flag==1){
                binding.editText2.error="ID Already Exists!"
                binding.editText2.requestFocus()

            }



        }

    }

    private fun upload() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading....")
        progressDialog.setCancelable(false)
        progressDialog.show()

    }


    override fun onBackPressed() {
        val intent = Intent(this, Doctorslist::class.java)
        startActivity(intent)
        super.onBackPressed()
    }



    var selectedPhotoUri : Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            selectedPhotoUri  =data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            val bitmapDrawable= BitmapDrawable(bitmap)
            binding.img.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private  fun setavl()
    {
        for ( i in 0..6){
            var selectedSlot = slots[i].toMutableList()
            if( selectedSlot[0]==1 || selectedSlot[1]==1 || selectedSlot[2]==1 )
            {
                if(avl.indexOf(i+1)==-1){
                    avl.add(i+1)
                }
            }
            if ( selectedSlot[0]==0 && selectedSlot[1]==0 && selectedSlot[2]==0 )
            {
                avl.remove(i+1)
            }

        }
        avl.sort()
        var text = ""
        for (i in avl){
            when(i){
                1->text+="Sun"
                2->text+=" Mon"
                3-> text+=" Tue"
                4->text+=" Wed"
                5->text+=" Thur"
                6->text+=" Fri"
                7->text+=" Sat"
            }

        }
        binding.days.text=text
        Log.e("AVL------>","avl=$avl")
    }
    private fun setslots(i: Int) {
        setavl()
        binding.slot1.isChecked=false
        binding.slot2.isChecked=false
        binding.slot3.isChecked=false
        //------------------------
        var selectedSlot = slots[i].toMutableList()
        if (selectedSlot[0]==1){
            binding.slot1.isChecked=true
        }
        if (selectedSlot[1]==1){
            binding.slot2.isChecked=true
        }
        if (selectedSlot[2]==1){
            binding.slot3.isChecked=true
        }
        binding.slot1.setOnClickListener {
            if (binding.slot1.isChecked()) {
                selectedSlot[0] = 1
                Log.e("Selectedslots:::>", "1True$selectedSlot")
            }
            else{
                selectedSlot[0] = 0
                Log.e("Selectedslots:::>", "1True$selectedSlot")
            }
            setavl()
        }
        binding.slot2.setOnClickListener {
            if (binding.slot2.isChecked()) {
                selectedSlot[1] = 1
                Log.e("Selectedslots:::>", "1True$selectedSlot")
            }
            else{
                selectedSlot[1] = 0
                Log.e("Selectedslots:::>", "1True$selectedSlot")
            }
            setavl()
        }
        binding.slot3.setOnClickListener {
            if (binding.slot3.isChecked()) {
                selectedSlot[2] = 1
                Log.e("Selectedslots:::>", "1True$selectedSlot")
            }
            else{
                selectedSlot[2] = 0
                Log.e("Selectedslots:::>", "1True$selectedSlot")
            }
            setavl()
        }

        slots[i]= selectedSlot
        Log.e("Selectedslots:::>","$selectedSlot")
        setavl()
    }
}