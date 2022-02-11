package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application

class FavoritenViewModel(application: Application) : MainViewModel(application) {

    val liveFavorits = repository.getAllFavoritsLiveData()

}