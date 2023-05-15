package com.example.walletwizard.Activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.walletwizard.R
import com.example.walletwizard.Models.Shoppingcartmodel
import com.google.firebase.database.FirebaseDatabase

class itemview : AppCompatActivity() {

    private lateinit var vitemname: TextView
    private lateinit var vquantity: TextView
    private lateinit var vprice: TextView
    private lateinit var vdate: TextView
    private lateinit var vtotal: TextView
    private lateinit var btnupdate: Button
    private lateinit var btndelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide() // Hide the action bar

        setContentView(R.layout.activity_itemview)

        val backarrow: ImageButton = findViewById<ImageButton>(R.id.bcarrow)
        backarrow.setOnClickListener {
            val i = Intent(this, Shoppingcart_home::class.java)
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
            deleteRecord(
                intent.getStringExtra("cartId").toString()
            )
        }
    }

    private fun deleteRecord(
        cartId: String
    ) {
        val dbrRef =
            FirebaseDatabase.getInstance().getReference("Shopping Cart").child(cartId)
        val mTask = dbrRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Shopping Cart Deleted", Toast.LENGTH_LONG).show()
            val intent = Intent(this, Shoppingcart_home::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Error: ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        vitemname = findViewById(R.id.vitname)
        vquantity = findViewById(R.id.vquantity)
        vprice = findViewById(R.id.vprice)
        vdate = findViewById(R.id.vdate)
        vtotal = findViewById(R.id.Total)
        btnupdate = findViewById(R.id.updte)
        btndelete = findViewById(R.id.delete)
    }

    private fun setValuesToView() {
        vitemname.text = intent.getStringExtra("itname")
        vquantity.text = intent.getStringExtra("quant")
        vprice.text = intent.getStringExtra("price")
        vdate.text = intent.getStringExtra("date")

        val quantity = intent.getStringExtra("quant")?.toIntOrNull()
        val price = intent.getStringExtra("price")?.toDoubleOrNull()
        if (quantity != null && price != null) {
            val total = quantity * price
            vtotal.text = total.toString()
        } else {
            // Handle the case where parsing fails
            Toast.makeText(this, "Invalid quantity or price", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openUpdateDialog(
        cartId: String,
        itname: String
    ) {
        val mDialog = AlertDialog.Builder(this).create()
        val dialogView = layoutInflater.inflate(R.layout.update_dialog, null)
        mDialog.setView(dialogView)

        val upitname = dialogView.findViewById<EditText>(R.id.uitname)
        val upquantity = dialogView.findViewById<EditText>(R.id.uquantity)       // Rest of the code...

        val upprice = dialogView.findViewById<EditText>(R.id.uprice)
        val update = dialogView.findViewById<EditText>(R.id.udate)
        val btnupdaten = dialogView.findViewById<Button>(R.id.btnupdtedata)

        upitname.setText(intent.getStringExtra("itname").toString())
        upquantity.setText(intent.getStringExtra("quant").toString())
        upprice.setText(intent.getStringExtra("price").toString())
        update.setText(intent.getStringExtra("date").toString())

        mDialog.setTitle("Updating $itname Record")

        btnupdaten.setOnClickListener {
            updateCart(
                cartId,
                upitname.text.toString(),
                upquantity.text.toString(),
                upprice.text.toString(),
                update.text.toString()
            )
            mDialog.dismiss()
        }

        mDialog.show()
    }

    private fun updateCart(
        cartId: String,
        itname: String,
        quant: String,
        price: String,
        date: String
    ) {
        val dbrRef =
            FirebaseDatabase.getInstance().getReference("Shopping Cart").child(cartId)
        val cartinf = Shoppingcartmodel(cartId, itname, quant, price, date)
        dbrRef.setValue(cartinf)
            .addOnSuccessListener {
                Toast.makeText(this, "Shopping cart updated", Toast.LENGTH_LONG).show()

                vitemname.text = itname
                vquantity.text = quant
                vprice.text = price
                vdate.text = date

                val total = quant.toIntOrNull()?.times(price.toDoubleOrNull() ?: 0.0)
                vtotal.text = total?.toString() ?: ""
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Updating Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
    }
}

