package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Course
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import kotlinx.coroutines.launch

class StartViewModel(application: Application) : BaseViewModel(application) {



    val liveFaculties = repository.getAllFacultiesLiveData()
    fun addMainCourse(course: Course) {
        addMainCourse(course.courseName)
    }

    /**
     * Selects a course a the main course.
     *
     * @param[choosenCourse] The course that is supposed to be the mein course.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun addMainCourse(choosenCourse: String) {
        viewModelScope.launch {
            val returnCourse =  getCourseId(choosenCourse)
            setSelectedCourse(choosenCourse)
            returnCourse?.let { setReturnCourse(it) }
            if (getUuid() == null) {
                returnCourse?.let { firstStart(it) }
            } else {
                //TODO RetrofitConnect(context).setUserCourses()
            }
        }
    }

    val liveCoursesForFaculty = MutableLiveData<List<Course>?>()
    override fun setReturnFaculty(faculty: Faculty){
        super.setReturnFaculty(faculty)
        viewModelScope.launch {
            val courses = getCoursesByFacultyid(faculty.fbid)
            liveCoursesForFaculty.postValue(courses)
        }
    }

    fun checkLoginStatus():Boolean {
        return getSelectedCourse()!=null
    }

    fun firstStart(faculty: Faculty){
        viewModelScope.launch(coroutineExceptionHandler) {
            if(repository.getUuid()!=null){
                return@launch
            }
            val uuid = repository.fetchUUID(faculty)
            uuid?.uuid?.let { repository.insertUuid(it) }
        }
    }

    fun firstStart(faculty: String){
        viewModelScope.launch(coroutineExceptionHandler) {
            val uuid = repository.fetchUUID(faculty)
            uuid?.uuid?.let { repository.insertUuid(it) }
        }
    }

}