package com.example.walletwizard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.walletwizard.Models.expensesModel
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

        amount = findViewById(R.id.amount_add)
        type = findViewById(R.id.type_add)
        note = findViewById(R.id.note_add)
        addbtn = findViewById(R.id.expenseSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Expenses")

        addbtn.setOnClickListener {
            saveExpenses()
        }
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

        val expensesId = dbRef.push().key!!
        val expenses = expensesModel(expensesId, expensesAmount, expensesNote, expensesType)
       dbRef.child(expensesId).setValue(expenses)

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
