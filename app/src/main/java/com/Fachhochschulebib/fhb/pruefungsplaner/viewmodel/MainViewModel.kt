package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {


    val liveEntriesOrdered = repository.getAllEntriesLiveDataByDate()

    val liveSelectedFaculty = MutableLiveData<Faculty?>()

    val liveFilteredEntriesByDate = MutableLiveData<List<TestPlanEntry>?>()

    val liveEntriesForCourse = MutableLiveData<List<TestPlanEntry>?>()

    val liveChoosenCourses = repository.getAllChoosenCoursesLiveData()

    val liveFilteredFavorits = MutableLiveData<List<TestPlanEntry>?>()

    var liveCoursesForFacultyId = MutableLiveData<List<Course>?>()

    fun getSelectedFaculty(){
        viewModelScope.launch {
            val id = getReturnFaculty()
            val faculty = id?.let { getFacultyById(it) }
            liveSelectedFaculty.postValue(faculty)
        }
    }

    fun filterCoursename(){
        viewModelScope.launch {
            val entriesForCourse = repository.getEntriesForCourseLiveData(Filter.courseName)
            liveEntriesForCourse.postValue(entriesForCourse)
        }
    }

    fun filter() {
        viewModelScope.launch {
            val entriesByDate = repository.getAllEntries()?.let { Filter.validateList(it) }
            val favorits = repository.getFavorites(true)
            liveFilteredEntriesByDate.postValue(entriesByDate)
            liveFilteredFavorits.postValue(favorits?.let { Filter.validateList(it) })
        }
    }
}