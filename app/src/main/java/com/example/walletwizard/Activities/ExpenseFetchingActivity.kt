package com.example.walletwizard.Activities

import android.annotation.SuppressLint
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
import com.example.walletwizard.Adapters.ExpAdapter
import com.example.walletwizard.Models.expensesModel
import com.example.walletwizard.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import kotlin.math.exp

class ExpenseFetchingActivity : AppCompatActivity() {
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<expensesModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var main : ImageView
    private  lateinit var add : FloatingActionButton
    private  lateinit var expense : TextView
    private  lateinit var budget : TextView
    private lateinit var balance : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_expense)

        main = findViewById(R.id.imageView9)
        add = findViewById(R.id.fab)
        expense =findViewById(R.id.expense)
        budget = findViewById(R.id.budget)
        balance = findViewById(R.id.balance)

        main.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        add.setOnClickListener {
            val intent = Intent(this, AddExpenses::class.java)
            startActivity(intent)
        }

        empRecyclerView = findViewById(R.id.receive)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.exLoadingData)

        empList = arrayListOf<expensesModel>()

        getExpensesData()



        var budgetValue =0.0;
        var expenses = 0.0;

        var total =0.0;

        calculateExpenses { exp ->
            run {
                expense.text = exp.toString();
                expenses = exp;
            }
        }
        calculateBudget { budg -> run {
            budget.text = budg.toString();
            budgetValue = budg;
        } }
        println("budgetValue :: :::" + budgetValue + "expensesVal :::::"+ expenses)
        balance.text = (budgetValue - expenses).toString()

    }

    private fun calculateBudget(callback: (Double) -> Unit) {
        var sum = 0.0

        dbRef = FirebaseDatabase.getInstance().getReference("Expenses")
        var expenseQuery = dbRef.orderByChild("expensesAmount").startAt("-")
        expenseQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Loop through the data and add the amounts with prefix "-"
                for (expenseSnapshot in snapshot.children) {
                    val amount =
                        expenseSnapshot.child("expensesAmount").getValue(String::class.java)
                    if (amount?.startsWith("-") == true) {
                        sum += amount.substring(1).toDouble()
                    }
                }
                // Todo:
                println("Sum of expenses with prefix \"-\": $sum")

                expense.text = sum.toString()
                callback(sum)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors
                println("error ::::::" + error)
            }
        })
    }
        private fun calculateExpenses(callback: (Double) -> Unit) {
            var sum = 0.0

            dbRef = FirebaseDatabase.getInstance().getReference("Expenses")
            var expenseQuery = dbRef.orderByChild("expensesAmount").startAt("+")
            expenseQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Loop through the data and add the amounts with prefix "+"
                    for (expenseSnapshot in snapshot.children) {
                        val amount = expenseSnapshot.child("expensesAmount").getValue(String::class.java)
                        if (amount?.startsWith("+") == true) {

                            sum += amount.substring(1).toDouble()
                        }
                    }
                    // Todo:
                    println("Sum of expenses with prefix \"+\": $sum")

                    budget.text = sum.toString()
                    callback(sum)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors
                    println("error ::::::"+error)
                }
            })
    }

    private fun getExpensesData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Expenses")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val expenseId = empSnap.key
                        val empData = empSnap.getValue(expensesModel::class.java)
                        println("expenseId : --------------------"+ expenseId+ " "+ empData);
                        empList.add(empData!!)
                    }
                    val mAdapter = ExpAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ExpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            println(position)
                            val intent = Intent(this@ExpenseFetchingActivity, ExpensesDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("expensesId", empList[position].expensesId)
                            intent.putExtra("expensesType", empList[position].expensesType)
                            intent.putExtra("expensesNote", empList[position].expensesNote)
                            intent.putExtra("expensesAmt", empList[position].expensesAmount)
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