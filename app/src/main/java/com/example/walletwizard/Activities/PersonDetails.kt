package com.example.walletwizard.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.walletwizard.Activities.FetchingActivity
import com.example.walletwizard.R
import com.example.walletwizard.Models.PersonModel
import com.google.firebase.database.FirebaseDatabase

class PersonDetails : AppCompatActivity() {
    private lateinit var tvpId: TextView
    private lateinit var tvPName: TextView
    private lateinit var tvDebttype: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvDes: TextView
    private lateinit var tvTotamount: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_person_details)

        val backarrow: ImageButton = findViewById<ImageButton>(R.id.bcarrowf)
        backarrow.setOnClickListener {
            val i = Intent(this, FetchingActivity::class.java)
            startActivity(i)

        }

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("tvPid").toString(),
                intent.getStringExtra("tvPName").toString(),
            )
        }
            btnDelete.setOnClickListener {
                deleteRecord(
                    intent.getStringExtra("tvPid").toString()
                )
            }

        }
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Persons").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        tvpId = findViewById(R.id.tvpId)
        tvPName = findViewById(R.id.tvPName)
        tvDebttype = findViewById(R.id.tvDebttype)
        tvDate = findViewById(R.id.tvDate)
        tvDes = findViewById(R.id.tvDes)
        tvTotamount = findViewById(R.id.tvTotamount)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvpId.text = intent.getStringExtra("tvPid")
        tvPName.text = intent.getStringExtra("tvPName")
        tvDebttype.text = intent.getStringExtra("tvDebttype")
        tvDate.text = intent.getStringExtra("tvDate")
       tvDes.text = intent.getStringExtra("tvDes")
        tvTotamount.text = intent.getStringExtra("tvTotamount")
    }

    private fun openUpdateDialog(
        pId: String,
        pName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.activity_update_dialog, null)

        mDialog.setView(mDialogView)

        val etpNames = mDialogView.findViewById<EditText>(R.id.etpNames)
        val etDebttypes = mDialogView.findViewById<EditText>(R.id.etDebttype)
        val etDates = mDialogView.findViewById<EditText>(R.id.etDate)
        val etDess = mDialogView.findViewById<EditText>(R.id.etDes)
        val etTotamount = mDialogView.findViewById<EditText>(R.id.etTotamount)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etpNames.setText(intent.getStringExtra("tvPName").toString())
        etDebttypes.setText(intent.getStringExtra("tvDebttype").toString())
        etDates.setText(intent.getStringExtra("tvDate").toString())
        etDess.setText(intent.getStringExtra("tvDes").toString())
        etTotamount.setText(intent.getStringExtra("tvTotamount").toString())



        mDialog.setTitle("Updating $pName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updatePersonData(
                pId,
                etpNames.text.toString(),
                etDebttypes.text.toString(),
                etDates.text.toString(),
                etDess.text.toString(),
                etTotamount.text.toString()


            )
            Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews

            tvPName.text = etpNames.text.toString()
           tvDebttype.text = etDebttypes.text.toString()
           tvDate.text = etDates.text.toString()
            tvDes.text = etDess.text.toString()
            tvTotamount.text = etTotamount.text.toString()

            alertDialog.dismiss()

        }
    }

    private fun updatePersonData(
        id: String,
        pname: String,
        debttype: String,
        date: String,
        des: String,
        totamount:String

    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Persons").child(id)
        val perInfo = PersonModel(id, pname, debttype, date, des,totamount)
        dbRef.setValue(perInfo)
    }

}




