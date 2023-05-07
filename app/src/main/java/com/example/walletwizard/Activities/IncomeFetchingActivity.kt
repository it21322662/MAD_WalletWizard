package com.example.walletwizard.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walletwizard.Adapters.IncAdapter
import com.example.walletwizard.Models.incomeModel
import com.example.walletwizard.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class IncomeFetchingActivity : AppCompatActivity() {
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<incomeModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var main : ImageView
    private lateinit var  mainincome : ImageView
    private  lateinit var add : FloatingActionButton
    private  lateinit var incomecal : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_rvincome)

        main = findViewById(R.id.imageView9)
        add = findViewById(R.id.fab)
        mainincome = findViewById(R.id.imageView8)
        incomecal = findViewById(R.id.income)

        main.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        add.setOnClickListener {
            val intent = Intent(this, AddIncome::class.java)
            startActivity(intent)
        }

        mainincome.setOnClickListener {
            val intent = Intent(this, IncomeMainActivity::class.java)
            startActivity(intent)
        }

        empRecyclerView = findViewById(R.id.receive)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.exLoadingData)

        empList = arrayListOf<incomeModel>()

        getincomeData()

        var income = 0.0;

        calculateIncome(
            onTotalIncomeCalculated = { incomeSum -> incomecal.text = incomeSum.toString()}
        )
    }

    private fun calculateIncome(onTotalIncomeCalculated: (Double) -> Unit) {
        var sum = 0.0

        dbRef = FirebaseDatabase.getInstance().getReference("Income")
        var incomeQuery = dbRef.orderByChild("incomeAmount")
        incomeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Loop through the data and add the amounts with prefix "+"
                for (incomeSnapshot in snapshot.children) {
                    val amount = incomeSnapshot.child("incomeAmount").getValue(String::class.java)
                    if (amount !== null) {
                        sum += amount.toDouble()
                    }
                }
                // Todo:
                println("Sum of income with prefix \"+\": $sum")
                onTotalIncomeCalculated(sum)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors
                println("error ::::::"+error)
            }
        })
    }

    private fun getincomeData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Income")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val incomeId = empSnap.key
                        val empData = empSnap.getValue(incomeModel::class.java)
                        println("incomeId : --------------------"+ incomeId+ " "+ empData);
                        empList.add(empData!!)
                    }
                    val mAdapter = IncAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : IncAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            println(position)
                            val intent = Intent(this@IncomeFetchingActivity, IncomeDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("incomeId", empList[position].incomeId)
                            intent.putExtra("incomeType", empList[position].incomeType)
                            intent.putExtra("incomeNote", empList[position].incomeNote)
                            intent.putExtra("incomeAmt", empList[position].incomeAmount)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}