package com.mmi.paymentapp.data.repository

import com.mmi.paymentapp.data.local.TransactionDao
import com.mmi.paymentapp.data.model.Transaction
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) : ITransactionRepository {
    override suspend fun AddTransaction(transaction: Transaction) {
        withContext(IO){
            transactionDao.InsertTransaction(transaction)
        }
    }

    override suspend fun GetAllTransaction(): Flow<List<Transaction>> {
        return withContext(IO) {
            transactionDao.GetAllTransaction()
        }
    }

    override suspend fun GetLatestTransaction(): Flow<Transaction> {
        return  withContext(IO){
            transactionDao.GetLatestTransaction()
        }
    }

}