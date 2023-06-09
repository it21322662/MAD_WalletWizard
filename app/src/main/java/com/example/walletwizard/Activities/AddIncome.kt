package com.example.walletwizard.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
    private lateinit var incomebackbtn : ImageView

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
        incomebackbtn = findViewById(R.id.back)

        dbRef = FirebaseDatabase.getInstance().getReference("Income")

        addbtn.setOnClickListener {
            saveIncome()
        }

        incomebackbtn.setOnClickListener {
            val intent = Intent(this, IncomeMainActivity::class.java)
            startActivity(intent)
        }


    }
    private fun validateFields(incomeAmount: String, incomeNote: String, expensesType: String): Boolean {
        if (incomeAmount.isEmpty() || incomeNote.isEmpty() || expensesType.isEmpty()) {
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
            return;
        }
//
        if (!isAmountFieldValidated(incomeAmount)) {
            Toast.makeText(this, "Please check Income amount format", Toast.LENGTH_LONG).show()
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

                val intent = Intent(this, IncomeMainActivity::class.java)
                finish()
                startActivity(intent)

            }.addOnFailureListener { err ->

                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

            }

    }
}
