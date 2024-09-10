package com.mmi.paymentapp.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val route : String,
    val iconSelected : ImageVector,
    val iconUnSelected : ImageVector,
    val label : String)

data class DetailDataTransaction(
    val trxDate : String,
    val amount : String
)