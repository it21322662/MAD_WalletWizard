package com.example.walletwizard.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walletwizard.Adapters.PerAdapter
import com.example.walletwizard.Models.PersonModel
import com.example.walletwizard.R
import com.example.walletwizard.activities.PersonDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchingActivity : AppCompatActivity() {

    private lateinit var pRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var personList: ArrayList<PersonModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_fetching)

        pRecyclerView = findViewById(R.id.rvPer)
        pRecyclerView.layoutManager = LinearLayoutManager(this)
        pRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        personList = arrayListOf<PersonModel>()

        getPersonData()

    }

    private fun getPersonData() {

        pRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Persons")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                personList.clear()
                if (snapshot.exists()) {
                    for (perSnap in snapshot.children) {
                        val perData = perSnap.getValue(PersonModel::class.java)
                        personList.add(perData!!)
                    }
                    val pAdapter = PerAdapter(personList)
                    pRecyclerView.adapter = pAdapter

                    pAdapter.setOnItemClickListener(object : PerAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, PersonDetails::class.java)

                            //put extras
                            intent.putExtra("tvPid",personList[position].pid)
                            intent.putExtra("tvPName", personList[position].pName)
                            intent.putExtra("tvBankLoan", personList[position].pBankloans)
                            intent.putExtra("tvPersonalLoan", personList[position].pPerLoan)
                            intent.putExtra("tvLeasing", personList[position].pLeasing)

                            startActivity(intent)
                        }

                    })

                    pRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}