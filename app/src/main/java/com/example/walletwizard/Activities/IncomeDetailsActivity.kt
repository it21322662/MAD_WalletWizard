package com.example.walletwizard.Activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.Models.incomeModel
import com.example.walletwizard.R
import com.google.firebase.database.FirebaseDatabase

class IncomeDetailsActivity : AppCompatActivity() {
    private lateinit var amount_add: TextView
    private lateinit var type_add: TextView
    private lateinit var note_add: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_income_update)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("incomeId").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("incomeId").toString()
            )
        }

    }

    private fun initView() {
        amount_add = findViewById(R.id.amount_add)
        type_add = findViewById(R.id.type_add)
        note_add = findViewById(R.id.note_add)

        btnUpdate = findViewById(R.id.btnUpdate_income)
        btnDelete = findViewById(R.id.btnDelete_income)
    }

    private fun setValuesToViews() {

        println("catch values  ::::::::::::"+ intent.getStringExtra("incomeId"))

        amount_add.text = intent.getStringExtra("incomeAmt")
        type_add.text = intent.getStringExtra("incomeType")
        note_add.text = intent.getStringExtra("incomeNote")

    }

//    private fun deleteRecord(
//        id: String
//    ){
//        val dbRef = FirebaseDatabase.getInstance().getReference("Income").child(id)
//        val mTask = dbRef.removeValue()
//
//        mTask.addOnSuccessListener {
//            Toast.makeText(this, "Income data deleted", Toast.LENGTH_LONG).show()
//
//            val intent = Intent(this, IncomeMainActivity::class.java)
//            finish()
//            startActivity(intent)
//        }.addOnFailureListener{ error ->
//            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
//        }
//    }

    private fun deleteRecord(id: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Record")
        alertDialog.setMessage("Are you sure you want to delete this record?")
        alertDialog.setPositiveButton("Delete") { dialog: DialogInterface, which: Int ->
            performDeleteRecord(id)
        }
        alertDialog.setNegativeButton("Cancel", null)
        alertDialog.show()
    }

    private fun performDeleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Income").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Income data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, IncomeMainActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun openUpdateDialog(
        empId: String,
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_income, null)

        mDialog.setView(mDialogView)

        val etamount = mDialogView.findViewById<EditText>(R.id.etamount)
        val ettype = mDialogView.findViewById<EditText>(R.id.ettype)
        val etnote = mDialogView.findViewById<EditText>(R.id.etnote)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etamount.setText(intent.getStringExtra("incomeAmt").toString())
        ettype.setText(intent.getStringExtra("incomeType").toString())
        etnote.setText(intent.getStringExtra("incomeNote").toString())

        mDialog.setTitle("Updating $empId Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                intent.getStringExtra("incomeId").toString(),
                etamount.text.toString(),
                etnote.text.toString(),
                ettype.text.toString(),
            )

            Toast.makeText(applicationContext, "Income Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            amount_add.text = etamount.text.toString()
            type_add.text = ettype.text.toString()
            note_add.text = etnote.text.toString()

            val intent = Intent(this, IncomeMainActivity::class.java)
            finish()
            startActivity(intent)

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        incomeId : String,
        incomeAmount : String,
        incomeNote : String,
        incomeType : String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Income").child(incomeId)
        val empInfo = incomeModel(incomeId, incomeAmount, incomeNote, incomeType)
        dbRef.setValue(empInfo)
    }
}