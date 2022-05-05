package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.SettingsFragment].
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class SettingsViewModel(application: Application) : BaseViewModel(application) {

    /**
     * Live Data containing all favorites.
     */
    val liveFavorites = repository.getAllFavoritesLiveData()

    /**
     * Live Data containing all [com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry]-Objects in the local database.
     */
    val liveEntries = repository.getAllEntriesLiveDataByDate()

    /**
     * Sets in the sharedPreferences, if the calendar should be synced or not.
     *
     * @param context The Applicationcontext
     * @param sync If the calendar should be synced or not.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun setCalendarSync(context: Context,sync: Boolean){
        setCalendarSync(sync)
        if(sync) updateCalendar(context) else deleteAllFromCalendar(context)
    }


    /**
     * Deletes all favorites.
     *
     * @param context The Applicationcontext
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteAllFavorites(context: Context){
        viewModelScope.launch {
            repository.unselectAllFavorites()
            updateCalendar(context)
        }
    }
}