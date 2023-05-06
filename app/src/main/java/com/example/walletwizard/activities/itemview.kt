package com.example.walletwizard.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walletwizard.R
import com.example.walletwizard.models.Shoppingcartmodel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class itemview : AppCompatActivity() {

    private lateinit var vitemname: TextView
    private lateinit var vquantity: TextView
    private lateinit var vprice: TextView
    private lateinit var vdate: TextView
    private lateinit var btnupdate: Button
    private lateinit var btndelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itemview)

        val backarrow: ImageButton = findViewById<ImageButton>(R.id.bcarrow)
        backarrow.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)

        }
        initView()
        setValuesToView()

        btnupdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("cartId").toString(),
                intent.getStringExtra("itname").toString()
            )
        }
        btndelete.setOnClickListener {
            deletRecord(
                intent.getStringExtra("cartId").toString()
            )
        }
    }

    private fun deletRecord(
        cartId: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Shopping Cart").child(cartId)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this,"Shopping Cart Delete", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this,"Deleting Err ${error.message}",Toast.LENGTH_LONG).show()
        }
    }

    private fun initView(){
        vitemname = findViewById(R.id.vitname)
        vquantity = findViewById(R.id.vquantity)
        vprice = findViewById(R.id.vprice)
        vdate = findViewById(R.id.vdate)
        btnupdate = findViewById(R.id.updte)
        btndelete = findViewById(R.id.delete)
    }
    private fun setValuesToView(){
        vitemname.text = intent.getStringExtra("itname")
        vquantity.text = intent.getStringExtra("quant")
        vprice.text = intent.getStringExtra("price")
        vdate.text = intent.getStringExtra("date")
    }
    private fun openUpdateDialog(
        cartId:String,
        itname: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialgView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialgView)


        val uitname = mDialgView.findViewById<EditText>(R.id.uitname)
        val uquantity = mDialgView.findViewById<EditText>(R.id.uquantity)
        val uprice = mDialgView.findViewById<EditText>(R.id.uprice)
        val udate = mDialgView.findViewById<EditText>(R.id.udate)
        val btnupdaten = mDialgView.findViewById<Button>(R.id.btnupdtedata)

        uitname.setText(intent.getStringExtra("itname").toString())
        uquantity.setText(intent.getStringExtra("quant").toString())
        uprice.setText(intent.getStringExtra("price").toString())
        udate.setText(intent.getStringExtra("date").toString())

        mDialog.setTitle("Updating $itname Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnupdaten.setOnClickListener {
            updatecart(
                cartId,
                uitname.text.toString(),
                uquantity.text.toString(),
                uprice.text.toString(),
                udate.text.toString()
            )
        }

        Toast.makeText(applicationContext,"Shopping cart updated", Toast.LENGTH_LONG).show()

        vitemname.text = uitname.text.toString()
        vquantity.text = uquantity.text.toString()
        vprice.text = uprice.text.toString()
        vdate.text = udate.text.toString()

        alertDialog.dismiss()
    }

    private fun updatecart(
        cartId: String,
        itname: String,
        quant: String,
        price: String,
        date: String
    ){
        val dbref = FirebaseDatabase.getInstance().getReference("Shopping Cart").child(cartId)
        val cartinf = Shoppingcartmodel(itname,quant, price, date)
        dbref.setValue(cartinf)
    }
}