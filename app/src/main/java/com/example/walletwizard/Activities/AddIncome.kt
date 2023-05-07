package com.example.walletwizard.Activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.Models.incomeModel
import com.example.walletwizard.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddIncome : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference

    private lateinit var amount: EditText
    private lateinit var type: EditText
    private lateinit var note: EditText
    private lateinit var addbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.add_income)

        amount = findViewById(R.id.etamount)
        type = findViewById(R.id.ettype)
        note = findViewById(R.id.etnote)
        addbtn = findViewById(R.id.incomeSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Income")

        addbtn.setOnClickListener {
            saveIncome()
        }
    }
    private fun validateFields(incomeAmount: String, incomeNote: String, expensesType: String): Boolean {
        if (incomeAmount.isEmpty() || incomeNote.isEmpty() || expensesType.isEmpty()) {
            return false;
        }
        return true;
    }

    private fun saveIncome() {
        //getting Values
        val incomeAmount = amount.text.toString()
        val incomeNote = note.text.toString()
        val incomeType = type.text.toString()

        if (incomeAmount.isEmpty()) {
            amount.error = "Please enter amount"
        }

        if (incomeNote.isEmpty()) {
            note.error = "Please enter note"
        }

        if (incomeType.isEmpty()) {
            type.error = "Please enter type"
        }
        if (!validateFields(incomeAmount,incomeNote,incomeType)){
            Toast.makeText(this, "Please Insert All Data", Toast.LENGTH_LONG).show()
            return;
        }
        val incomeId = dbRef.push().key!!
        val income = incomeModel(incomeId, incomeAmount, incomeNote, incomeType)
        dbRef.child(incomeId).setValue(income)

            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                amount.text.clear()
                note.text.clear()
                type.text.clear()
            }.addOnFailureListener { err ->

                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

            }

    }
}
