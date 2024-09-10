package com.mmi.paymentapp.ui

import android.graphics.Typeface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.mmi.paymentapp.data.model.DetailDataTransaction


@Composable
fun SingleLineChartWithGridLines() {
    val pointsData: List<Point> =
        listOf(
            Point(1f, 3f),
            Point(2f, 7f),
            Point(3f, 8f),
            Point(4f, 10f),
            Point(5f, 5f),
            Point(6f, 10f),
            Point(7f, 1f),
            Point(8f, 3f),
            Point(9f, 5f),
            Point(10f, 10f),
            Point(11f, 7f),
            Point(12f, 7f),
        )

    val detailDataLinear = listOf(
        DetailDataTransaction("Januari","Rp. 3,000,000"),
        DetailDataTransaction("Februari","Rp. 7,000,000"),
        DetailDataTransaction("Maret","Rp. 8,000,000"),
        DetailDataTransaction("April","Rp. 10,000,000"),
        DetailDataTransaction("Mei","Rp. 5,000,000"),
        DetailDataTransaction("Juni","Rp. 10,000,000"),
        DetailDataTransaction("Juli","Rp. 1,000,000"),
        DetailDataTransaction("Agustus","Rp. 3,000,000"),
        DetailDataTransaction("September","Rp. 5,000,000"),
        DetailDataTransaction("Oktober","Rp. 10,000,000"),
        DetailDataTransaction("November","Rp. 7,000,000"),
        DetailDataTransaction("Desember","Rp. 7,000,000"),
    )

    val steps = 5
    val xAxisData = AxisData.Builder()
        .axisStepSize(60.dp)
        .backgroundColor(Color.White)
        .steps(pointsData.size - 1)
        .labelData { i -> (i+1).toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .axisStepSize(10.dp)
        .steps(steps)
        .backgroundColor(Color.White)
        .labelData { i ->
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()

    val lineChart = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )

    Column(
        modifier = Modifier
            .padding(24.dp, 0.dp, 16.dp, 0.dp)
            .fillMaxSize()
    ) {
        LineChart(
            modifier = Modifier
                .height(300.dp),
            lineChartData = lineChart
        )
        Text(text = "Detail Pengeluaran Perbulan")
        DataDetailTransaction(detailTransactions = detailDataLinear)
    }
}

@Composable
fun SimpleDonutChart() {
    val dataChart = PieChartData(
        slices = listOf(
            PieChartData.Slice("Tarik Tunai", 55f, Color(0xFF5F0A87)),
            PieChartData.Slice("QRIS Payment", 31f, Color(0xFF20BF55)),
            PieChartData.Slice("Topup Gopay", 7.7f, Color(0xFFA40606)),
            PieChartData.Slice("Lainnya", 6.3f, Color(0xFFF53844)),
        ),
        plotType = PlotType.Donut
    )
    val detailDataCash = listOf(
        DetailDataTransaction("21/01/2023","Rp. 1,000,000"),
        DetailDataTransaction("20/01/2023","Rp. 500,000"),
        DetailDataTransaction("19/01/2023","Rp. 1,000,000"),
    )
    val detailDataQris = listOf(
        DetailDataTransaction("21/01/2023","Rp. 159,000"),
        DetailDataTransaction("20/01/2023","Rp. 35,000"),
        DetailDataTransaction("19/01/2023","Rp. 1500"),
    )
    val detailDataGopay = listOf(
        DetailDataTransaction("21/01/2023","Rp. 200,000"),
        DetailDataTransaction("20/01/2023","Rp. 195,000"),
        DetailDataTransaction("19/01/2023","Rp. 5,000,000"),
    )
    val detailDataOther = listOf(
        DetailDataTransaction("21/01/2023","Rp. 1,000,000"),
        DetailDataTransaction("20/01/2023","Rp. 500,000"),
        DetailDataTransaction("19/01/2023","Rp. 1,000,000"),
    )

    var detailCashVisible by remember {
        mutableStateOf(false)
    }

    var detailQrisVisible by remember {
        mutableStateOf(false)
    }

    var detailGopayVisible by remember {
        mutableStateOf(false)
    }

    var detailOtherVisible by remember {
        mutableStateOf(false)
    }

    val pieChartConfig =
        PieChartConfig(
            labelVisible = true,
            strokeWidth = 120f,
            labelColor = Color.Black,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
            isAnimationEnable = true,
            chartPadding = 50,
            labelFontSize = 42.sp,
        )
    Column(
        modifier = Modifier
            .padding(24.dp, 0.dp, 16.dp, 0.dp)
            .fillMaxSize()
    ) {
        Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData = dataChart, 3))
        DonutPieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            dataChart,
            pieChartConfig
        ) { slice ->
            when (slice.label) {
                "Tarik Tunai" -> {
                    detailCashVisible = true
                    detailGopayVisible = false
                    detailOtherVisible = false
                    detailQrisVisible = false
                }
                "QRIS Payment" -> {
                    detailCashVisible = false
                    detailGopayVisible = false
                    detailOtherVisible = false
                    detailQrisVisible = true
                }
                "Topup Gopay" -> {
                    detailCashVisible = false
                    detailGopayVisible = true
                    detailOtherVisible = false
                    detailQrisVisible = false
                }
                "Lainnya" -> {
                    detailCashVisible = false
                    detailGopayVisible = false
                    detailOtherVisible = true
                    detailQrisVisible = false
                }
            }
        }
        if (detailCashVisible)
        {
            Text(text = "Detail Transaksi Cash", modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp))
            DataDetailTransaction(detailTransactions = detailDataCash)
        }
        else if (detailGopayVisible)
        {
            Text(text = "Detail Transaksi Gopay", modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp))
            DataDetailTransaction(detailTransactions = detailDataGopay)
        }
        else if (detailQrisVisible)
        {
            Text(text = "Detail Transaksi QRIS", modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp))
            DataDetailTransaction(detailTransactions = detailDataQris)
        }
        else if (detailOtherVisible)
        {
            Text(text = "Detail Transaksi Lainnya", modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp))
            DataDetailTransaction(detailTransactions = detailDataOther)
        }
    }
}

@Composable
fun DataDetailTransaction(detailTransactions : List<DetailDataTransaction>){
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 16.dp, 0.dp, 8.dp)
    ){
        items(detailTransactions){ detailTransaction ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 16.dp, 0.dp),
                colors = CardColors(
                    colorScheme.primaryContainer, colorScheme.primary,
                    colorScheme.inversePrimary, colorScheme.inverseSurface
                ),
                shape = MaterialTheme.shapes.medium)
            {
                Text(text = detailTransaction.trxDate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp, 8.dp, 8.dp)
                        .align(Alignment.CenterHorizontally) )
                Text(text = detailTransaction.amount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp, 8.dp, 16.dp)
                        .align(Alignment.CenterHorizontally) )
            }
        }
    }
}
