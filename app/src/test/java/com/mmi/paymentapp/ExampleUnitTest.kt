package com.mmi.paymentapp

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import com.mmi.paymentapp.data.local.PaymentAppDatabase
import com.mmi.paymentapp.data.local.PromotionDao
import com.mmi.paymentapp.data.local.TransactionDao
import com.mmi.paymentapp.data.model.Transaction
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.ZoneId
import java.time.ZonedDateTime

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

    private lateinit var transactionDao: TransactionDao
    private lateinit var promotionDao: PromotionDao
    private lateinit var paymentAppDatabase: PaymentAppDatabase

    @Before
    fun setUp() {
        paymentAppDatabase = inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            PaymentAppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        transactionDao = paymentAppDatabase.PaymentAppDao
        promotionDao = paymentAppDatabase.PromotionDao
    }

    @After
    fun tearDown() {
        paymentAppDatabase.close()
    }

    @Test
    fun `paymentDaoTest`() = runTest {
        val zoneId = ZoneId.of("GMT+7")
        val newTransaction = Transaction(
            balance = 1000000F,
            channel = "",
            provider = "",
            paymentInfo = "",
            type = "Topup",
            merchant = "Payment",
            amount = 1000000F,
            createdDate = ZonedDateTime.now(zoneId).toString()
        )

        val transactionList = mutableListOf(newTransaction)
        var latestTransaction : Transaction? = null
        var localTransactionList = mutableListOf<Transaction>()

            transactionDao.InsertTransaction(newTransaction)
        transactionDao.GetLatestTransaction().collect{
            latestTransaction = it
        }
        transactionDao.GetAllTransaction().collect{
            localTransactionList = it as MutableList<Transaction>
        }

        assertEquals(latestTransaction!!,newTransaction)
        assertEquals(localTransactionList,transactionList)
    }
}