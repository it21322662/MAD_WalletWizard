package com.example.walletwizard.activities

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
    private lateinit var etBankloans: EditText
    private lateinit var etPerLoan: EditText
    private lateinit var etLeasing: EditText
    private lateinit var btnSave: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_insertion)

        etPname= findViewById(R.id.etPname)
        etBankloans= findViewById(R.id.etBankloans)
        etPerLoan= findViewById(R.id.etPerLoan)
        etLeasing= findViewById(R.id.etLeasing)
        btnSave = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Persons")

        btnSave.setOnClickListener {
            savePersonData()
        }
    }
    private fun validateFields(  pName:String, pBankloans:String, pPerLoan:String,pLeasing:String): Boolean {
        if (pName.isEmpty() || pBankloans.isEmpty() || pBankloans.isEmpty()|| pPerLoan.isEmpty()||pLeasing.isEmpty()) {
            return false;
        }
        return true;
    }
    private fun  savePersonData() {

        //getting values
        val pName = etPname.text.toString()
        val pBankloans = etBankloans.text.toString()
        val pPerLoan = etPerLoan.text.toString()
        val pLeasing = etLeasing.text.toString()

        if (pName.isEmpty()) {
            etPname.error = "Please enter name"
        }
        if (pBankloans.isEmpty()) {
            etBankloans.error = "Please enter age"
        }
        if (pPerLoan.isEmpty()) {
            etPerLoan.error = "Please enter salary"
        }
        if (pLeasing.isEmpty()) {
            etLeasing.error = "Please enter salary"
        }

        if (!validateFields(pName,pBankloans,pPerLoan,pLeasing)){
            Toast.makeText(this, "Please Insert All Data", Toast.LENGTH_LONG).show()
            return;
        }


        val pId = dbRef.push().key!!

        val person = PersonModel(pId,pName,pBankloans,pPerLoan,pLeasing)

        dbRef.child(pId).setValue(person)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etPname.text.clear()
                etBankloans.text.clear()
                etPerLoan.text.clear()
                etLeasing.text.clear()

                val intent = Intent(this, PMainActivity::class.java)
                finish()
                startActivity(intent)

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

}



