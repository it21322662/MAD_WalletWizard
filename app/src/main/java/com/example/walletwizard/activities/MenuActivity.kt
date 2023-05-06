package com.example.walletwizard.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.walletwizard.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navig)

        bNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.expens -> {
                    // Do something when expenses item is clicked
                    true
                }
                R.id.additem -> {
                    val r = Intent(this, Edit::class.java)
                    startActivity(r)
                    true
                }
                R.id.home2 -> {
                    // Do something when home item is clicked
                    true
                }
                R.id.money2 -> {
                    // Do something when money item is clicked
                    true
                }
                R.id.moneyd -> {
                    // Do something when moneyd item is clicked
                    true
                }
                else -> false
            }
        }
    }
}