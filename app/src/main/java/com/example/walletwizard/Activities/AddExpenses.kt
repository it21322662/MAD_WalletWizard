package com.example.walletwizard.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
    private lateinit var exbackadd : ImageView

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
        exbackadd = findViewById(R.id.exbackadd)

        dbRef = FirebaseDatabase.getInstance().getReference("Expenses")

        addbtn.setOnClickListener {
            saveExpenses()
        }

        exbackadd.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
    private fun isFieldsEmpty(expensesAmount: String, expensesNote: String, expensesType: String): Boolean {
        if (expensesAmount.isEmpty() || expensesNote.isEmpty() || expensesType.isEmpty()) {
            return true
        }
        return false;
    }

    private fun isAmountFieldValidated(expAmt: String): Boolean {
        var pattern = "^[+-]?\\d+$".toRegex()
        if (!pattern.matches(expAmt)) {
            return false;
        }
        return true
    }

//    private fun validateFields(expensesAmount: String, expensesNote: String, expensesType: String): List<String> {
//        val errorMessages = mutableListOf<String>()
//
//        var pattern = "^[+-]?\\d+$".toRegex()
//        if (!expensesAmount.matches(pattern)) {
//            errorMessages.add("Expenses Amount cannot be letters")
//        }
//        if (expensesAmount.isEmpty() || expensesNote.isEmpty() || expensesType.isEmpty()) {
//            errorMessages.add("Fields cannot be empty")
//        }
//
//        return errorMessages
//    }

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

//        val errorMessages = validateFields(expensesAmount,expensesNote,expensesType);
//        if (errorMessages.isNotEmpty()) {
//            errorMessages.forEach { errMsg ->
//                Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show()
//            }
//            return;
//        }
        if (isFieldsEmpty(expensesAmount,expensesNote,expensesType)){
            return;
        }
//
        if (!isAmountFieldValidated(expensesAmount)) {
            Toast.makeText(this, "Please check expense amount format", Toast.LENGTH_LONG).show()
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
