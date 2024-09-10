package com.mmi.paymentapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mmi.paymentapp.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Upsert
    suspend fun InsertTransaction(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` ORDER BY TransactionId DESC LIMIT 1")
    fun GetLatestTransaction() : Flow<Transaction>

    @Query("SELECT * FROM `Transaction` ORDER BY TransactionId DESC")
    fun GetAllTransaction() : Flow<List<Transaction>>
}