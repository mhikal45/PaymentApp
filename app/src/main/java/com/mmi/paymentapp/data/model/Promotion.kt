package com.mmi.paymentapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Promotion(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "PromotionId")
	val promotionId : Int = 0,
	@ColumnInfo(name = "createdAt")
	val createdAt: String? = null,
	@ColumnInfo(name = "nama")
	val nama: String? = null,
	@ColumnInfo(name = "namePromo")
	val namePromo: String? = null,
	@ColumnInfo(name = "lokasi")
	val lokasi: String? = null,
	@ColumnInfo(name = "latitude")
	val latitude: String? = null,
	@ColumnInfo(name = "count")
	val count: Int? = null,
	@ColumnInfo(name = "alt")
	val alt: Int? = null,
	@ColumnInfo(name = "title")
	val title: String? = null,
	@ColumnInfo(name = "descPromo")
	val descPromo: String? = null,
	@ColumnInfo(name = "desc")
	val desc: String? = null,
	@ColumnInfo(name = "longitude")
	val longitude: String? = null,
	@ColumnInfo(name = "updatedAt")
	val updatedAt: String? = null
)