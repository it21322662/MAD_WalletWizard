package com.example.walletwizard.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.walletwizard.R
import com.example.walletwizard.Models.Shoppingcartmodel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Addactivity : AppCompatActivity() {

    private lateinit var edtitemname:EditText
    private lateinit var edtquantity:EditText
    private lateinit var edtprice:EditText
    private lateinit var stdate:EditText
    private lateinit var btnadddata:Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window. FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();//This Line hides the action bar

        setContentView(R.layout.activity_addactivity)

        val backarrow: ImageButton = findViewById<ImageButton>(R.id.bcarrow)
        backarrow.setOnClickListener {
            val i = Intent(this, Shoppingcart_home::class.java)
            startActivity(i)
        }

        edtitemname = findViewById(R.id.edtitname)
        edtquantity = findViewById(R.id.quantity)
        edtprice = findViewById(R.id.price)
        stdate = findViewById(R.id.setdate)
        btnadddata = findViewById(R.id.insert_data)

        dbRef = FirebaseDatabase.getInstance().getReference("Shopping Cart")

        btnadddata.setOnClickListener {
            addShoppingcartdata()
        }
    }
    private fun addShoppingcartdata(){

        val itname = edtitemname.text.toString()
        val quantity = edtquantity.text.toString()
        val price = edtprice.text.toString()
        val date = stdate.text.toString()

        if(itname.isEmpty()){
            edtitemname.error = ("Please Enter Item Name")
        }
        else if(quantity.isEmpty()){
            edtquantity.error = ("Please Give Number of Quantity")
        }
        else if(price.isEmpty()){
            edtprice.error = ("Please Enter Price")
        }
        else if(date.isEmpty()){
            stdate.error = ("Please Enter Date")
        }else{
            val cartId = dbRef.push().key!!

            val scart = Shoppingcartmodel(cartId,itname, quantity, price, date)

            dbRef.child(cartId).setValue(scart)
                .addOnCompleteListener{
                    Toast.makeText(this,"Data Add Successfully",Toast.LENGTH_LONG).show()
                    val i = Intent(this,Shoppingcart_home::class.java)
                    startActivity(i)
                }.addOnFailureListener{ err ->
                    Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }


    }
}