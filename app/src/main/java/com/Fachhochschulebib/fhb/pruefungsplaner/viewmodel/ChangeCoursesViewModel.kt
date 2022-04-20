package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitConnect
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * ViewModel for the [com.Fachhochschulebib.fhb.pruefungsplaner.view.fragments.ChangeCoursesFragment].
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class ChangeCoursesViewModel(application: Application) : BaseViewModel(application) {

    val liveCoursesForFaculty = MutableLiveData<List<Course>?>()

    /**
     * Updates the Room-Database with data from the Server.
     *
     * @since 1.6
     * @author Alexander Lange
     */
    fun updateDbEntries() {
        viewModelScope.launch {
            val courses = getAllCourses()
            val courseIds = JSONArray()
            var courseName: String
            if (courses != null) {
                for (course in courses) {
                    try {
                        courseName = course.courseName ?: ""
                        if (!course.choosen) {
                            val toDelete = getEntriesByCourseName(courseName, false)
                            toDelete?.let { deleteEntries(it) }
                        }
                        if (getOneEntryByName(
                                        courseName,
                                        false
                                ) == null && course.choosen
                        ) {
                            val idJson = JSONObject()
                            idJson.put("ID", course.sgid)
                            courseIds.put(idJson)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
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
    fun changeMainCourse(course:String){
        if(course == getSelectedCourse()) return
        viewModelScope.launch {
            setSelectedCourse(course)
            repository.updateCourse(course,true)
            getCourses()
        }
    }

    /**
     * Loads new courses for a specific faulty in the livadata-object.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun getCourses(){
        viewModelScope.launch {
            val courses = getReturnFaculty()?.let { getCoursesByFacultyid(it) }
            liveCoursesForFaculty.postValue(courses)
        }
    }
}