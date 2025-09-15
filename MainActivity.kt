package com.example.ordersapp

import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {
    private lateinit var db: OrderDatabase
    private lateinit var adapter: OrderAdapter
    private lateinit var totalProfitText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val carNameInput = findViewById<EditText>(R.id.carNameInput)
        val partCostInput = findViewById<EditText>(R.id.partCostInput)
        val clientPriceInput = findViewById<EditText>(R.id.clientPriceInput)
        val addButton = findViewById<Button>(R.id.addButton)
        val exportButton = findViewById<Button>(R.id.exportButton)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        totalProfitText = findViewById(R.id.totalProfit)

        db = OrderDatabase.getDatabase(this)
        adapter = OrderAdapter(listOf(), { order -> editOrder(order) }, { order -> deleteOrder(order) })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            db.orderDao().getAllOrders().collect { orders ->
                adapter.updateData(orders)
                val totalProfit = orders.sumOf { it.profit }
                totalProfitText.text = "Łączny zysk: ${totalProfit} zł"
            }
        }

        addButton.setOnClickListener {
            val carName = carNameInput.text.toString()
            val partCost = partCostInput.text.toString().toDoubleOrNull() ?: 0.0
            val clientPrice = clientPriceInput.text.toString().toDoubleOrNull() ?: 0.0
            val profit = clientPrice - partCost

            val order = Order(carName = carName, partCost = partCost, clientPrice = clientPrice, profit = profit)

            lifecycleScope.launch { db.orderDao().insertOrder(order) }

            carNameInput.text.clear()
            partCostInput.text.clear()
            clientPriceInput.text.clear()
        }

        exportButton.setOnClickListener { exportToCSV() }
    }

    private fun deleteOrder(order: Order) {
        lifecycleScope.launch { db.orderDao().deleteOrder(order) }
    }

    private fun editOrder(order: Order) {
        val dialog = EditOrderDialog(this, order) { updatedOrder ->
            lifecycleScope.launch { db.orderDao().updateOrder(updatedOrder) }
        }
        dialog.show()
    }

    private fun exportToCSV() {
        lifecycleScope.launch {
            val orders = db.orderDao().getAllOrders()
            val file = File(getExternalFilesDir(null), "zlecenia.csv")
            file.printWriter().use { out ->
                out.println("ID,Nazwa auta,Cena części,Cena klienta,Zysk")
                db.orderDao().getAllOrders().collect { list ->
                    list.forEach { order ->
                        out.println("${order.id},${order.carName},${order.partCost},${order.clientPrice},${order.profit}")
                    }
                }
            }
            Toast.makeText(this@MainActivity, "Wyeksportowano do ${file.absolutePath}", Toast.LENGTH_LONG).show()
        }
    }
}
