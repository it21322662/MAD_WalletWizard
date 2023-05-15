package com.example.walletwizard.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.R
import com.example.walletwizard.Activities.Shoppingcart_home

class InExMainActivity : AppCompatActivity() {
    private lateinit var imageEx : ImageView
    private lateinit var imageIn: ImageView
    private lateinit var imagedebt: ImageView
    private lateinit var imageshop: ImageView
    private lateinit var logout : ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_in_ex_main)


        imageEx = findViewById(R.id.imageEx)
        imageIn = findViewById(R.id.imageIn)
        imagedebt= findViewById(R.id.imagedebt)
        imageshop= findViewById(R.id.incomeh)
        logout = findViewById(R.id.logout)



        imageEx.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        imageIn.setOnClickListener {
            val intent = Intent(this, IncomeMainActivity::class.java)
            startActivity(intent)
        }

      imagedebt.setOnClickListener {
          val intent = Intent(this, PMainActivity::class.java )
          startActivity(intent)
      }

        imageshop.setOnClickListener {
            val intent = Intent(this, Shoppingcart_home::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }
}