package com.mmi.paymentapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmi.paymentapp.data.model.Promotion
import com.mmi.paymentapp.data.repository.PromoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromotionViewModel @Inject constructor(
    private val promoRepository: PromoRepository
) : ViewModel() {
    val _allPromotions = MutableStateFlow(emptyList<Promotion>())
    val allPromotions = _allPromotions.asStateFlow()

    fun GetAllPromotions() {
        viewModelScope.launch(IO) {
            promoRepository.getPromotions().collectLatest {
                _allPromotions.tryEmit(it)
            }
        }
    }
}