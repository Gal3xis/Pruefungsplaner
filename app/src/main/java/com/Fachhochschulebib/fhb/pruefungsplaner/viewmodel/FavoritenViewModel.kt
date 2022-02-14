package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application

class FavoritenViewModel(application: Application) : BaseViewModel(application) {

    val liveFavorits = repository.getAllFavoritsLiveData()

    fun getPruefungszeitraum(): String? {
        val start = getStartDate()
        val end = getEndDate()

        if(start==null||end==null)return null

        return sdfDisplay.format(start) + "-" + sdfDisplay.format(end)
    }
}