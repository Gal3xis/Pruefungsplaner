package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import com.fachhochschulebib.fhb.pruefungsplaner.utils.Filter
import kotlinx.coroutines.launch

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.activities.MainActivity]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

    val liveEntriesOrdered = repository.getAllEntriesLiveDataByDate()
    val liveSelectedFaculty = MutableLiveData<Faculty?>()
    val liveEntriesForCourse = MutableLiveData<List<TestPlanEntry>?>()
    val liveChoosenCourses = repository.getAllChoosenCoursesLiveData()
    var liveProfList = repository.getFirstExaminerNames()
    val liveMainCourse = MutableLiveData<Course>()

    /**
     * Gets the selected Faculty from the room database.
     * Stores the result in the [liveSelectedFaculty]-LiveDataObject
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getSelectedFacultyName() {
        viewModelScope.launch {
            val id = getSelectedFaculty() ?: return@launch
            val faculty = getFacultyById(id)
            liveSelectedFaculty.postValue(faculty)
        }
    }

    /**
     * Gets a list of all entries for the coursename selected in the Filter.
     * Stores the result in the [liveEntriesForCourse]-LiveDataObject.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun filterCoursename() {
        viewModelScope.launch {
            val entriesForCourse = Filter.courseName?.let {
                repository.getEntriesForCourseLiveData(it)
            } ?: repository.getAllEntries()
            liveEntriesForCourse.postValue(entriesForCourse)
        }
    }

    fun getMainCourse() {
        viewModelScope.launch {
          val id = getMainCourseId() ?: return@launch
            val course = repository.getAllCourses()?.find { c ->
                c.sgid == id
            }?:return@launch
            liveMainCourse.postValue(course)
        }
    }
}