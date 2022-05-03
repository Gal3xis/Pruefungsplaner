package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application

/**
 * The ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.FavoriteOverviewFragment]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class FavoriteOverviewViewModel(application: Application) : BaseViewModel(application) {

    /**
     * Live Data containing all favorite entries.
     */
    val liveFavorites = repository.getAllFavoritesLiveData()
}