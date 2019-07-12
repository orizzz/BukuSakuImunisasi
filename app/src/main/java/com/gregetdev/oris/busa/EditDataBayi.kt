package com.gregetdev.oris.busa

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_data_bayi.*
import org.joda.time.LocalDate
import org.joda.time.Months
import java.text.SimpleDateFormat
import java.util.*

class EditDataBayi : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var ImageStore: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    var selectedPhotoUri: Uri? = null
    val calendar: Calendar = Calendar.getInstance()

    private lateinit var tglLahir_edit: EditText
    private lateinit var Umur_edit: EditText
    private lateinit var Nama_edit: EditText
    private lateinit var laki: RadioButton
    private lateinit var Perempuan: RadioButton
    private lateinit var gender: RadioGroup
    private lateinit var SelecetedImage: ImageView

    var imageURL : String = ""
    var keyBayi  : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_data_bayi)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val key = intent.getStringExtra("Key")
        keyBayi = key
        database = FirebaseDatabase.getInstance()
        val table_bayi = database.getReference("/Bayi")

        table_bayi.child(key).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val profilbayi = dataSnapshot.getValue(BayiModel::class.java)

                Edit_data_Namabayi_edt.setText(profilbayi?.namaBayi)
                Edit_data_Tglbayi_edt.setText(profilbayi?.tglLahir)
                Edit_data_UmurBayi_edt.setText(profilbayi?.umur)
                if (profilbayi?.jekel.equals("Laki - Laki")){
                    Laki_RadioButton.isChecked = true
                } else {
                    Perempuan_RadioButton.isChecked = true
                }
                Picasso.get().load(profilbayi?.imageURL).into(SelecetedImage_ImageVIew)
                imageURL = profilbayi?.imageURL.toString()
                SelectPhotoButton.alpha = 0f
            }
        })

        Edit_data_Btn.setOnClickListener() {
            if (Edit_data_Namabayi_edt.text.toString().isEmpty()) {
                Edit_data_Namabayi_edt.error = "Masukan Nama"
                Edit_data_Namabayi_edt.requestFocus()
                return@setOnClickListener
            } else if (Edit_data_Tglbayi_edt.text.toString().isEmpty()) {
                Edit_data_Tglbayi_edt.error = "Masukan Tanggal Lahir"
                Edit_data_Tglbayi_edt.requestFocus()
                return@setOnClickListener
            } else if (!(Laki_RadioButton.isChecked || Perempuan_RadioButton.isChecked)){
                Toast.makeText(this@EditDataBayi,"Pilih jenis kelamin", Toast.LENGTH_LONG).show()
            } else{
                uploadImage()
            }
        }

        Edit_data_Tglbayi_edt.setOnClickListener {openDatePicker()}
        datePicker.setOnClickListener {openDatePicker()}
        SelectPhotoButton.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    private fun uploadImage() {
        Log.d("Edit","$selectedPhotoUri")
        ImageStore = FirebaseStorage.getInstance()
        if (selectedPhotoUri == null) {
            add_data_loading.visibility = View.VISIBLE
            Edit_data_Btn.visibility = View.INVISIBLE
            EditData(imageURL)
        } else {
            val imageName = UUID.randomUUID().toString()
            val ImageStorage = ImageStore.getReference("/Profil/$imageName")
            add_data_loading.visibility = View.VISIBLE
            Edit_data_Btn.visibility = View.INVISIBLE

            ImageStorage.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Tambah data bayi", "Foto telah di upload")
                    ImageStorage.downloadUrl.addOnSuccessListener {
                        Log.d("Tambah data bayi", "$it")
                        EditData(it.toString())
                    }.addOnFailureListener(){
                        add_data_loading.visibility = View.INVISIBLE
                        Edit_data_Btn.visibility = View.VISIBLE

                    }

                }
        }

    }

    private fun EditData(imageURL: String) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val Table_bayi = database.getReference("/Bayi")
        val nama = Edit_data_Namabayi_edt.text.toString()
        val tgl = Edit_data_Tglbayi_edt.text.toString()
        val umur = Edit_data_UmurBayi_edt.text.toString()
        val ParentID = auth.uid ?: ""
        val ImageURL = imageURL
        var Jekel = ""
        if (Laki_RadioButton.isChecked){ Jekel = "Laki - Laki"}
        else if (Perempuan_RadioButton.isChecked){ Jekel = "Perempuan" }
        val key = keyBayi
        val databayi = BayiModel(nama, tgl, umur, ParentID, ImageURL,Jekel)
        Table_bayi.child(key).setValue(databayi)
            .addOnCompleteListener() {
                Log.d("Tambah data bayi", "Data telah disimpan di database")
                val intent = Intent(this@EditDataBayi, Profile::class.java)
                intent.putExtra("BayiKey",key)
                /*intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)*/
                startActivity(intent)
                Toast.makeText(this@EditDataBayi, "Data telah di edit", Toast.LENGTH_LONG).show()
            }.addOnFailureListener() {
                Toast.makeText(this@EditDataBayi, "Data gagal di edit", Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d("Add Photo", "Photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            SatuanUmur.visibility = View.VISIBLE
            SelecetedImage_ImageVIew.setImageBitmap(bitmap)
            SelectPhotoButton.alpha = 0f
        }
    }

    private fun openDatePicker() {

        val setDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                calendar.run {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }

                Edit_data_Tglbayi_edt.setText(getReadableDate(calendar).toString())
                Edit_data_Tglbayi_edt.setTextColor(Color.BLACK)
                getAge(year, month+1, dayOfMonth)

            }

        DatePickerDialog(
            this, setDateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    private fun getReadableDate(calendar: Calendar): String? {
        val dformat = SimpleDateFormat("dd/MM/YYYY")
        Log.d("Calendar","Add tgl lahir = ${dformat.format(calendar.time)}")
        return dformat.format(calendar.time)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getAge(year: Int, month: Int, day: Int) {
        val dob = Calendar.getInstance()

        dob.set(year, month, day)

        val lahir = LocalDate(year, month, day)
        val hariIni = LocalDate.now()

        Log.d("tglLahir", "TGL lahir : $lahir")
        Log.d("tglLahir", "Hari ini : $hariIni")


        val MonthBetween = Months.monthsBetween(lahir, hariIni).getMonths()
        Edit_data_UmurBayi_edt.setText("$MonthBetween")
        Edit_data_UmurBayi_edt.setTextColor(Color.BLACK)
        SatuanUmur.visibility = View.VISIBLE

    }
}
