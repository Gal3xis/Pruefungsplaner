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
        if(sync) updateCalendar(context) else deleteCalendarEntries(context)
    }

    fun getAllCalendarEntries(context: Context):List<Long>{
        return liveFavorits.value?.let { GoogleCalendarIO.findEventIds(context, it) }?: listOf()
    }

    fun getCalendarModuleNames(context: Context):List<String>{
        return liveFavorits.value?.let { GoogleCalendarIO.findEventModuleNames(context, it) }?: listOf()
    }

    fun deleteCalendarEntries(context: Context){
        liveFavorits.value?.let{GoogleCalendarIO.deleteAll(context,it)}
    }

    fun updateCalendar(context: Context){
        liveFavorits.value?.let { GoogleCalendarIO.update(context,it,getCalendarInsertionType()) }
    }

    fun deleteFavorits(context: Context){
        viewModelScope.launch {
            repository.unselectAllFavorits()
            updateCalendar(context)
        }
    }
}