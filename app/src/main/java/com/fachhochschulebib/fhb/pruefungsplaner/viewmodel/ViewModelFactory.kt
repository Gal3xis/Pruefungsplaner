package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider

/**
 * Factory to create a new ViewModel in the Fragments and Activities.
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class ViewModelFactory(application: Application):ViewModelProvider.AndroidViewModelFactory(application)