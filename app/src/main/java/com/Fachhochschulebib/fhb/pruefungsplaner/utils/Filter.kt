package com.Fachhochschulebib.fhb.pruefungsplaner.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.Fachhochschulebib.fhb.pruefungsplaner.model.room.TestPlanEntry
import java.text.SimpleDateFormat
import java.util.*

/**
 * Inner Class to filter the table of moduls. Used by TermineFragment-fragment and FavoritenFragment-fragment.
 *
 * @author Alexander Lange
 * @since 1.6
 * @see Terminefragment
 * @see Favoritenfragment
 */
object Filter {
    /**
     * Parameter to lock the Filter.
     * If it is set to true, all listener are not going to be called when a value is changed.
     * If it is set back to false, the [filterChanged()]-Method is called.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    var locked = false
        set(value) {
            field = value
        }

    /**
     * Parameter to Filter with the Modulename.
     * Calls the [onModuleNameChangedListener] and the [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onModulNameChangedListener
     * @see onFilterChangedListener
     */
    var modulName: String? = null
        set(value) {
            field = value
            if (locked) {
                return
            }
            modulNameChanged()
            filterChanged()
        }

    /**
     * Parameter to Filter with the Coursename.
     * Calls the [onCourseNameChangedListener] and the [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onCourseNameChangedListener
     * @see onFilterChangedListener
     */
    var courseName: String? = null
        set(value) {
            field = value
            if (locked) {
                return
            }
            courseNameChanged()
        }

    /**
     * Parameter to Filter with a specific date.
     * Calls the [onDateChangedListener] and the [onFilterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onDateChangedListener
     * @see onFilterChangedListener
     */
    var datum: Date? = null
        set(value) {
            field = value
            if (locked) {
                return
            }
            dateChanged()
            filterChanged()
        }

    /**
     * Parameter to filter with a specific examiner.
     * Calls the [onExaminerChangedListener] and [onFilterChangedListener].
     * @author Alexander Lange
     * @since 1.6
     * @see onExaminerChangedListener
     * @see onFilterChangedListener
     */
    var examiner: String? = null
        set(value) {
            field = value
            if (locked) {
                return
            }
            examinerChanged()
            filterChanged()
        }

    /**
     * Array of 6 semester, where each field contains a boolean, if the semester is selected (true), or not (false)
     *
     * @author Alexander Lange
     * @since 1.6
     */
    var semester: Array<Boolean> = arrayOf(true, true, true, true, true, true)
        set(value) {
            return
        }

    /**
     * Public method to set the value for a specific semester.
     * Calls the [onSemesterChangedListener] and the [onFilterChangedListener]
     * @param[pSemester] The semester to set the value.
     * @param[active] If the semester is checked or not.
     * @author Alexander Lange
     * @since 1.6
     * @see onSemesterChangedListener
     * @see onFilterChangedListener

     */
    fun SetSemester(pSemester: Int, active: Boolean) {
        semester[pSemester] = active
        if (locked) {
            return
        }
        semesterChanged()
        filterChanged()
    }


    /**
     * Invokes every Method, appended to the onModuleNameChangedListener.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onModulNameChangedListener
     */
    private fun modulNameChanged() {
        Log.d("Filter", "Modul changed")
        /*for (i in onModulNameChangedListener) {
            i.invoke()
        }*/
    }

    /**
     * Invokes every Method, appended to the onCourseNameChangedListener.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onCourseNameChangedListener
     */
    private fun courseNameChanged() {
        Log.d("Filter", "Course changed")
        /*for (i in onCourseNameChangedListener) {
            i.invoke()
        }*/
    }

    /**
     * Invokes every Method, appended to the onDateChangedListener.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onDateChangedListener
     */
    private fun dateChanged() {
        /*for (i in onDateChangedListener) {
            i.invoke()
        }*/
    }

    /**
     * Invokes every method, appended to the onExaminerChangedListener.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onExaminerChangedListener
     */
    private fun examinerChanged() {
        /*for (i in onExaminerChangedListener) {
            i.invoke()
        }*/

    }

    /**
     * Invokes every method, appended to the [onSemesterChangedListener].
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onSemesterChangedListener
     */
    private fun semesterChanged() {
        /*for (i in onSemesterChangedListener) {
            i.invoke()
        }*/

    }

    /**
     * Invokes every Method, appended to the onFilterChangedListener.
     *
     * @author Alexander Lange
     * @since 1.6
     * @see onFilterChangedListener
     */
    private fun filterChanged() {
        for (i in onFilterChangedListener) {
            i.invoke()
        }
    }

    /**
     * Validates a testplanentry-Object. Checks if all Filter-values agree with the given entry.
     *
     * @param[context] Current context
     * @param[entry] The Entry that needs to be validated
     * @return true->The entry agrees with the filter,false->the entry does not agree with the filter
     * @author Alexander Lange
     * @since 1.6
     */
    fun validateFilter(entry: TestPlanEntry?): Boolean {
        if (entry == null) {
            return false
        }
        if (modulName!=null&&entry.module?.lowercase()?.startsWith(
                modulName?.lowercase() ?: entry.module?.lowercase() ?: "-1"
            ) == false
        ) {
            return false
        }
        if (entry.course?.lowercase()?.startsWith(
                courseName?.lowercase() ?: entry.course?.lowercase() ?: "-1"
            ) == false
        ) {
            return false
        }
        if (datum != null) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(entry.date)
            if (!datum!!.atDay(date)) {
                return false
            }
        }
        if (entry.firstExaminer?.lowercase()?.startsWith(
                examiner?.lowercase() ?: entry.firstExaminer?.lowercase() ?: "-1"
            ) == false
        ) {
            return false
        }
        if (entry.semester != null && !semester[entry.semester!!.toInt().minus(1)]) {
            return false
        }
        return true
    }

    fun validateList(list: List<TestPlanEntry>): List<TestPlanEntry> {
        val ret = mutableListOf<TestPlanEntry>()
        list.forEach {
            if (validateFilter(it)) {
                ret.add(it)
            }
        }
        return ret
    }

    fun validateList(liveData:LiveData<List<TestPlanEntry>?>):LiveData<List<TestPlanEntry>?>{
        val list = liveData.value
        val filtered = list?.let { validateList(it) }
        val ret = MutableLiveData<List<TestPlanEntry>>()
        ret.postValue(filtered)
        return ret
    }

    /**
     * Resets the Filter, sets every value to null.
     * Calls the onResetListener.
     *
     * @author Alexander Lange
     * @since 1.6
     */
    fun reset() {
        courseName = null
        modulName = null
        datum = null
        semester.fill(true)
        filterChanged()
        /*for (i in onResetListener) {
            i.invoke()
        }*/
    }

    //Declaration and empty initilization of every listener. To add an Method, write: listenerX.add{ ... }
//    var onModulNameChangedListener: MutableList<() -> Unit> = mutableListOf()
//    var onCourseNameChangedListener: MutableList<() -> Unit> = mutableListOf()
//    var onDateChangedListener: MutableList<() -> Unit> = mutableListOf()
//    var onExaminerChangedListener: MutableList<() -> Unit> = mutableListOf()
//    var onSemesterChangedListener: MutableList<() -> Unit> = mutableListOf()
      var onFilterChangedListener: MutableList<() -> Unit> = mutableListOf()
//    var onResetListener: MutableList<() -> Unit> = mutableListOf()
}
