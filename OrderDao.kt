package com.example.ordersapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert suspend fun insertOrder(order: Order)
    @Update suspend fun updateOrder(order: Order)
    @Delete suspend fun deleteOrder(order: Order)

    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): Flow<List<Order>>
}
