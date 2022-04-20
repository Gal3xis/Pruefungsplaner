package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.GoogleCalendarIO
import kotlinx.coroutines.launch

/**
 * ViewModel for the [com.Fachhochschulebib.fhb.pruefungsplaner.view.fragments.SettingsFragment].
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class SettingsViewModel(application: Application) : BaseViewModel(application) {

    val liveFavorites = repository.getAllFavoritesLiveData()

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
        if(sync) updateCalendar(context) else deleteCalendarEntries(context)
    }

    /**
     * Returns a list of all Eventids in the calendar.
     *
     * @param context The Applicationcontext.
     *
     * @return A list with all ids for the events currently stored in the calendar.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getAllCalendarEntries(context: Context):List<Long>{
        return liveFavorites.value?.let {
            GoogleCalendarIO.findEventIds(context, it)
        }?: listOf()
    }

    /**
     * Returns a list of all modulenames currently stored in the calendar.
     *
     * @param context The Apllicationcontext
     *
     * @return A list of all modulenames for the events currently stored in the calendar
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getCalendarModuleNames(context: Context):List<String>{
        return liveFavorites.value?.let {
            GoogleCalendarIO.findEventModuleNames(context, it)
        }?: listOf()
    }

    /**
     * Deletes all events in the calendar for the application.
     *
     * @param context The Applicationcontext
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun deleteCalendarEntries(context: Context){
        liveFavorites.value?.let{GoogleCalendarIO.deleteAll(context,it)}
    }

    /**
     * Updates the google calendar with the new favorites.
     *
     * @param context The Applicationcontext
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun updateCalendar(context: Context){
        liveFavorites.value?.let { GoogleCalendarIO.update(context,it,getCalendarInsertionType()) }
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
            repository.unselectAllFavorits()
            updateCalendar(context)
        }
    }
}