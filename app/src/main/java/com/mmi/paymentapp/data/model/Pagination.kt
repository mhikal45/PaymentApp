package com.mmi.paymentapp.data.model

import android.os.Parcelable

data class Pagination(
	val pageCount: Int? = null,
	val total: Int? = null,
	val pageSize: Int? = null,
	val page: Int? = null
)