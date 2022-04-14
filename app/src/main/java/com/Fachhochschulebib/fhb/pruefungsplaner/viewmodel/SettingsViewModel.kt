package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.GoogleCalendarIO
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : BaseViewModel(application) {

    val liveFavorits = repository.getAllFavoritsLiveData()

    fun setCalendarSync(context: Context,sync: Boolean){
        setCalendarSync(sync)
        updateCalendar(context)
    }

    fun getAllCalendarEntries(context: Context):List<Long>{
        return GoogleCalendarIO.findEventIds(context)
    }

    fun getCalendarModuleNames(context: Context):List<String>{
        return GoogleCalendarIO.findEventModuleNames(context)
    }

    fun deleteFavorits(context: Context){
        viewModelScope.launch {
            repository.unselectAllFavorits()
            updateCalendar(context)
        }
    }
}