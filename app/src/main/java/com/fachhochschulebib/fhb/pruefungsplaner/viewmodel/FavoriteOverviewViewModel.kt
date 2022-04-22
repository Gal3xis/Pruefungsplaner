package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application

/**
 * The ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.FavoriteOverviewFragment]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class FavoriteOverviewViewModel(application: Application) : BaseViewModel(application) {

    val liveFavorits = repository.getAllFavoritesLiveData()


    /**
     * Gets the timespan for the next period.
     *
     * @return The timespan as a string to display in the ui.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getPeriodeTimeSpan(): String? {
        val start = getStartDate()
        val end = getEndDate()

        if(start==null||end==null)return null

        return sdfDisplay.format(start) + "-" + sdfDisplay.format(end)
    }
}