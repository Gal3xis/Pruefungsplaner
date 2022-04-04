package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ListenableWorker
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import com.Fachhochschulebib.fhb.pruefungsplaner.utils.PushService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainViewModel(application: Application) : BaseViewModel(application) {


    val liveEntriesOrdered = repository.getAllEntriesLiveDataByDate()

    val liveSelectedFaculty = MutableLiveData<Faculty?>()

    val liveFilteredEntriesByDate = MutableLiveData<List<TestPlanEntry>?>()

    val liveEntriesForCourse = MutableLiveData<List<TestPlanEntry>?>()

    val liveChoosenCourses = repository.getAllChoosenCoursesLiveData()

    val liveFilteredFavorits = MutableLiveData<List<TestPlanEntry>?>()

    var liveCoursesForFacultyId = MutableLiveData<List<Course>?>()

    var liveProfList = repository.getFirstExaminerNames()




    fun getSelectedFaculty(){
        viewModelScope.launch {
            val id = getReturnFaculty()
            val faculty = id?.let { getFacultyById(it) }
            liveSelectedFaculty.postValue(faculty)
        }
    }

    fun filterCoursename(){
        viewModelScope.launch {
            val entriesForCourse =Filter.courseName?.let {repository.getEntriesForCourseLiveData(it)  }?:repository.getAllEntries()
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