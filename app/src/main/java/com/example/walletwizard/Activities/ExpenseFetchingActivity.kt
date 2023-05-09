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

class ExpenseFetchingActivity : AppCompatActivity() {
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<expensesModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var main : ImageView
    private  lateinit var add : FloatingActionButton
    private  lateinit var expense : TextView
    private lateinit var  income : ImageView
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
        income = findViewById(R.id.imageView8)
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

        income.setOnClickListener {
            val intent = Intent(this, IncomeMainActivity::class.java)
            startActivity(intent)
        }

        empRecyclerView = findViewById(R.id.receive)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.exLoadingData)

        empList = arrayListOf<expensesModel>()

        getExpensesData()


        calculateAll(
            onIncomeSumCalculated = { incomeSum ->
                budget.text = incomeSum.toString()
            },
            onExpenseSumCalculated = { expenseSum ->
                expense.text = expenseSum.toString()
            },
            onBudgetCalculated = { budgetDifference ->
                balance.text = budgetDifference.toString()
            }
        )
    }

    private fun calculateAll(
        onIncomeSumCalculated: (Double) -> Unit,
        onExpenseSumCalculated: (Double) -> Unit,
        onBudgetCalculated: (Double) -> Unit
    ) {
        var incomeSum = 0.0
        var expenseSum = 0.0

        dbRef = FirebaseDatabase.getInstance().getReference("Expenses")
        var incomeQuery = dbRef.orderByChild("expensesAmount").startAt("+")
        incomeQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (incomeSnapshot in snapshot.children) {
                    val amount =
                        incomeSnapshot.child("expensesAmount").getValue(String::class.java)
                    if (amount?.startsWith("+") == true) {
                        incomeSum += amount.substring(1).toDouble()
                    }
                }
                onIncomeSumCalculated(incomeSum)

                dbRef = FirebaseDatabase.getInstance().getReference("Expenses")
                var expenseQuery = dbRef.orderByChild("expensesAmount").startAt("-")
                expenseQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (expenseSnapshot in snapshot.children) {
                            val amount =
                                expenseSnapshot.child("expensesAmount").getValue(String::class.java)
                            if (amount?.startsWith("-") == true) {
                                expenseSum += amount.substring(1).toDouble()
                            }
                        }
                        onExpenseSumCalculated(expenseSum)

                        val budgetDifference = incomeSum - expenseSum
                        onBudgetCalculated(budgetDifference)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle any errors
                        println("error ::::::" + error)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors
                println("error ::::::" + error)
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
                            intent.putExtra("expensesAmt", empList[position].expensesAmount)
                            intent.putExtra("expensesType", empList[position].expensesType)
                            intent.putExtra("expensesNote", empList[position].expensesNote)
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