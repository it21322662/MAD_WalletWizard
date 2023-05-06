package com.example.walletwizard.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walletwizard.R
import com.example.walletwizard.adapters.ShCview
import com.example.walletwizard.models.Shoppingcartmodel
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var rcvcartview: RecyclerView
    //private lateinit var loadingview: TextView
    private lateinit var shCartList:ArrayList<Shoppingcartmodel>
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addCart: ImageButton = findViewById<ImageButton>(R.id.addCart)
        addCart.setOnClickListener {
            val i = Intent(this, Addactivity::class.java)
            startActivity(i)
       }


        rcvcartview = findViewById(R.id.rvitname)
        rcvcartview.layoutManager = LinearLayoutManager(this)
        rcvcartview.setHasFixedSize(true)
        //loadingview = findViewById(R.id.tvLoadingData)

        shCartList = arrayListOf<Shoppingcartmodel>()

        getshCartdata()

    }

    private fun getshCartdata() {
        rcvcartview.visibility = View.GONE
        //loadingview.visibility = View.VISIBLE

        dbref = FirebaseDatabase.getInstance().getReference("Shopping Cart")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shCartList.clear()
                if (snapshot.exists()) {
                    for (itSnap in snapshot.children) {
                        val scartData= itSnap.getValue(Shoppingcartmodel::class.java)
                        shCartList.add(scartData!!)
                    }
                    val mAdapter = ShCview(shCartList)
                    rcvcartview.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ShCview.onItemclickListner{

                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@MainActivity, itemview::class.java)

                            intent.putExtra("itname", shCartList[position].itname)
                            intent.putExtra("quant", shCartList[position].quant)
                            intent.putExtra("price", shCartList[position].price)
                            intent.putExtra("date", shCartList[position].date)
                            startActivity(intent)
                        }

                    })

                    rcvcartview.visibility = View.VISIBLE
                    //loadingview.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
