package com.example.ordersapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class EditOrderDialog(context: Context, private val order: Order, private val onSave: (Order) -> Unit) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_order)

        val carNameInput = findViewById<EditText>(R.id.editCarName)
        val partCostInput = findViewById<EditText>(R.id.editPartCost)
        val clientPriceInput = findViewById<EditText>(R.id.editClientPrice)
        val saveButton = findViewById<Button>(R.id.saveButton)

        carNameInput.setText(order.carName)
        partCostInput.setText(order.partCost.toString())
        clientPriceInput.setText(order.clientPrice.toString())

        saveButton.setOnClickListener {
            val updated = order.copy(
                carName = carNameInput.text.toString(),
                partCost = partCostInput.text.toString().toDoubleOrNull() ?: 0.0,
                clientPrice = clientPriceInput.text.toString().toDoubleOrNull() ?: 0.0
            )
            onSave(updated.copy(profit = updated.clientPrice - updated.partCost))
            dismiss()
        }
    }
}
