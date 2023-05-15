package com.example.walletwizard.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.R

class MainActivity : AppCompatActivity() {
    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    private lateinit var exbackbtn : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_main)


        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)
        exbackbtn = findViewById(R.id.exbackbtn)

        btnInsertData.setOnClickListener {
            val intent = Intent(this, AddExpenses::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener {
            val intent = Intent(this, ExpenseFetchingActivity::class.java)
            startActivity(intent)
        }

        exbackbtn.setOnClickListener {
            val intent = Intent(this, InExMainActivity::class.java)
            startActivity(intent)
        }

    }
}