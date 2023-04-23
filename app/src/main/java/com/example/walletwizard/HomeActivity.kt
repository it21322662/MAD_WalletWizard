package com.example.walletwizard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()
    }
}