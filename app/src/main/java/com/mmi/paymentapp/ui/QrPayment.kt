package com.mmi.paymentapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.mmi.paymentapp.R
import com.mmi.paymentapp.data.model.Transaction
import com.mmi.paymentapp.data.viewmodel.TransactionViewModel
import kotlinx.coroutines.delay
import java.time.ZoneId
import java.time.ZonedDateTime

var QrString : String? = null
var Transaction : Transaction? = null

@SuppressLint("SimpleDateFormat")
fun MakeTransaction() {
    val qrStringData = QrString?.split('.')
    val zoneId = ZoneId.of("GMT+7")

    if (qrStringData!!.isNotEmpty()){
        Transaction = Transaction(
            balance = Balance!! - qrStringData[3].toFloat(),
            channel = "QRIS",
            provider = qrStringData[0],
            paymentInfo = qrStringData[1],
            type = "Payment",
            merchant = qrStringData[2],
            amount = qrStringData[3].toFloat(),
            createdDate = ZonedDateTime.now(zoneId).toString()
            )
    }
}

@Composable
fun ProcessQrisPayment() {
    var showDialog by remember { mutableStateOf(true) }
    var loadingVisible by remember { mutableStateOf(true) }
    val viewModel = hiltViewModel<TransactionViewModel>()
    LaunchedEffect(key1 = true, block = {
        viewModel.AddTransaction(Transaction!!)
        delay(2000)
        loadingVisible = false
    })
    if(showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            content = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(White, shape = RoundedCornerShape(8.dp))
                    ) {
                        if (loadingVisible) {
                            CircularProgressIndicator()
                        } else {
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.ic_success),
                                    contentDescription = "Success",
                                    modifier = Modifier
                                        .size(100.dp)
                                )
                                Text(text = "Pembayaran Berhasil")
                            }
                            LaunchedEffect(key1 = true, block = {
                                delay(2000)
                                showDialog = false
                                isPaymentComplete.value = true
                            })
                        }
                    }
                }
            }
        )
    }
}