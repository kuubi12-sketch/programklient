package com.example.ordersapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val carName: String,
    val partCost: Double,
    val clientPrice: Double,
    val profit: Double
)
