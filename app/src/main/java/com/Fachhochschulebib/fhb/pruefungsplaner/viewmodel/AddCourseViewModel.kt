package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.model.retrofit.RetrofitConnect
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AddCourseViewModel(application: Application) : BaseViewModel(application) {

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

    fun getCourses(){
        viewModelScope.launch {
            val courses = getReturnFaculty()?.let { getCoursesByFacultyid(it) }
            liveCoursesForFaculty.postValue(courses)
        }
    }
}