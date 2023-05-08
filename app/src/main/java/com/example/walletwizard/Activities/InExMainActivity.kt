package com.example.walletwizard.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.R

class InExMainActivity : AppCompatActivity() {
    private lateinit var btnExpenses : Button
    private lateinit var btnIncome: Button
    private lateinit var btnDebt: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_in_ex_main)


        btnExpenses = findViewById(R.id.btnExpenses)
        btnIncome = findViewById(R.id.btnIncome)
        btnDebt= findViewById(R.id.btndebt2)


        btnExpenses.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnIncome.setOnClickListener {
            val intent = Intent(this, IncomeMainActivity::class.java)
            startActivity(intent)
        }

      btnDebt.setOnClickListener {
          val intent = Intent(this, PMainActivity::class.java)
            startActivity(intent)
        }

    }
}