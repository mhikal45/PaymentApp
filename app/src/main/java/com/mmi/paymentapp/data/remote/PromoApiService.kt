package com.mmi.paymentapp.data.remote

import com.mmi.paymentapp.data.model.PromotionResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface PromoApiService {

    companion object{
        val BASE_URL = "https://content.digi46.id/"
    }

    @GET("api/promos")
    suspend fun getPromotions(
        @Header("Authorization") token : String = "\"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWF0IjoxNjc1OTE0MTUwLCJleHAiOjE2Nzg1MDYxNTB9.TcIgL5CDZYg9o8CUsSjUbbUdsYSaLutOWni88ZBs9S8\""
    ) : PromotionResponse
}