package com.mmi.paymentapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmi.paymentapp.data.model.Transaction
import com.mmi.paymentapp.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    val latestTransaction = MutableStateFlow<Transaction?>(null)
    val _allTransactions = MutableStateFlow(emptyList<Transaction>())
    val allTransactions = _allTransactions.asStateFlow()

    suspend fun GetLatestTransaction() {
        viewModelScope.launch(IO) {
            transactionRepository.GetLatestTransaction().collectLatest {
                latestTransaction.tryEmit(it)
            }
        }
    }

    fun GetAllTransactions() {
        viewModelScope.launch(IO) {
            transactionRepository.GetAllTransaction().collectLatest {
                _allTransactions.tryEmit(it)
            }
        }
    }

    fun AddTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.AddTransaction(transaction)
        }
    }
}