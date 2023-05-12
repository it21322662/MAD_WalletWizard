package com.example.walletwizard.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.R

class InExMainActivity : AppCompatActivity() {
    private lateinit var imageEx : ImageView
    private lateinit var imageIn: ImageView
    private lateinit var imagedebt: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_in_ex_main)


        imageEx = findViewById(R.id.imageEx)
        imageIn = findViewById(R.id.imageIn)
        imagedebt= findViewById(R.id.imagedebt)


        imageEx.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        imageIn.setOnClickListener {
            val intent = Intent(this, IncomeMainActivity::class.java)
            startActivity(intent)
        }

        imagedebt.setOnClickListener {
          val intent = Intent(this, PMainActivity::class.java)
            startActivity(intent)
        }

    }
}