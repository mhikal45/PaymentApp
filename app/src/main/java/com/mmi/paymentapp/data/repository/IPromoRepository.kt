package com.mmi.paymentapp.data.repository

import com.mmi.paymentapp.data.model.Promotion
import kotlinx.coroutines.flow.Flow

interface IPromoRepository {
    suspend fun getPromotions() : Flow<List<Promotion>>
}