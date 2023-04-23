package com.example.walletwizard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

@Suppress("DEPRECATION")
class ExpensesManage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove Action Bar/Title Bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        setContentView(R.layout.addexpenses)
    }
}