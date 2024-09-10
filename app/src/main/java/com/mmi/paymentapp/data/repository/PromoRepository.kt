package com.mmi.paymentapp.data.repository

import com.mmi.paymentapp.data.local.PromotionDao
import com.mmi.paymentapp.data.model.Promotion
import com.mmi.paymentapp.data.remote.PromoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PromoRepository @Inject constructor(
    private val apiService: PromoApiService, private val promotionDao: PromotionDao
) : IPromoRepository {
    override suspend fun getPromotions() : Flow<List<Promotion>> {

        val onlinePromotions = apiService.getPromotions().data

        var localPromotions = promotionDao.getPromotions()
        val tempPromotions = mutableListOf<Promotion>()

        for (promo in onlinePromotions!!){
            var localPromo = localPromotions.first().firstOrNull { it.alt == promo!!.attributes!!.alt }
            if (localPromo == null){
                promotionDao.AddPromotion(promo!!.attributes!!)
                localPromo = promo.attributes!!
            }

            tempPromotions.add(localPromo)
        }

        localPromotions = flow{
            this.emit(tempPromotions)
        }

        return localPromotions
    }
}