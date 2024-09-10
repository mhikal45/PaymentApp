@file:OptIn(ExperimentalMaterial3Api::class)

package com.mmi.paymentapp.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mmi.paymentapp.R
import com.mmi.paymentapp.data.model.Transaction
import com.mmi.paymentapp.data.viewmodel.PromotionViewModel
import com.mmi.paymentapp.data.viewmodel.TransactionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.ZonedDateTime

var showBottomSheet = mutableStateOf(false)
var ShowHistoryBottomsheet = mutableStateOf(false)
var Balance : Float? = null

@Composable
fun HomeTabView(transactioniewModel: TransactionViewModel, promotionViewModel: PromotionViewModel) {
    isPaymentComplete.value = false
    var balance by remember {
        mutableStateOf("Loading...")
    }

    var showPromotionBottomsheet by remember {
        mutableStateOf(false)
    }
    var showHistoryBottomsheet by remember {
        mutableStateOf(false)
    }
    var showGraphBottomsheet by remember {
        mutableStateOf(false)
    }

    var showLoadingState by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(true) {
        withContext(Dispatchers.IO){
            if(showLoadingState) {
                transactioniewModel.GetAllTransactions()
                promotionViewModel.GetAllPromotions()
            }
            transactioniewModel.GetLatestTransaction()
            delay(500)
            Balance = GetCurrentBalance(transactioniewModel)
            balance = Balance.toString()
            showLoadingState = false
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 0.dp),
            colors = CardColors(
                colorScheme.primaryContainer, colorScheme.primary,
                colorScheme.inversePrimary, colorScheme.inverseSurface
            ),
            shape = MaterialTheme.shapes.medium,
            onClick = {ShowHistoryBottomsheet.value = true}
        ) {
            Text(
                text = "Saldo Anda Saat Ini",
                style = TextStyle(
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 8.dp, 0.dp, 0.dp)
            )
            Text(
                text = "Rp. $balance",
                style = TextStyle(
                    fontSize = 25.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 16.dp, 0.dp, 0.dp)
            )

            Text(modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val strokeWidth = 1 * density
                    val y = size.height - strokeWidth / 2

                    drawLine(
                        Color.Gray,
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                },
                text = ""
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 16.dp, 0.dp),
                colors = CardColors(
                    colorScheme.primaryContainer, colorScheme.primary,
                    colorScheme.inversePrimary, colorScheme.inverseSurface
                ),
                shape = MaterialTheme.shapes.medium,
                onClick = {showHistoryBottomsheet = true}
            ) {
                Row {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow),
                        contentDescription = "open transaction history",
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .align(Alignment.CenterVertically)
                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    )
                    Text(
                        text = "Riwayat Transaksi",
                        style = TextStyle(
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
                if(showHistoryBottomsheet){
                    val historySheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true,
                    )
                    ModalBottomSheet(
                        onDismissRequest = {
                            showHistoryBottomsheet = false
                        },
                        sheetState = historySheetState,
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        if (showLoadingState) {
                            CircularProgressIndicator(
                                modifier = Modifier.width(64.dp),
                                color = colorScheme.primary,
                                trackColor = colorScheme.primaryContainer,
                            )
                        }
                        TransactionHistory(transactioniewModel)
                    }
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 0.dp),
            colors = CardColors(
                colorScheme.primaryContainer, colorScheme.primary,
                colorScheme.inversePrimary, colorScheme.inverseSurface
            ),
            shape = MaterialTheme.shapes.medium,
            onClick = { showPromotionBottomsheet = true}
        ) {
            Row {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow),
                    contentDescription = "go to promotion",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                )
                Text(
                    text = "Cek Promo Anda Disini",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            if(showPromotionBottomsheet){
                val historySheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                )
                ModalBottomSheet(
                    onDismissRequest = {
                        showPromotionBottomsheet = false
                    },
                    sheetState = historySheetState,
                    modifier = Modifier
                        .fillMaxHeight(),
                ) {
                    if (showLoadingState) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp),
                            color = colorScheme.primary,
                            trackColor = colorScheme.primaryContainer,
                        )
                    }
                    PromotionList(promotionViewModel)
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 0.dp),
            colors = CardColors(
                colorScheme.primaryContainer, colorScheme.primary,
                colorScheme.inversePrimary, colorScheme.inverseSurface
            ),
            shape = MaterialTheme.shapes.medium,
            onClick = {showGraphBottomsheet = true}
        ) {
            Row {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow),
                    contentDescription = "go to Spending",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                )
                Text(
                    text = "Cek Pengeluaran Anda",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            if (showGraphBottomsheet) {
                ChartBottomSheet()
            }
        }
    }
}

fun GetCurrentBalance(viewModel: TransactionViewModel) : Float {
    val balance : Float
    val latestTransaction : Transaction? = viewModel.latestTransaction.value

    if (latestTransaction == null){
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
        Log.i("Get Current Balance", "No Latest Transaction")
        viewModel.AddTransaction(transaction = newTransaction)
        balance = newTransaction.balance

    }
    else{
        balance = latestTransaction.balance
    }

    return  balance
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TransactionHistory(viewModel: TransactionViewModel){
    val historyTransactions = viewModel.allTransactions.collectAsStateWithLifecycle().value

    Column {
        if (historyTransactions.isNotEmpty()){
            Log.i("History Transaction", "not empty " + historyTransactions.count())
            LazyColumn {
                items(historyTransactions){historyTransaction ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 16.dp, 16.dp, 0.dp),
                        colors = CardColors(
                            colorScheme.primaryContainer, colorScheme.primary,
                            colorScheme.inversePrimary, colorScheme.inverseSurface
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "${historyTransaction.type} - ${historyTransaction.merchant}" ,
                            style = TextStyle(
                                fontSize = 13.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp, 16.dp, 0.dp, 0.dp)
                        )
                        Text(
                            text = "Rp. ${historyTransaction.amount}",
                            style = TextStyle(
                                fontSize = 25.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp, 16.dp, 0.dp, 16.dp)
                        )
                    }
                }
            }
        }
        else{
            Log.i("History Transaction", "empty")
            Text(text = "No Transaction Recorded")
        }
    }
}

@Composable
fun PromotionList(viewModel: PromotionViewModel) {
    val allPromotions = viewModel.allPromotions.collectAsStateWithLifecycle().value

    Column {
        if (allPromotions.isNotEmpty()){
            Log.i("History Transaction", "not empty " + allPromotions.count())
            LazyColumn {
                items(allPromotions){promotion ->
                    var promoDesc = promotion.desc

                    var showDetailPromotion by remember {
                        mutableStateOf(false)
                    }

                    var iconToShow by remember {
                        mutableIntStateOf(
                            0
                        )
                    }

                    if(!showDetailPromotion) {
                        if (promoDesc?.length!! > 200) {
                            promoDesc = buildString {
                                append(promoDesc!!.removeRange(201..(promoDesc!!.length -1)))
                                append("...")
                            }
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 16.dp, 16.dp, 0.dp),
                        colors = CardColors(
                            colorScheme.primaryContainer, colorScheme.primary,
                            colorScheme.inversePrimary, colorScheme.inverseSurface
                        ),
                        shape = MaterialTheme.shapes.medium,
                        onClick = {showDetailPromotion = !showDetailPromotion}
                    ) {
                        Text(
                            text = "${promotion.title}" ,
                            style = TextStyle(
                                fontSize = 24.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp, 16.dp, 0.dp, 0.dp)
                        )
                        Text(
                            text = "$promoDesc",
                            style = TextStyle(
                                fontSize = 14.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp, 16.dp, 24.dp, 8.dp)
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    val strokeWidth = 1 * density
                                    val y = size.height - strokeWidth / 2
                                    drawLine(
                                        Color.Gray,
                                        Offset(0f, y),
                                        Offset(size.width, y),
                                        strokeWidth
                                    )
                                },
                            text = ""
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 8.dp, 16.dp, 0.dp),
                            colors = CardColors(
                                colorScheme.primaryContainer, colorScheme.primary,
                                colorScheme.inversePrimary, colorScheme.inverseSurface
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row {
                                if(showDetailPromotion) {
                                    iconToShow = R.drawable.ic_arrow_down
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = iconToShow),
                                        contentDescription = "open transaction history",
                                        modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp)
                                            .align(Alignment.CenterVertically)
                                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                                    )
                                    Text(
                                        text = "Sembunyikan Detail Promo",
                                        style = TextStyle(
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                                else{
                                    iconToShow = R.drawable.ic_arrow
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = iconToShow),
                                        contentDescription = "open transaction history",
                                        modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp)
                                            .align(Alignment.CenterVertically)
                                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                                    )
                                    Text(
                                        text = "Tampilkan Detail Promo",
                                        style = TextStyle(
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp, 0.dp, 0.dp, 0.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                            }
                            if(showDetailPromotion){
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                        ) {
                                            Row (
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Dibuat",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                                Text(
                                                    text = promotion.createdAt!!.replace('T',' ').trim('Z'),
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                            }
                                            Row (
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Nama",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                                Text(
                                                    text = "${promotion.nama}",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                            }
                                            Row (
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            )  {
                                                Text(
                                                    text = "Lokasi",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                                Text(
                                                    text = "${promotion.lokasi}",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                            }
                                            Row (
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            )  {
                                                Text(
                                                    text = "Dipakai",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                                Text(
                                                    text = "${promotion.count}",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                            }
                                        }
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                        ) {
                                            Row (
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            )  {
                                                Text(
                                                    text = "Alternatif (Jumlah)",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                                Text(
                                                    text = "${promotion.alt}",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                            }
                                            Row (
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Longitude",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                                Text(
                                                    text = "${promotion.longitude}",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                            }
                                            Row (
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Latitude",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                                Text(
                                                    text = "${promotion.latitude}",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                            }
                                            Row (
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            )  {
                                                Text(
                                                    text = "Diperbarui",
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                                Text(
                                                    text = promotion.updatedAt!!.replace('T',' ').trim('Z'),
                                                    style = TextStyle(
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier
                                                        .padding(24.dp, 16.dp, 0.dp, 16.dp)
                                                )
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else{
            Text(text = "No Promotion Recorded")
        }
    }
}