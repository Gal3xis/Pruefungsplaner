package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FeedbackViewModel(application: Application) : BaseViewModel(application) {

    fun sendFeedBack(
        ratingUsability: Float,
        ratingFunctions: Float,
        ratingStability: Float,
        text: String
    ) {
        viewModelScope.launch (coroutineExceptionHandler){
            val uuid = getUuid()?.uuid
            uuid?.let { repository.sendFeedBack(it,ratingUsability,ratingFunctions,ratingStability,text) }
        }
    }

}