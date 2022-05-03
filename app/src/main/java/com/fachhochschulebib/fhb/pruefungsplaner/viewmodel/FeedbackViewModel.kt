package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.FeedbackFragment]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class FeedbackViewModel(application: Application) : BaseViewModel(application) {

    /**
     * Send feedback to the server.
     *
     * @param ratingUsability How good is the usability of the application
     * @param ratingFunctions Are there enough functions in the application
     * @param ratingStability How stable was the application, did it crashes?
     * @param text A comment from the user.
     *
     * @author Alexander Lange
     * @since 1.6
     */
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