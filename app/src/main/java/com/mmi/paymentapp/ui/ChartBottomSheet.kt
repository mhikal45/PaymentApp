package com.mmi.paymentapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartBottomSheet() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheet.value = false
        },
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        var isLineChartVisible by remember {
            mutableStateOf(true)
        }
        val primary = colorScheme.primaryContainer
        var monthlyCardColor by remember {
            mutableStateOf(primary)
        }
        var spendingCardColor by remember {
            mutableStateOf(Color.White)
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Card(
                onClick = {
                    isLineChartVisible = true
                },
                colors = CardColors(
                    monthlyCardColor, colorScheme.primary,
                    monthlyCardColor, colorScheme.primary
                ),
                modifier = Modifier
                    .weight(1f, false)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(text = "Bulanan",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
            Card (
                onClick = {
                    isLineChartVisible = false
                },
                colors = CardColors(
                    spendingCardColor, colorScheme.primary,
                    spendingCardColor, colorScheme.primary
                ),
                modifier = Modifier
                    .weight(1f, false)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraSmall
            ){
                Text(text = "Penggunaan",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally))
            }
        }

        if(isLineChartVisible){
            monthlyCardColor = colorScheme.primaryContainer
            spendingCardColor = Color.White
            SingleLineChartWithGridLines()
        }
        else{
            spendingCardColor = colorScheme.primaryContainer
            monthlyCardColor = Color.White
            SimpleDonutChart()
        }
    }
}