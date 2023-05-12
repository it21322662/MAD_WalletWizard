package com.example.walletwizard.Activities

import com.example.walletwizard.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val backgroundImage: ImageView = findViewById(R.id.SplashScreenImage)
        val sideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        backgroundImage.startAnimation(sideAnimation)


        Handler().postDelayed({
            val intent = Intent(this, InExMainActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000) // delaying for 4 seconds...



    }
}