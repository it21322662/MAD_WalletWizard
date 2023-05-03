package com.example.walletwizard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

@Suppress("DEPRECATION")
class ExpensesManage : AppCompatActivity() {
    lateinit var amountadd:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addexpenses)
        amountadd = findViewById(R.id.amount_add)
        //Remove Action Bar/Title Bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()


    }
    fun buttonClick(view: View?){
        println(amountadd.text)
    }
}