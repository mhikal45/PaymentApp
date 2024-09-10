@file:SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

package com.mmi.paymentapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mmi.paymentapp.data.viewmodel.PromotionViewModel
import com.mmi.paymentapp.data.viewmodel.TransactionViewModel
import com.mmi.paymentapp.ui.BottomNavigationView
import com.mmi.paymentapp.ui.theme.PaymentAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val transactionViewModel = hiltViewModel<TransactionViewModel>()
            val promotionViewModel = hiltViewModel<PromotionViewModel>()
            PaymentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background
                ) {
                    BottomNavigationView(transactionViewModel,promotionViewModel)
                }
            }
        }
    }
}

/*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val viewModel = hiltViewModel<PaymentAppViewModel>()
    PaymentAppTheme {
        BottomNavigationView(viewModel)
    }
}*/
