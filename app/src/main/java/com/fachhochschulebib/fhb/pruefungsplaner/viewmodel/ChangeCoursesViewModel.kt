package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.fragments.ChangeCoursesFragment].
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class ChangeCoursesViewModel(application: Application) : BaseViewModel(application) {

    /**
     * Live Data for storing all courses for a selected Faculty.
     * Is set in [getCourses]
     */
    val liveCoursesForFaculty = MutableLiveData<List<Course>?>()


    /**
     * Updates the Room-Database with data from the Server.
     *
     * @since 1.6
     * @author Alexander Lange
     */
    fun updateDbEntries() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val courses = getAllCourses() ?: return@launch
            val courseIds = JSONArray()
            var courseName: String
            for (course in courses) {
                courseName = course.courseName
                if (!course.chosen) {
                    val toDelete = getFavoritesByCourseName(courseName, false)
                    toDelete?.let { deleteEntries(it) }
                }
                if (checkCourseForFavorites(courseName) && course.chosen
                ) {
                    val idJson = JSONObject()
                    idJson.put("ID", course.sgid)
                    courseIds.put(idJson)
                }
            }
        }
    }

    /**
     * Changes the maincourse for the user. If the new course is not a favorite yet,
     * it is also updated.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun changeMainCourse(course: String) {
        viewModelScope.launch {
            val id = getCourseId(course) ?: return@launch
            if (id == getMainCourseId()) return@launch
            setMainCourse(id)
            repository.updateCourse(course, true)
            getCourses()
        }
    }

    /**
     * Loads new courses for a specific faulty in the livedata-object.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getCourses() {
        viewModelScope.launch {
            val courses = getSelectedFacultyId()?.let { getCoursesByFacultyId(it) }
            liveCoursesForFaculty.postValue(courses)
        }
    }
}