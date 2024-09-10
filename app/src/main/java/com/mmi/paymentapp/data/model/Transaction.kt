package com.mmi.paymentapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "TransactionId")
    val transactionId : Int = 0,
    @ColumnInfo(name = "PaymentInfo")
    val paymentInfo : String ="",
    @ColumnInfo(name = "Balance")
    val balance : Float = 0f,
    @ColumnInfo(name = "Amount")
    val amount : Float = 0f,
    @ColumnInfo(name = "Type")
    val type : String = "",
    @ColumnInfo(name = "Channel")
    val channel : String = "",
    @ColumnInfo(name = "Provider")
    val provider : String = "",
    @ColumnInfo(name = "Merchant")
    val merchant : String = "",
    @ColumnInfo(name = "CreatedDate")
    val createdDate : String = "",
)