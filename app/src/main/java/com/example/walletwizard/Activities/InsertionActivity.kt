package com.example.walletwizard.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.walletwizard.models.PersonModel
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


        val pId = dbRef.push().key!!

        val person = PersonModel(pId,pName,pBankloans,pPerLoan,pLeasing)

        dbRef.child(pId).setValue(person)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etPname.text.clear()
                etBankloans.text.clear()
                etPerLoan.text.clear()
                etLeasing.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

}



