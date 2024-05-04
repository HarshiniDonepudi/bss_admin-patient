package com.example.lion_nav_barhomepage.doctor
import com.example.lion_nav_barhomepage.R
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
import coil.clear

import com.example.lion_nav_barhomepage.databinding.ActivityAdddoctorBinding
import com.example.lion_nav_barhomepage.doctor.Data
import com.example.lion_nav_barhomepage.doctor.Doctorslist
import com.example.lion_nav_barhomepage.doctor.doctor
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class AddDoctorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdddoctorBinding
    var slots : MutableList<MutableList<Int>> = mutableListOf(mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0),mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0))
    var avl: MutableList<Int> = mutableListOf(0)
    var selectedPhotoUri : Uri?=null
    private lateinit var dbref : DatabaseReference
    private lateinit var cropIntent:Intent
    var img_url = "https://images.app.goo.gl/TW4Eu84iUQzBvUoM8"
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdddoctorBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        //---------------------------------
        binding.img.setOnClickListener{
          val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)


        }

        //---------------------------------
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

        binding.button.setOnClickListener {

            val name = binding.editText1.text.toString()
            val id = binding.editText2.text.toString()
            val spec = binding.editText3.text.toString()
            val about = binding.editText4.text.toString()

            val exp = binding.editText5.text.toString()
            val lan= binding.editText6.text.toString()
            val fee = binding.editText7.text.toString()
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

                if (selectedPhotoUri != null ) {
                    upload()
                    val ref = FirebaseStorage.getInstance().getReference("/img/$id")
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                    val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

                    ref.putBytes(reducedImage)
//                    ref.putFile(selectedPhotoUri!!)
                        .addOnSuccessListener {


                            Log.d("Uploading", "sucessfully:${it.metadata?.path}")

                            ref.downloadUrl.addOnSuccessListener {
                                img_url = it.toString()
                                Log.e("img-inside", "url====>$img_url")
//================avl.removeAt(0)

                                Log.e("avl-inside", "url====>$avl")
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

                                val toast =
                                    Toast.makeText(this, " Sucessfully added", Toast.LENGTH_SHORT)
                                toast.show()
                                val intent = Intent(this, Doctorslist::class.java)
                                startActivity(intent)
//==========================
                            }

                        }.addOnFailureListener {

                            val toast = Toast.makeText(this," Failed ",Toast.LENGTH_SHORT)
                            toast.show()
                            Log.d("Uploading", "Failed ")
                        }

                    Log.e("out", "url====>$img_url")

                }
                else{
                    upload()
                    val data = Data(
                        id.toInt(),
                        name,
                        spec,
                        about,
                        "https://firebasestorage.googleapis.com/v0/b/admindb-44854.appspot.com/o/img%2Fdoctor.jpeg?alt=media&token=83323dcd-9667-4737-9245-5fdd58caf149"
                        ,exp,
                        avl as ArrayList<Int>,
                        slots,
                        lan,
                        fee
                    )
                    Log.e("data", "$id ,$name , $about , $exp ,$lan")
                    setavl()
                    dbref.child(data.id.toString()).setValue(data)
                    val toast =
                        Toast.makeText(this, " Sucessfully added", Toast.LENGTH_SHORT)
                    toast.show()
                    val intent = Intent(this, Doctorslist::class.java)
                    startActivity(intent)
                }

            }
            else if(flag==1){
                binding.editText2.error="ID Already Exists!"
                           binding.editText2.requestFocus()

            }



    }
    }
//    private fun doCrop(picUri: Uri) {
//        try {
//            val cropIntent = Intent("com.android.camera.action.CROP")
//            cropIntent.setDataAndType(picUri, "image/*")
//            cropIntent.putExtra("crop", "true")
//            cropIntent.putExtra("aspectX", 1)
//            cropIntent.putExtra("aspectY", 1)
//            cropIntent.putExtra("outputX", 128)
//            cropIntent.putExtra("outputY", 128)
//            cropIntent.putExtra("return-data", true)
//            startActivityForResult(cropIntent, 1)
//        } // respond to users whose devices do not support the crop action
//        catch (anfe: ActivityNotFoundException) {
//            // display an error message
//            val errorMessage = "Whoops - your device doesn't support the crop action!"
//            val toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT)
//            toast.show()
//        }
//    }
    private fun upload() {

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading....")
            progressDialog.setCancelable(false)
            progressDialog.show()


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            binding.img.clear()
            selectedPhotoUri  =data.data
//            selectedPhotoUri?.let { doCrop(it) }
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            val bitmapDrawable= BitmapDrawable(bitmap)
            binding.img.setBackgroundDrawable(bitmapDrawable)
        }
//        if (requestCode === 1 && data!=null) {
////            selectedPhotoUri =data.data
////            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
////            val bitmapDrawable= BitmapDrawable(bitmap)
////            binding.img.setBackgroundDrawable(bitmapDrawable)
//                val extras: Bundle? = data.extras
//                val bitmap = extras!!.getParcelable<Bitmap>("data")
//                binding.img.setImageBitmap(bitmap)
//
//        }
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

//    private fun cropImages(){
//        try{
//            cropIntent=Intent("com.android.camera.action.CROP")
//            cropIntent.setDataAndType(selectedPhotoUri,*\)
//            cropIntent
//            cropIntent
//            cropIntent
//            cropIntent
//            cropIntent
//
//        }
//    }

}



