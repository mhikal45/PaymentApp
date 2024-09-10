package com.mmi.paymentapp.data.repository

import com.mmi.paymentapp.data.model.Transaction
import kotlinx.coroutines.flow.Flow

interface ITransactionRepository {
    suspend fun AddTransaction (transaction: Transaction)
    suspend fun GetAllTransaction() : Flow<List<Transaction>>
    suspend fun GetLatestTransaction() : Flow<Transaction>
}