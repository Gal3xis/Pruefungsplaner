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

    /**
     * Live Data containing all entries, ordered by the exam date.
     */
    val liveEntriesOrdered = repository.getAllEntriesLiveDataByDate()

    /**
     * Live Data for storing the selected faculty. Is set in [getSelectedFaculty].
     */
    val liveSelectedFaculty = MutableLiveData<Faculty?>()

    /**
     * Live Data for storing all entries either for a specific course or if not course was selected, for all chosen courses.
     */
    val liveEntriesForCourse = MutableLiveData<List<TestPlanEntry>?>()

    /**
     * Live Data containg all chosen courses.
     */
    val liveChoosenCourses = repository.getAllChosenCoursesLiveData()

    /**
     * Live Data containing the names of all first examiners.
     */
    var liveProfList = repository.getFirstExaminerNamesLiveData()

    /**
     * Live Data for storing the maincourse.
     */
    val liveMainCourse = MutableLiveData<Course>()

    /**
     * Gets the selected Faculty from the room database.
     * Stores the result in the [liveSelectedFaculty]-LiveDataObject
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getSelectedFaculty() {
        viewModelScope.launch {
            val id = getSelectedFacultyId() ?: return@launch
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
                repository.getEntriesForCourse(it)
            } ?: repository.getAllEntries()
            liveEntriesForCourse.postValue(entriesForCourse)
        }
    }

    /**
     * Gets the main course from the shared preferences and stores it in the [liveMainCourse].LiveData-Object.
     *
     * @author Alexander Lange
     * @since 1.6
     */
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