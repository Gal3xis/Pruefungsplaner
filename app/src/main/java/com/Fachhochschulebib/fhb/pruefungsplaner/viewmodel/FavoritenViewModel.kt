package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application

class FavoritenViewModel(application: Application) : BaseViewModel(application) {

    val liveFavorits = repository.getAllFavoritsLiveData()

}