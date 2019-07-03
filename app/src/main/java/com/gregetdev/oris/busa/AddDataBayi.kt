package com.gregetdev.oris.busa

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.THEME_HOLO_LIGHT
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
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.gregetdev.oris.busa.model.AlarmModel
import com.gregetdev.oris.busa.model.DataImunisasiModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_data_bayi.*
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.Months
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddDataBayi : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ImageStore: FirebaseStorage
    private lateinit var datePicker: ImageButton

    private lateinit var tglLahir_edit: EditText
    private lateinit var Umur_edit: EditText
    private lateinit var Nama_edit: EditText
    private lateinit var laki: RadioButton
    private lateinit var Perempuan: RadioButton
    private lateinit var gender: RadioGroup

    private lateinit var SelecetedImage: ImageView


    private lateinit var PicturePic: Button

    var selectedPhotoUri: Uri? = null
    val calendar: Calendar = Calendar.getInstance()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data_bayi)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        Nama_edit = findViewById(R.id.Namabayi_edt)
        tglLahir_edit = findViewById(R.id.Tglbayi_edt)
        Umur_edit = findViewById(R.id.UmurBayi_edt)
        laki = findViewById(R.id.Laki_RadioButton)
        Perempuan = findViewById(R.id.Perempuan_RadioButton)
        gender = findViewById(R.id.gender_RGrup)
        SelecetedImage = findViewById(R.id.SelecetedImage_ImageVIew)




        backButton_addDataBayi.setOnClickListener() {
            val intent = Intent(this@AddDataBayi, DataBayi::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }




        tglLahir_edit.setOnClickListener {
            openDatePicker()
        }

        datePicker = findViewById(R.id.datePicker)
        datePicker.setOnClickListener {
            openDatePicker()
        }

        TambahDataBtn.setOnClickListener() {

            if (Nama_edit.text.toString().isEmpty()) {
                Nama_edit.error = "Masukan Nama"
                Nama_edit.requestFocus()
                return@setOnClickListener
            } else if (tglLahir_edit.text.toString().isEmpty()) {
                tglLahir_edit.error = "Masukan Tanggal Lahir"
                tglLahir_edit.requestFocus()
                return@setOnClickListener
            } else if (!(laki.isChecked || Perempuan.isChecked)){
                Toast.makeText(this@AddDataBayi,"Pilih jenis kelamin",Toast.LENGTH_LONG).show()
            } else{
                uploadImage()

            }


        }
        SelectPhotoButton.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d("Add Photo", "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            SelecetedImage_ImageVIew.setImageBitmap(bitmap)
            SelectPhotoButton.alpha = 0f

        }
    }

    private fun TambahData(imageURL: String) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        val Table_bayi = database.getReference("/Bayi")


        val nama = Nama_edit.text.toString()
        val tgl = tglLahir_edit.text.toString()
        val umur = Umur_edit.text.toString()
        val ParentID = auth.uid ?: ""
        val ImageURL = imageURL
        var Jekel = ""
        if (Laki_RadioButton.isChecked){
           Jekel = "Laki - Laki"
        } else if (Perempuan_RadioButton.isChecked){
            Jekel = "Perempuan"
        }




        val key = Table_bayi.push().key
        val databayi = BayiModel(nama, tgl, umur, ParentID, ImageURL,Jekel)
        if (key != null) {
            Table_bayi.child(key).setValue(databayi)
                .addOnCompleteListener() {
                    Log.d("Tambah data bayi", "Data telah disimpan di database")

                    AddDataImunisasiAwal(key)

                    val intent = Intent(this@AddDataBayi, DataBayi::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    Toast.makeText(this@AddDataBayi, "Data telah ditambahkan", Toast.LENGTH_LONG).show()
                }.addOnFailureListener() {
                    Toast.makeText(this@AddDataBayi, "Data gagal di input", Toast.LENGTH_LONG).show()
                }
        }


    }

    private fun AddDataImunisasiAwal(key: String) {
        val Data_imunisasi = database.getReference("/Data Imunisasi/$key")
        val Alarm = database.getReference("/Alarm/$key")
        //val imunisasiKEY = Data_imunisasi.push().key

        val Year = calendar.get(Calendar.YEAR)
        var Months = calendar.get(Calendar.MONTH)
        var Day = calendar.get(Calendar.DAY_OF_MONTH)

        Log.d("Calendar","Tgl Imunisasai, $Day/$Months/$Year")

        Data_imunisasi.run {
            child("Hepatitis B")
                .setValue(DataImunisasiModel("Hepatitis B",TglImunisasi(0),"Belum","0"))
            child("BCG, Polio 1")
                .setValue(DataImunisasiModel("BCG, Polio 1",TglImunisasi(1),"Belum","1"))
            child("DPT-HB-Hib 1, Polio 2")
                .setValue(DataImunisasiModel("DPT-HB-Hib 1, Polio 2",TglImunisasi(2),"Belum","2"))
            child("DPT-HB-Hib 2, Polio 3")
                .setValue(DataImunisasiModel("PT-HB-Hib 2, Polio 3",TglImunisasi(3),"Belum","3"))
            child("DPT-HB-Hib 3, Polio 4, IPV")
                .setValue(DataImunisasiModel("DPT-HB-Hib 3, Polio 4, IPV",TglImunisasi(4),"Belum","4"))
            child("Campak")
                .setValue(DataImunisasiModel("Campak",TglImunisasi(9),"Belum","9"))
        }
        Alarm.run {
            child("Hepatitis B")
                .setValue(AlarmModel("Hepatitis B",TglImunisasi(0),"--:--",false,"1"))
            child("BCG, Polio 1")
                .setValue(AlarmModel("BCG, Polio 1",TglImunisasi(1),"--:--",false,"2"))
            child("DPT-HB-Hib 1, Polio 2")
                .setValue(AlarmModel("DPT-HB-Hib 1, Polio 2",TglImunisasi(2),"--:--",false,"3"))
            child("DPT-HB-Hib 2, Polio 3")
                .setValue(AlarmModel("PT-HB-Hib 2, Polio 3",TglImunisasi(3),"--:--",false,"4"))
            child("DPT-HB-Hib 3, Polio 4, IPV")
                .setValue(AlarmModel("DPT-HB-Hib 3, Polio 4, IPV",TglImunisasi(4),"--:--",false,"5"))
            child("Campak")
                .setValue(AlarmModel("Campak",TglImunisasi(9),"--:--",false,"6"))
        }
    }

    private fun TglImunisasi(tambah: Int): String {
        var Year = calendar.get(Calendar.YEAR)
        var Months = calendar.get(Calendar.MONTH) + 1
        var Day = calendar.get(Calendar.DAY_OF_MONTH)

        var MonthsPlus = Months + tambah
        var DaysPlus: String

        if (Day in 1..9){
            DaysPlus = "0$Day"
        } else {
            DaysPlus = "$Day"
        }

        if (MonthsPlus > 12){
            MonthsPlus -= 12
            Year += 1
            if(MonthsPlus in 1..9) {
                Log.d("Calendar","$Day/0$MonthsPlus/$Year")
                return "$DaysPlus/0$MonthsPlus/$Year"
            } else {
                Log.d("Calendar","$Day/$MonthsPlus/$Year")
                return "$DaysPlus/$MonthsPlus/$Year"
            }
        } else {
            if(MonthsPlus in 1..9) {
                Log.d("Calendar","$Day/0$MonthsPlus/$Year")
                return "$DaysPlus/0$MonthsPlus/$Year"
            } else {
                Log.d("Calendar","$Day/$MonthsPlus/$Year")
                return "$DaysPlus/$MonthsPlus/$Year"
            }
        }



    }

    private fun uploadImage() {
        ImageStore = FirebaseStorage.getInstance()
        if (selectedPhotoUri == null) {
            Toast.makeText(this@AddDataBayi, "Tambahkan foto", Toast.LENGTH_LONG).show()
            return
        }


        val imageName = UUID.randomUUID().toString()
        val ImageStorage = ImageStore.getReference("/Profil/$imageName")
        add_data_loading.visibility = VISIBLE
        TambahDataBtn.visibility = INVISIBLE

        ImageStorage.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Tambah data bayi", "Foto telah di upload")
                ImageStorage.downloadUrl.addOnSuccessListener {
                    Log.d("Tambah data bayi", "$it")
                    TambahData(it.toString())
                }.addOnFailureListener(){
                    add_data_loading.visibility = INVISIBLE
                    TambahDataBtn.visibility = VISIBLE
                    Toast.makeText(this@AddDataBayi, "Upload gagal ", Toast.LENGTH_LONG).show()
                }

            }
    }

        @RequiresApi(Build.VERSION_CODES.N)
        private fun openDatePicker() {

            val setDateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month , dayOfMonth ->

                    calendar.run {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }

                    tglLahir_edit.setText(getReadableDate(calendar).toString())
                    tglLahir_edit.setTextColor(Color.BLACK)
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
            Umur_edit.setText("$MonthBetween")
            Umur_edit.setTextColor(Color.BLACK)
            SatuanUmur.visibility = View.VISIBLE

        }


}