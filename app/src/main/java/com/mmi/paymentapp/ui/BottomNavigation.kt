package com.mmi.paymentapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mmi.paymentapp.R
import com.mmi.paymentapp.data.model.BottomNavigationItem
import com.mmi.paymentapp.data.viewmodel.PromotionViewModel
import com.mmi.paymentapp.data.viewmodel.TransactionViewModel

var isPaymentComplete = mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavigationView(transactionViewModel: TransactionViewModel, promotionViewModel: PromotionViewModel){
    val homeTab = BottomNavigationItem("home",
        ImageVector.vectorResource(id = R.drawable.ic_home),
        ImageVector.vectorResource(id = R.drawable.ic_home),
        "Home")
    val paymentTab = BottomNavigationItem("payment",
        ImageVector.vectorResource(id = R.drawable.ic_qr),
        ImageVector.vectorResource(id = R.drawable.ic_qr),
        "Payment")

    val bottomNavItem = listOf(homeTab,paymentTab)
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Simple Payment App")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController, bottomNavItem) }) {
        NavHost(
            navController = navController,
            startDestination = homeTab.route,
            Modifier.padding(0.dp,80.dp)
        ) {
            composable(homeTab.route) {
                HomeTabView(transactionViewModel,promotionViewModel)
            }
            composable(paymentTab.route) {
                PaymentTabView()
            }
        }
    }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) {selectedIcon} else {unselectedIcon},
            contentDescription = title
        )
    }
}

@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}

@Composable
fun BottomNavigationBar (navController: NavController, bottomNavItem : List<BottomNavigationItem>)
{
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    NavigationBar {
        bottomNavItem.forEachIndexed{
                index, bottomNavigationItem ->
            NavigationBarItem(selected = selectedTabIndex == index ,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(bottomNavigationItem.route)
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = bottomNavigationItem.iconSelected,
                        unselectedIcon = bottomNavigationItem.iconUnSelected,
                        title = bottomNavigationItem.label
                    )
                },
                label = {
                    bottomNavigationItem.label
                },
            )
        }
        if(isPaymentComplete.value){
            selectedTabIndex = 0
            navController.navigate(bottomNavItem.first().route) {
                popUpTo(bottomNavItem.first().route){
                    saveState = true
                    inclusive = false
                }
                restoreState = false
                launchSingleTop = true
            }
        }
    }
}