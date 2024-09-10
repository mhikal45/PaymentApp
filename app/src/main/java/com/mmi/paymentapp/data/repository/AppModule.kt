package com.mmi.paymentapp.data.repository

import android.app.Application
import androidx.room.Room
import com.mmi.paymentapp.data.local.PaymentAppDatabase
import com.mmi.paymentapp.data.remote.PromoApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import kotlin.math.log

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun ProvidePaymentAppDatabase (app : Application) : PaymentAppDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = PaymentAppDatabase::class.java,
            name = "PaymentApp.db"
        ).build()
    }

    @Provides
    @Singleton
    fun ProvideTransactionRepository(paymentAppDatabase: PaymentAppDatabase) : TransactionRepository {
        return TransactionRepository(paymentAppDatabase.PaymentAppDao)
    }

    @Provides
    @Singleton
    fun ProvideMoshi() : Moshi{
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun ProvideApiService(moshi: Moshi) : PromoApiService{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val httpClient = OkHttpClient.Builder().addInterceptor(logging).build()
        return Retrofit.Builder()
            .run {
                baseUrl(PromoApiService.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .client(httpClient)
                    .build()
            }.create(PromoApiService::class.java)
    }

    @Provides
    @Singleton
    fun ProvidePromotionRepository(paymentAppDatabase: PaymentAppDatabase) : PromoRepository {
        return PromoRepository(ProvideApiService(ProvideMoshi()), paymentAppDatabase.PromotionDao)
    }
}