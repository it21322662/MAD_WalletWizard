package com.example.walletwizard.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.walletwizard.Activities.MainActivity
import com.example.walletwizard.Activities.PMainActivity
import com.example.walletwizard.Models.PersonModel
import com.example.walletwizard.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {
    private lateinit var etPname: EditText
    private lateinit var etDebttype: EditText
    private lateinit var etDate: EditText
    private lateinit var etDes: EditText
    private lateinit var etTotamount: EditText
    private lateinit var btnSave: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_insertion)

        etPname= findViewById(R.id.etPname)
        etDebttype= findViewById(R.id.etDebttype)
        etDate= findViewById(R.id.etDate)
        etDes= findViewById(R.id.etDes)
        etTotamount=findViewById(R.id.etTotamount)
        btnSave = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Persons")

        btnSave.setOnClickListener {
            savePersonData()
        }
    }
    private fun validateFields(  pName:String, pDebttype:String, pDate:String,pDes:String,pAmount:String): Boolean {
        if (pName.isEmpty() || pDebttype.isEmpty() || pDebttype.isEmpty()|| pDate.isEmpty()||pDes.isEmpty()||pAmount.isEmpty(

        )) {
            return false;
        }
        return true;
    }
    private fun isAmountFieldValidated(expAmt: String): Boolean {
        var pattern = "^[+-]?\\d+$".toRegex()
        if (!pattern.matches(expAmt)) {
            return false;
        }
        return true
    }
    private fun  savePersonData() {

        //getting values
        val pName = etPname.text.toString()
        val pDebttype = etDebttype.text.toString()
        val pDate = etDate.text.toString()
        val pDes = etDes.text.toString()
        val pAmount = etTotamount.text.toString()


        if (pName.isEmpty()) {
            etPname.error = "Please enter name"
        }
        if (pDebttype.isEmpty()) {
            etDebttype.error = "Please enter age"
        }
        if (pDate.isEmpty()) {
            etDate.error = "Please enter salary"
        }
        if (pDes.isEmpty()) {
            etDes.error = "Please enter salary"
        }
        if (pAmount.isEmpty()) {
           etTotamount.error = "Please enter salary"
        }

        if (!isAmountFieldValidated(pAmount)) {
            Toast.makeText(this, "Please check Income amount format", Toast.LENGTH_LONG).show()
            return;
        }

        if (!validateFields(pName,pDebttype,pDate,pDes,pAmount)){
            Toast.makeText(this, "Please Insert All Data", Toast.LENGTH_LONG).show()
            return;
        }


        val pId = dbRef.push().key!!

        val person = PersonModel(pId,pName,pDebttype,pDate,pDes,pAmount)

        dbRef.child(pId).setValue(person)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etPname.text.clear()
                etDebttype.text.clear()
                etDate.text.clear()
                etDes.text.clear()
                etTotamount.text.clear()

                val intent = Intent(this, PMainActivity::class.java)
                finish()
                startActivity(intent)

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

}



