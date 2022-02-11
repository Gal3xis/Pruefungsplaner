package com.Fachhochschulebib.fhb.pruefungsplaner.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Courses
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.Faculty
import kotlinx.coroutines.launch

class StartViewModel(application: Application) : MainViewModel(application) {



    val liveFaculties = repository.getAllFacultiesLiveData()

    val liveSelectedFaculty = MutableLiveData<Faculty>()

    fun addMainCourse(course: Courses) {
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
        val returnCourse =  getCourseId(choosenCourse)
        setSelectedCourse(choosenCourse)
        returnCourse?.let { setReturnCourse(it) }
        if (getUuid() == null) {
            //TODO RetrofitConnect(context).firstStart()
        } else {
            //TODO RetrofitConnect(context).setUserCourses()
        }
    }

    fun fetchSelectedFaculty(){
        viewModelScope.launch {
            liveSelectedFaculty.postValue(getReturnCourse()?.let { getFacultyById(it) })
        }
    }

    fun checkLoginStatus():Boolean {
        return getSelectedCourse()!=null
    }


}