package com.fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import com.fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import kotlinx.coroutines.launch

/**
 * ViewModel for the [com.fachhochschulebib.fhb.pruefungsplaner.view.activities.StartActivity]
 *
 * @author Alexander Lange (Email:alexander.lange@fh-bielefeld.de)
 * @since 1.6
 */
class StartViewModel(application: Application) : BaseViewModel(application) {

    val liveFaculties = repository.getAllFacultiesLiveData()
    val liveCoursesForFaculty = MutableLiveData<List<Course>?>()

    /**
     * Selects a course a the main course.
     *
     * @param[course] The course that is supposed to be the mein course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun addMainCourse(course: Course) {
        addMainCourse(course.courseName)
    }

    /**
     * Selects a course as the main course.
     *
     * @param[choosenCourse] The name of th course that is supposed to be the main course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun addMainCourse(choosenCourse: String) {
        viewModelScope.launch {
            val id =  getCourseId(choosenCourse)?:return@launch
            setMainCourse(id)
            if (getUuid() == null) {
                firstStart(id)
            } else {
                //TODO RetrofitConnect(context).setUserCourses()
            }
        }
    }

    /**
     * Sets the selected Faculty. Override to update the liveData.
     *
     * @param faculty The new Faculty
     *
     * @author Alexander Lange
     * @since 1.6
     */
    override fun setSelectedFaculty(faculty: Faculty){
        super.setSelectedFaculty(faculty)
        viewModelScope.launch {
            val courses = getCoursesByFacultyid(faculty.fbid)
            liveCoursesForFaculty.postValue(courses)
        }
    }

    /**
     * Checks if the user already selected a maincourse. If that is the case, he is not asked again and instead redirected to the MainActivity.
     *
     * @return Wheter a course has been picked or not.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun checkLoginStatus():Boolean {
        return getMainCourseId()!=null
    }

    /**
     * Updates the UUID on the Rest-Api when the app is started for the first time.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun firstStart(faculty: String){
        viewModelScope.launch(coroutineExceptionHandler) {
            val uuid = repository.fetchUUID(faculty)
            uuid?.uuid?.let { repository.insertUuid(it) }
        }
    }
}