package com.example.ordersapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(
    private var orders: List<Order>,
    private val onEdit: (Order) -> Unit,
    private val onDelete: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carName: TextView = view.findViewById(R.id.carName)
        val prices: TextView = view.findViewById(R.id.prices)
        val profit: TextView = view.findViewById(R.id.profit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.carName.text = order.carName
        holder.prices.text = "Części: ${order.partCost} zł | Klient: ${order.clientPrice} zł"
        holder.profit.text = "Zysk: ${order.profit} zł"

        holder.itemView.setOnClickListener { onEdit(order) }
        holder.itemView.setOnLongClickListener {
            onDelete(order)
            true
        }
    }

    override fun getItemCount() = orders.size

    fun updateData(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
