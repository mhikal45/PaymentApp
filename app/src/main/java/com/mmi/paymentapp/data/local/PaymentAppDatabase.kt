package com.mmi.paymentapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mmi.paymentapp.data.model.Promotion
import com.mmi.paymentapp.data.model.Transaction

@Database(entities = [Transaction::class,Promotion::class], version = 1)
abstract class PaymentAppDatabase : RoomDatabase() {
    abstract val PaymentAppDao : TransactionDao
    abstract val PromotionDao : PromotionDao
}