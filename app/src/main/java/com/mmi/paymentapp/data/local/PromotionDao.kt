package com.mmi.paymentapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mmi.paymentapp.data.model.Promotion
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionDao {
    @Upsert
    suspend fun AddPromotion(promo : Promotion)

    @Query("SELECT * FROM `Promotion` ORDER BY PromotionId DESC")
    fun getPromotions() : Flow<List<Promotion>>
}