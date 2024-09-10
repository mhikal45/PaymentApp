package com.mmi.paymentapp

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.mmi.paymentapp.data.local.PaymentAppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PaymentApp : Application() {
}