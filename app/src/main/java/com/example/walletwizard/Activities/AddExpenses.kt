package com.example.walletwizard.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.walletwizard.Models.expensesModel
import com.example.walletwizard.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddExpenses : AppCompatActivity() {
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

        setContentView(R.layout.add_expenses)

        amount = findViewById(R.id.etamount)
        type = findViewById(R.id.ettype)
        note = findViewById(R.id.etnote)
        addbtn = findViewById(R.id.expenseSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Expenses")

        addbtn.setOnClickListener {
            saveExpenses()
        }
    }
    private fun validateFields(expensesAmount: String, expensesNote: String, expensesType: String): Boolean {
        if (expensesAmount.isEmpty() || expensesNote.isEmpty() || expensesType.isEmpty()) {
            return false;
        }
        return true;
    }

    private fun saveExpenses() {
        //getting Values
        val expensesAmount = amount.text.toString()
        val expensesNote = note.text.toString()
        val expensesType = type.text.toString()

         if (expensesAmount.isEmpty()) {
             amount.error = "Please enter amount"
          }

        if (expensesNote.isEmpty()) {
            note.error = "Please enter note"
             }

        if (expensesType.isEmpty()) {
            type.error = "Please enter type"
             }
        if (!validateFields(expensesAmount,expensesNote,expensesType)){
            Toast.makeText(this, "Please Insert All Data", Toast.LENGTH_LONG).show()
            return;
        }
        val expensesId = dbRef.push().key!!
        val expenses = expensesModel(expensesId, expensesAmount, expensesNote, expensesType)
       dbRef.child(expensesId).setValue(expenses)

       .addOnCompleteListener {
             Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

           amount.text.clear()
           note.text.clear()
           type.text.clear()

           val intent = Intent(this, MainActivity::class.java)
           finish()
           startActivity(intent)

       }.addOnFailureListener { err ->

        Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

            }

    }
}
