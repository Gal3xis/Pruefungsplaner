package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for the [com.Fachhochschulebib.fhb.pruefungsplaner.view.fragments.FeedbackFragment]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
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