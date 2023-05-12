package com.example.walletwizard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walletwizard.R
import com.example.walletwizard.models.Shoppingcartmodel

class ShCview (private val cartdList:ArrayList<Shoppingcartmodel>):
    RecyclerView.Adapter<ShCview.ViewHolder>() {

    private lateinit var mListner: onItemclickListner

    interface onItemclickListner{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListner: onItemclickListner){
        mListner = clickListner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShCview.ViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_layout,parent,false)
        return ViewHolder(itemView,mListner)
    }

    override fun onBindViewHolder(holder: ShCview.ViewHolder, position: Int) {
        val currentItemName = cartdList[position]
        holder.crvItName.text = currentItemName.itname
        holder.Crvprice.text = currentItemName.price
    }

    override fun getItemCount(): Int {
        return cartdList.size
    }

    class ViewHolder(itemView: View, clickListner: onItemclickListner) : RecyclerView.ViewHolder(itemView) {
        val crvItName : TextView = itemView.findViewById(R.id.Crvitname)
        val Crvprice : TextView = itemView.findViewById(R.id.Crvprice)


        init {
            itemView.setOnClickListener {
                clickListner.onItemClick(adapterPosition)
            }
        }
    }


}