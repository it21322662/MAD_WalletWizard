package com.example.walletwizard

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExpenseManage : AppCompatActivity() {
     private lateinit var fab: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_expense)

        fab = findViewById(R.id.fab)

        fab.setOnClickListener {
          val intent = Intent(this, AddExpenses::class.java)
            startActivity(intent)
        }


    }
}